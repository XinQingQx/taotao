package com.taotao.test.jedis;

import com.taotao.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisClient {

    @Test
    public void testClient(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");

        JedisClient client = context.getBean(JedisClient.class);

        client.set("JedisClientCluster", "JedisClientCluster-value");
        System.out.println(client.get("JedisClientCluster"));
    }
}
