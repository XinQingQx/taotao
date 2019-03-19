package com.itheima.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class ActiveMQProducer {

    @Test
	public void test() throws Exception{
		//创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//获取一个连接
		Connection connection = factory.createConnection();
		//创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建发布对象
		Topic topic = session.createTopic("Topic--1");
		//创建发布内容
		MessageProducer producer = session.createProducer(topic);
		//构建发布消息
		TextMessage message = new ActiveMQTextMessage();
		message.setText("发布消息123");
		producer.send(message);
		connection.close();
		session.close();
		producer.close();
	}
}
