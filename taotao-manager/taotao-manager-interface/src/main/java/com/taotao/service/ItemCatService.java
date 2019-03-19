package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 规格参数 商品类目 商品分类的service的接口
 * @title ItemCatService.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface ItemCatService {
	/**
	 * 根据父节点的id查询子节点的列表
	 * @param parentId
	 * @return
	 */
    List<EasyUITreeNode> getItemCatListByParentId(Long parentId);
}
