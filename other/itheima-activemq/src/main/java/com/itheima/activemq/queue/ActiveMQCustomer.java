package com.itheima.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQCustomer {
	@Test
	public void test2() throws Exception{
//		第一步：创建一个ConnectionFactory对象。
		ConnectionFactory factory= new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
//		第二步：从ConnectionFactory对象中获得一个Connection对象。
		Connection connection = factory.createConnection();
//		第三步：开启连接。调用Connection对象的start方法。
		connection.start();
//		第四步：使用Connection对象创建一个Session对象。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
		Queue queue = session.createQueue("test-2");
//		第六步：使用Session对象创建一个Consumer对象。
		MessageConsumer consumer = session.createConsumer(queue);
//		第七步：接收消息。
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message msg) {
				if (msg instanceof TextMessage) {
					TextMessage ms=(TextMessage)msg;
					try {
						System.out.println("接收到消息"+ms.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
//		第八步：打印消息。
		Thread.sleep(10000);
//		第九步：关闭资源
		connection.close();
		session.close();
		consumer.close();
	}
}
