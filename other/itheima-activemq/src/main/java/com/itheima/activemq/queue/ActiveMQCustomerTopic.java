package com.itheima.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQCustomerTopic {
	@Test
	public void test() throws Exception{
		//创建ActiveMQ工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//获取连接
		Connection connection = factory.createConnection();
		//开启连接
		connection.start();
		//获取session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建接收端
		Topic topic = session.createTopic("Topic--1");
		//创建消费端
		MessageConsumer consumer = session.createConsumer(topic);
		//获取消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message msg) {
				if (msg instanceof TextMessage) {
					TextMessage ms = (TextMessage)msg;
					try {
						System.out.println("接收到消息"+ms.getText());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		Thread.sleep(10000);
		connection.close();
		session.close();
		consumer.close();
	}
}
