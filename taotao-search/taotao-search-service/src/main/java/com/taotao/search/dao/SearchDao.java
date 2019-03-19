package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 从索引库中搜索商品的dao
 */
@Repository
public class SearchDao {

    //spring 管理 spring 注入
    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query) throws Exception{

        SearchResult searchResult = new SearchResult();

        //1.创建 SolrServer对象，由spring注入
        //2.直接执行查询
        QueryResponse response = solrServer.query(query);

        //3.获取查询的结果集
        SolrDocumentList results = response.getResults();

        //设置searchresult的总记录数
        searchResult.setRecordCount(results.getNumFound());//获取总记录数

        //4.遍历结果集
        //定义一个集合
        List<SearchItem>  searchItems = new ArrayList<>();

        //取高亮
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (SolrDocument result : results) {
            //将solrdocument中的属性 一个个的设置到 searchItem中
            SearchItem item = new SearchItem();

            item.setId(Long.parseLong(result.get("id").toString()));
            item.setCategory_name(result.get("item_category_name").toString());
            item.setImage(result.get("item_image").toString());
            //item.setItem_desc(result.get("item_desc").toString()); 描述没有提交的索引库中，查询不到，所以不用设置
            item.setPrice(Long.parseLong(result.get("item_price").toString()));
            item.setSell_point(result.get("item_sell_point").toString());

//          item.setTitle(result.get("item_title").toString());
            //取高亮
            List<String> list = highlighting.get(result.get("id")).get("item_title");
            //判断list是否为空
            String gaoliangstr = "";
            if(list!=null && list.size()>0){
                //有高亮
                gaoliangstr=list.get(0);
            }else{
                gaoliangstr = result.get("item_title").toString();
            }
            item.setTitle(gaoliangstr);


            //searchItem  封装到SearchResult中的itemList 属性中
            searchItems.add(item);
        }
        //5.设置SearchResult 的属性
        searchResult.setItemList(searchItems);
        return searchResult;
    }
}
