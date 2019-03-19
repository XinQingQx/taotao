package com.taotao.item.controller;


import com.taotao.common.pojo.TaotaoResult;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private ItemService service;

    @RequestMapping("/item/{itemid}")
    public String showItem(@PathVariable Long itemid, Model model){

        TbItem tbItem = service.getItemById(itemid);

        //tbitem 转成 item
        Item item= new Item(tbItem);

        TbItemDesc desc = service.getItemDescById(itemid);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", desc);


        return "item";
    }
}
