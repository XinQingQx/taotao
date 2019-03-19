package com.taotao.search.test;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrCloudTest {

    @Test
    public void test1() throws IOException, SolrServerException {
        //1.创建solrserver 建立连接
        // 指定zookeeper集群的节点列表字符串 需要指定地址
        CloudSolrServer server = new CloudSolrServer("192.168.25.133:2182,192.168.25.133:2183,192.168.25.133:2184");

        //设置默认的搜索的collection 默认的索引库（不是core所对应的，是指整个collection索引集合）
        server.setDefaultCollection("collection2");

        //设置SolrInputDocument对象
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id","testCloudId" );
        document.addField("item_title","鸟语花香" );

        //将文档提交到索引库中
        server.add(document);

        //提交
        server.commit();
    }
}
