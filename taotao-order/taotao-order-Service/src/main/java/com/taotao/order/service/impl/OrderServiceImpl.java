package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: taotao
 * @description: 生成订单
 * @author: 钟兴青
 * @create: 2018-11-10 13:46
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient client;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper itemMapper;

    @Autowired
    private TbOrderShippingMapper shippingMapper;

    @Value("${ORDER_ID_INIT}")//订单id 初始值
    private String ORDER_ID_INIT;

    @Value("${ORDER_ID_KEY}")//生成订单号的key
    private String ORDER_ID_KEY;

    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;

    /**
     * 创建订单
     *
     * @param orderInfo
     * @return
     */
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //1.插入订单表
        //通过redis的incr 生成订单id
        //判断如果key没存在 需要初始化一个key设置一个 初始值--ORDER_ID_INIT

        if (!client.exists(ORDER_ID_KEY)){
            client.set(ORDER_ID_KEY, ORDER_ID_INIT);
        }
        String orderid = client.incr(ORDER_ID_KEY).toString();
        System.out.println(orderid+"-------------------------");
        //补全其他的属性
        //info.setBuyerNick(buyerNick);  在controller设置
        orderInfo.setOrderId(orderid);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setPostFee("0");
        //info.setUserId(userId);由controller设置
        orderInfo.setUpdateTime(orderInfo.getCreateTime());

        //注入mapper
        orderMapper.insert(orderInfo);

        //2.插入订单项 明细表
            //补全其他的属性
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            //设置订单项的id 通过redis的incr 生成订单项的id
            String itemId = client.incr(ORDER_ITEM_ID_KEY).toString();
            orderItem.setOrderId(orderid);
            orderItem.setId(itemId);
            //插入订单项表
            itemMapper.insert(orderItem);
        }


        //3.插入订单物流表
        //设置订单id
        TbOrderShipping shipping = orderInfo.getOrderShipping();

        //补全其他的属性
        shipping.setOrderId(orderid);
        shipping.setCreated(orderInfo.getCreateTime());
        shipping.setUpdated(orderInfo.getCreateTime());

        //chauru
        shippingMapper.insert(shipping);
        //返回需要包含订单的ID

        return TaotaoResult.ok(orderid);
    }
}
