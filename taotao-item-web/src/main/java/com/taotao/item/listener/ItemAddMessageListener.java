package com.taotao.item.listener;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            TextMessage message1 = (TextMessage) message;
            try {
                //获取商品id

                long itemid = Long.parseLong(message1.getText());
                //2.从数据库中获取数据 可以调用manager中的服务   获取到了数据集
                TbItem item = itemService.getItemById(itemid);
                Item item1 = new Item(item);

                TbItemDesc tbItemDesc = itemService.getItemDescById(itemid);

                genHtmlFreemarker(item1,tbItemDesc);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private void genHtmlFreemarker(Item item1, TbItemDesc tbItemDesc) {
        //1.获取configuration对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        //2.创建模板 获取模板文件对象
        try {
            Template template = configuration.getTemplate("item.ftl");

            //3.创建数据集---准备模板需要的数据
            Map model = new HashMap<>();
            model.put("item", item1);
            model.put("itemDesc", tbItemDesc);
            //4.输出
            //D:\workspace
            Writer writer = new FileWriter(new File("D:\\workspace\\freemarker\\item"+"\\"+item1.getId()+".html"));
            template.process(model, writer);
            //5.关闭流
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
