package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: taotao
 * @description: 登陆 注册页面显示
 * @author: 钟兴青
 * @create: 2018-11-04 10:03
 **/
@Controller
public class PageController {

    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable String page, String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return page;
    }


}
