package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * author : liuqi
 * createTime : 2018-09-16
 * description : TODO
 * version : 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private HttpSolrServer solrServer;

    @Autowired
    private SearchDao searchDao;



    @Override
    public TaotaoResult importAllSearchItems() throws Exception {
        // 查询所有的商品数据
        List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();

        for(SearchItem searchItem : searchItemList){
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId().toString());//这里是字符串需要转换
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            //添加到索引库
            solrServer.add(document);
        }
        //提交
        solrServer.commit();
        return TaotaoResult.ok();
    }

    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
        // 设置查询条件
        SolrQuery query = new SolrQuery();

        if(StringUtils.isNoneBlank(queryString)){
            query.setQuery(queryString);
        }else{
            query.setQuery("*.*");
        }

        // 设置过滤条件
        if(page == null) page = 1;
        if(rows == null) rows = 60;

        query.setStart((page -1)*rows);
        query.setRows(rows);

        // 设置默认的搜索域
        query.set("df","item_keywords");

        // 设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em");
        query.addHighlightField("item_title");

        // 调用dao方法返回SearchResult
        SearchResult result = searchDao.search(query);
        // 设置总页数
        long pageCount = 0l;
        pageCount = result.getRecordCount()/rows;

        if(result.getRecordCount() % rows > 0){
            pageCount ++ ;
        }
        result.setPageCount(pageCount);
        return result;
    }
}
