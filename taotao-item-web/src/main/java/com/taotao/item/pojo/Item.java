package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class Item extends TbItem {

    /*public Item(TbItem tbItem){
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setId(tbItem.getId());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }*/
    public Item(TbItem item){
        BeanUtils.copyProperties(item, this);//将原来数据有的属性的值拷贝到item有的属性中
    }

    public String[] getImages(){
        if(StringUtils.isNotBlank(super.getImage())){
            return super.getImage().split(",");
        }
        return null;
    }
}
