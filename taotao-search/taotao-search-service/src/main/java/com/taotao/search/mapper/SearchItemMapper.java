package com.taotao.search.mapper;


import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 定义Mapper 关联查询3张表 查询出搜索时的商品数据
 */
public interface SearchItemMapper {
    //查询所有的商品的数据
    List<SearchItem> getSearchItemList();

    //根据商品的id查询索引库中商品的数据
    SearchItem getSearchItemById(Long itemId);
}
