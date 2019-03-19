package com.itheima.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 *
 * queue 是点对点模式，只能是一个生产者产生一个消息，被一个消费者消费。
 * topic 是发布订阅模式，一个生产者可以一个消息，可以被多个消费者消费。
 *
 * queue 默认是存在于MQ的服务器中的，发送消息之后，消费者随时取。但是一定是一个消费者取，消费完消息也就没有了。
 * topic 默认是不存在于MQ服务器中的，一旦发送之后，如果没有订阅，消息则丢失。
 */
public class QueueProducer {
	//生产者发送消息
	@Test
	public void send() throws Exception{
		//1.创建连接的工厂 指定MQ服务器的地址
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//2.获取连接  通过工厂获取连接对象
		Connection connection = connectionFactory.createConnection();		
		//3.开启连接
		connection.start();		
		//4.根据连接对象创建session (提供了操作activmq的方法)
		//第一个参数：表示是否开启分布式事务（JTA）  一般就是false :表示不开启。  只有设置了false ,第二个参数才有意义。
		//第二个参数：表示设置应答模式  自动应答和手动应答  。使用的是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.根据session创建目的地（destination）
		Queue queue = session.createQueue("queue-test");
		//6.创建生产者
		MessageProducer producer = session.createProducer(queue);	
		//7.构建消息对象
		TextMessage message = session.createTextMessage("queue发送消息123")	;
		//8.通过生产者发送消息
		producer.send(message);	
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
}
