package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品相关的处理的service
 * @title ItemService.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface ItemService {
	
	/**
	 *  根据当前的页码和每页 的行数进行分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
    EasyUIDataGridResult getItemList(Integer page, Integer rows);

	/**
	 * 新增商品
	 * @return
	 */
    TaotaoResult saveItem(TbItem item, String desc);

	/**
	 * 根据商品id 查询商品信息
	 * @param itemid
	 * @return
	 */
    TbItem getItemById(Long itemid);

	/**
	 * 根据商品id 查询商品描述
	 * @param itemid
	 * @return
	 */
    TbItemDesc getItemDescById(Long itemid);
}
