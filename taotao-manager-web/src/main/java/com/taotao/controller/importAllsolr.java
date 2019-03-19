package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class importAllsolr {

    @Autowired
    private SearchService service;

    @RequestMapping("/index/importAllsolr")
    @ResponseBody
    public TaotaoResult importAll() throws Exception{

        return service.importAllSearchItem();
    }
}
