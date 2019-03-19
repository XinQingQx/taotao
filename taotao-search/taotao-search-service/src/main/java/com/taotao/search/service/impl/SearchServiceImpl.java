package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchItemMapper mapper;

    //solr单机版
//    @Autowired
//    private HttpSolrServer solrServer;

    //集群版
    @Autowired
    private CloudSolrServer solrServer;

    @Autowired
    private SearchDao searchDao;

    /**
     * 生成索引库
     * @return
     * @throws Exception
     */
    @Override
    public TaotaoResult importAllSearchItem() throws Exception{
        //1.注入mapper
        //2.调用mapper的方法   查询所有的商品的数据
        List<SearchItem> searchItems = mapper.getSearchItemList();

        //将solrj写入到索引库中
            //1.创建 HttpSolrServer
            //2.创建 SolrInputDocument
        for (SearchItem searchItem : searchItems) {
            SolrInputDocument document = new SolrInputDocument();

            /**
             * name的属性数据类型 要和 value一致
             * Solr服务中  配置业务域 的数据类型
             */
            document.addField("id", searchItem.getId().toString());//id 是字符串需要转换

            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price",searchItem.getPrice() );
            document.addField("item_image",searchItem.getImage() );
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());

            solrServer.add(document);
        }
        solrServer.commit();
        return TaotaoResult.ok();
    }

    /**
     * 根据搜索内容查询商品数据
     *
     * @param queryString 查询的主条件
     * @param page        查询的当前的页码
     * @param rows        每页显示的行数 这个在controller中写死
     * @return
     * @throws Exception
     */
    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {

        //1.创建solrquery对象 用于设置条件 相似于 xxxExample 也是设置条件
        SolrQuery query = new SolrQuery();
        //2.设置主查询条件
        if(StringUtils.isNotBlank(queryString)){
            query.setQuery(queryString);
        }else{
            query.setQuery("*:*");
        }
        //2.1设置过滤条件 设置分页
        if(page==null)page=1;
        if(rows==null)rows=60;

        query.setStart((page-1)*rows);//page-1 * rows
        query.setRows(rows);

        //2.2.设置默认的搜索域,即默认搜索条件
        query.set("df", "item_keywords");

        //2.3设置高亮,  dao层已取高亮
        query.setHighlight(true);//打开高亮
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域

        //3.调用dao的方法 返回的是SearchResult 只包含了总记录数和商品的列表
        SearchResult search = searchDao.search(query);
        //4.设置SearchResult 的总页数
        long pageCount = 0l;
        pageCount = search.getRecordCount()/rows;
        if(search.getRecordCount()%rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        //5.返回
        return search;

    }
}
