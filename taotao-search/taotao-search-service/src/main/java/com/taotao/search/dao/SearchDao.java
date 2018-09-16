package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.management.Query;
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
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query) throws SolrServerException {
        SearchResult result = new SearchResult();
        // 根据条件查询索引库
        QueryResponse response = solrServer.query(query);
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

            item.setId(Long.parseLong(solrDocument.get("id").toString()));
            item.setCategory_name(solrDocument.get("item_category_name").toString());
            item.setPrice(Long.parseLong(solrDocument.get("item_price").toString()));
            item.setSell_point(solrDocument.get("item_sell_point").toString());

            //取高亮
            List<String> highlightList = highlighting.get(solrDocument.get("id")).get("item_title");
            String highlightStr = "";
            if(highlightList != null && highlightList.size() > 0){
                highlightStr = highlightList.get(0);
            }else{
                highlightStr = solrDocument.get("item_title").toString();
            }

            item.setTitle(highlightStr);
            searchItemList.add(item);
        }

        // 设置SearchResult属性
        result.setItemList(searchItemList);

        return result;
    }


}
