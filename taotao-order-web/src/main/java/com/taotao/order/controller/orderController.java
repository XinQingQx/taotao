package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: taotao
 * @description: 订单处理controller
 * @author: 钟兴青
 * @create: 2018-11-09 10:41
 **/
@Controller
public class orderController {

    @Autowired
    private UserLoginService loginService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Value("${TT_TOKEN}")
    private String TT_TOKEN;

    /**
     * 展示购物车
     * 参数：没有参数，但需要用户的id  从cookie中获取token 调用SSO的服务获取用户的ID
     * 返回值：逻辑视图 （订单的确认页面）
     * @param request
     * @return
     */
    @RequestMapping("/order/order-cart")//  /order/order-cart
    public String showOrder(HttpServletRequest request){

//        //从cookie中获取用户的token
//		String token = CookieUtils.getCookieValue(request, TT_TOKEN);
//		if(StringUtils.isNotBlank(token)){
//			//调用SSO的服务获取用户的信息
//			TaotaoResult result = loginService.getUserByToken(token);
//			if(result.getStatus()==200){
//				//必须是用户登录了才展示
//				//展示用户的送货地址   根据用户的ID查询该用户的配送地址。静态数据
//				//展示支付方式    从数据库中获取支付的方式。静态数据
//				//调用购物车服务从redis数据库中获取购物车的商品的列表
//				TbUser user = (TbUser)result.getData();
//				List<TbItem> cartList = cartService.getCartList(user.getId());
//				//将列表 展示到页面中(传递到页面中通过model)
//				request.setAttribute("cartList", cartList);
//			}
//		}

        TbUser user = (TbUser)request.getAttribute("USER_INFO");//获取用户登陆信息
        List<TbItem> cartList = cartService.getCartList(user.getId());
		//将列表 展示到页面中(传递到页面中通过model)
		request.setAttribute("cartList", cartList);

        return "order-cart";
    }

    /**
     * 订单提交 订单生成
     * url:/order/create
     * 参数：表单使用orderinfo来接收
     *返回值：逻辑视图 （订单提交成功页面）
     * @param info
     * @return
     */
    @RequestMapping(value="/order/create",method= RequestMethod.POST)
    public String createOrder(OrderInfo info,HttpServletRequest request){

        TbUser user = (TbUser)request.getAttribute("USER_INFO");//获取用户登陆信息
        info.setUserId(user.getId());
        info.setBuyerNick(user.getUsername());
        TaotaoResult result = orderService.createOrder(info);
        System.out.println(result.getData());
        request.setAttribute("orderId",result.getData());

        request.setAttribute("payment", info.getPayment());

        DateTime dateTime = new DateTime();
        DateTime plusDays = dateTime.plusDays(3);//加3天
        request.setAttribute("date", plusDays.toString("yyyy-MM-dd"));

        return "success";
    }
}
