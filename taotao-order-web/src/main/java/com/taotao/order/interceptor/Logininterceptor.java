package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: taotao
 * @description: 用户身份认证的拦截器
 * @author: 钟兴青
 * @create: 2018-11-09 12:16
 **/
public class Logininterceptor implements HandlerInterceptor {

    @Autowired
    private UserLoginService loginService;
    @Value("${TT_TOKEN}")
    private String TT_TOKEN;

    @Value("${SSO_LOCALHOST}")
    private String SSO_LOCALHOST;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //从cookie中获取用户的token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN);

        if (StringUtils.isEmpty(token)){
            //如果不存在，说明没有登陆  -->跳转至登陆页面
            // 注意  getRequestURL() 返回全路径名 ; getRequestURI() 去除localhost
            response.sendRedirect(SSO_LOCALHOST+"/page/login?redirect="+request.getRequestURL().toString());
            //后面就不用执行了
            return false;
        }
        TaotaoResult result = loginService.getUserByToken(token);
        if (result.getStatus()!=200){
            //用户过期   -->跳转至登陆页面
            response.sendRedirect(SSO_LOCALHOST+"/page/login?redirect="+request.getRequestURL().toString());
            //后面就不用执行了
            return false;
        }

        //没有过期 ，用户登陆
        //设置用户信息
        request.setAttribute("USER_INFO",result.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
