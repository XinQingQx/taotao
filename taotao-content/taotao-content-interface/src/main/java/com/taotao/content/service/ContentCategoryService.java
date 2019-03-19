package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * 内容分类接口
 */
public interface ContentCategoryService {

	//通过节点的id查询该节点的哦子节点列表
    List<EasyUITreeNode> getContentCategoryList(Long parentId);

	//添加内容分类

	/**
	 *
	 * @param parentId 父节点的id
	 * @param name 新增节点的名称
	 * @return
	 */
    TaotaoResult creatContentCategory(Long parentId, String name);

	/**
	 * 更新节点名称
	 * @param id 选中的节点id
	 * @param name 修改的名称
	 */
    TaotaoResult updateContentCategoryListName(Long id, String name);

	/**
	 * 删除节点
	 * @param id 删除节点的id
	 * @return
	 */
    void deleteContentCategoryListName(Long id);

}
