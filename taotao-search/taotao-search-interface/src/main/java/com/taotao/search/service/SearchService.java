package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {
    //从 MySQL 查询数据 导入所有的商品数据到索引库中
    TaotaoResult importAllSearchItem() throws Exception;

    /**
     * 根据搜索内容查询商品数据
     *
     * @param queryString 查询的主条件
     * @param page 查询的当前的页码
     * @param rows 每页显示的行数 这个在controller中写死
     * @return
     * @throws Exception
     */
    SearchResult search(String queryString, Integer page, Integer rows) throws Exception;
}
