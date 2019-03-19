package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * @program: taotao
 * @description:
 * @author: 钟兴青
 * @create: 2018-11-10 13:45
 **/
public interface OrderService {

    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    public TaotaoResult createOrder(OrderInfo orderInfo);
}
