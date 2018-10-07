package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : liuqi
 * createTime : 2018-09-17
 * description : 从索引库搜索商品的dao
 * version : 1.0
 */
@Repository
public class SearchDao {

    @Autowired
    private HttpSolrClient httpSolrClient;

    @Autowired
    private SearchItemMapper searchItemMapper;

    public SearchResult search(SolrQuery query) throws SolrServerException, IOException {
        SearchResult result = new SearchResult();
        // 根据条件查询索引库
        QueryResponse response = httpSolrClient.query(query);
        // 结果集
        SolrDocumentList documentList = response.getResults();
        // 设置searchResult记录数
        result.setRecordCount(documentList.getNumFound());
        // 遍历结果集
        List<SearchItem> searchItemList = new ArrayList<>();
        // 高亮
        Map<String,Map<String,List<String>>> highlighting= response.getHighlighting();

        for(SolrDocument solrDocument : documentList){
            SearchItem item = new SearchItem();

            item.setId(Long.parseLong(getDocumentValue(solrDocument,"id")));
            item.setCategory_name(getDocumentValue(solrDocument,"item_category_name"));
            item.setPrice(Long.parseLong(getDocumentValue(solrDocument,"item_price")));
            item.setSell_point(getDocumentValue(solrDocument,"item_sell_point"));

            //取高亮
            List<String> highlightList = highlighting.get(solrDocument.get("id")).get("item_title");
            String highlightStr = "";
            if(highlightList != null && highlightList.size() > 0){
                highlightStr = highlightList.get(0);
            }else{
                highlightStr = getDocumentValue(solrDocument,"item_title");
            }

            item.setTitle(highlightStr);
            searchItemList.add(item);
        }

        // 设置SearchResult属性
        result.setItemList(searchItemList);

        return result;
    }

    /**
     * 根据itemid更新索引
     * @param itemId
     * @return
     * @throws Exception
     */
    public TaotaoResult updateItemById(Long itemId) throws Exception {
        SearchItem searchItem = searchItemMapper.getItemById(itemId);

        //2.创建solrinputdocument
        SolrInputDocument document = new SolrInputDocument();
        //3.向文档中添加域
        document.addField("id", searchItem.getId());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
        //4.添加文档到索引库中
        httpSolrClient.add(document);
        httpSolrClient.close();
        return  TaotaoResult.ok();
    }

    public String getDocumentValue(SolrDocument solrDocument,String key){
        String val = "";
        if(solrDocument.get(key) != null){
            val = solrDocument.get(key).toString();
        }
        return val;
    }


}
