package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * 内容处理接口
 */
public interface ContentService {

    /**
     * 新增内容管理表一条记录
     * @param content
     * @return
     */
    TaotaoResult saveContent(TbContent content);

    /**
     * 显示内容管理内容
     * categoryId=89&page=1&rows=20
     * @return
     */
    EasyUIDataGridResult getItemList(Long categoryId, Integer page, Integer rows);

    /**
     * 删除内容管理表一条记录
     * @param ids
     * @return
     */
    TaotaoResult deleteContent(Long ids);

    /**
     * 根据内容分类的ID 查询其下的内容列表
     * @param categoryId
     * @return
     */
    List<TbContent> getContentListByCatId(Long categoryId);
}
