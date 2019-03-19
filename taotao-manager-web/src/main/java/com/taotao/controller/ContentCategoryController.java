package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类的处理controller
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService service;
    /**
     * url : '/content/category/list',
     * 		animate: true,
     * 		method : "GET",
     */
    @RequestMapping(value = "/content/category/list",method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0") Long parentId){

        return service.getContentCategoryList(parentId);
    }

    //url:/content/category/create
    //method=post
    /**
     * parentId:node.parentId 新增节点的父节点id
     * name:node.text 新增节点名称
     */

    /**
     * 添加节点
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult creatContentCategory(Long parentId,String name){
        return service.creatContentCategory(parentId, name);
    }



    //url:/content/category/update
    //id:node.id,name:node.text

    /**
     * 重命名节点
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContentCategoryListName(Long id,String name){
      return service.updateContentCategoryListName(id, name);
    }

    @RequestMapping(value = "/content/category/delete/",method = RequestMethod.POST)
    @ResponseBody
    public void deleteContentCategoryListName(Long id){
        service.deleteContentCategoryListName(id);
    }
}
