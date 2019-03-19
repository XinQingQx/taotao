package com.itheima.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQTest {
	@Test
	public void test() throws Exception{
//		第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
		ConnectionFactory factory =new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
//		第二步：使用ConnectionFactory对象创建一个Connection对象。
		Connection connection = factory.createConnection();
//		第三步：开启连接，调用Connection对象的start方法。
		connection.start();
//		第四步：使用Connection对象创建一个Session对象。
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//		第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
		Queue query = session.createQueue("test-2");
//		第六步：使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(query);
//		第七步：创建一个Message对象，创建一个TextMessage对象。
		TextMessage message = session.createTextMessage("hello ActiveMQ2222222");
//		第八步：使用Producer对象发送消息。
		producer.send(message);
//		第九步：关闭资源。
		session.close();
		producer.close();
		connection.close();
	}
}
