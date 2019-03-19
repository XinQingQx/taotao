package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper mapper;

	@Autowired
	private TbItemDescMapper descmapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource(name = "itemAddTopic")
	private Destination destination;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;

	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//1.设置分页的信息 使用pagehelper
		if(page==null)page=1;
		if(rows==null)rows=30;
		PageHelper.startPage(page, rows);
		//2.注入mapper
		//3.创建example 对象 不需要设置查询条件
		TbItemExample example = new TbItemExample();
		//4.根据mapper调用查询所有数据的方法
		//PageHelper只对紧跟着的第一个SQL语句起作用
		List<TbItem> list = mapper.selectByExample(example);
		//5.获取分页的信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		//6.封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int)info.getTotal());
		result.setRows(info.getList());
		//7.返回
		return result;
	}

	/**
	 * 新增商品
	 *
	 * @param item
	 * @return
	 */
	@Override
	public TaotaoResult saveItem(TbItem item,String desc) {
		//生成商品的id
		final long itemId = IDUtils.genItemId();
		//1.补全item 的其他属性
		item.setId(itemId);
		item.setCreated(new Date());
		//1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		item.setUpdated(item.getCreated());
		//2.插入到item表 商品的基本信息表
		mapper.insertSelective(item);

		//3.补全商品描述中的属性
		TbItemDesc desc2 = new TbItemDesc();
		desc2.setItemDesc(desc);
		desc2.setItemId(itemId);
		desc2.setCreated(item.getCreated());
		desc2.setUpdated(item.getCreated());
		//4.插入商品描述数据
		//注入tbitemdesc的mapper
		descmapper.insertSelective(desc2);

		//发送activemq消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId+"");
			}
		});


		//5.返回taotaoresult
		return TaotaoResult.ok();
	}

	/**
	 * 根据商品id 查询商品信息
	 *
	 * @param itemid
	 * @return
	 */
	@Override
	public TbItem getItemById(Long itemid) {

		// 1.从缓存中获取数据，如果有直接返回
		try {
			String s = jedisClient.get(ITEM_INFO + ":" + itemid + ":BASE");
			if (StringUtils.isNotBlank(s)){
				// 重新设置商品的有效期
				jedisClient.expire(ITEM_INFO + ":" + itemid + ":BASE", ITEM_EXPIRE);
				System.out.println("商品详情有缓存");
				return JsonUtils.jsonToPojo(s,TbItem.class );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}



		TbItem tbItem = mapper.selectByPrimaryKey(itemid);



		// 3.添加缓存到redis数据库中
		// 注入jedisclient
		// BASE:基本信息
		// DESC:商品描述
		try {
			jedisClient.set(ITEM_INFO + ":" + itemid + ":BASE", JsonUtils.objectToJson(tbItem));
			System.out.println("没有商品详情缓存");
			// 设置缓存的有效期
			jedisClient.expire(ITEM_INFO + ":" + itemid + ":BASE", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	/**
	 * 根据商品id 查询商品描述
	 *
	 * @param itemid
	 * @return
	 */
	@Override
	public TbItemDesc getItemDescById(Long itemid) {
		// 1.从缓存中获取数据，如果有直接返回
		try {
			String s = jedisClient.get(ITEM_INFO + ":" + itemid + ":DESC");
			if (StringUtils.isNotBlank(s)){
				// 重新设置商品的有效期
				jedisClient.expire(ITEM_INFO + ":" + itemid + ":DESC", ITEM_EXPIRE);
				System.out.println("商品描述有缓存");
				return JsonUtils.jsonToPojo(s,TbItemDesc.class );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemDesc tbItemDesc = descmapper.selectByPrimaryKey(itemid);

		// 3.添加缓存到redis数据库中
		// 注入jedisclient
		// BASE:基本信息
		// DESC:商品描述
		try {
			jedisClient.set(ITEM_INFO + ":" + itemid + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			System.out.println("没有商品描述缓存");
			// 设置缓存的有效期
			jedisClient.expire(ITEM_INFO + ":" + itemid + ":DESC", ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tbItemDesc;
	}


}
