package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.sso.service.UserLoginService;

@Controller
public class UserLoginController {
	@Autowired
	private UserLoginService loginservice;
	
	@Value("${TT_TOKEN}")
	private String TT_TOKEN;

	/**
	 * url:/user/login
	 * 参数：username password
	 * 返回值：json
	 * 请求限定的方法：post
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(HttpServletRequest request,HttpServletResponse response,String username,String password){

		TaotaoResult result = loginservice.login(username, password);
		//4.需要设置token到cookie中 可以使用 工具类  cookie需要跨域
		if(result.getStatus()==200){
			CookieUtils.setCookie(request, response,TT_TOKEN, result.getData().toString());
		}
		return result;
	}

	/**
	 * url:/user/token/{token}
	 * 参数：token
	 * 返回值：json
	 * 请求限定的方法：get
	 */
	@RequestMapping(value = "/user/token/{token}",method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult token(@PathVariable String token){

		TaotaoResult result = loginservice.getUserByToken(token);
		return result;

	}

}
