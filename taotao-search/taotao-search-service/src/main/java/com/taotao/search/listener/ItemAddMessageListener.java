package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private SearchItemMapper mapper;

    @Autowired
    private CloudSolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            try {
                //从消息中取出商品id
                TextMessage textMessage = (TextMessage)message;
                String text = textMessage.getText();
                long itemId = Long.valueOf(text);

                //根据商品id查询商品详情，这里需要注意的是消息发送方法
                //有可能还没有提交事务，因此这里是有可能取不到商品信息
                //的，为了避免这种情况出现，我们最好等待事务提交，这里
                //我采用3次尝试的方法，每尝试一次休眠一秒
                SearchItem searchItemById = null;
                for (int i=0;i<3;i++){
                    try {
                        Thread.sleep(1000);//休眠一秒
                        searchItemById = mapper.getSearchItemById(itemId);
                        //如果获取到了商品信息，那就退出循环
                        if (searchItemById != null){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", searchItemById.getId());
                document.setField("item_title", searchItemById.getTitle());
                document.setField("item_sell_point", searchItemById.getSell_point());
                document.setField("item_price", searchItemById.getPrice());
                document.setField("item_image", searchItemById.getImage());
                document.setField("item_category_name", searchItemById.getCategory_name());
                document.setField("item_desc", searchItemById.getItem_desc());

                //把文档写入索引库
                solrServer.add(document);
                //提交
                solrServer.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
