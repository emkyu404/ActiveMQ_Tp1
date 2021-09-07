package receiver;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyReceiver {

	public static void main(String[] args) {
		try{
			
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
			QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");
			
			Queue queue = (Queue) applicationContext.getBean("queue");
			Topic topic = (Topic) applicationContext.getBean("topic");
			
			// Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			Connection connection = connectionFactory.createConnection();
			
			// Open a session
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("myQueue");
			
			//Destination destination = session.createTopic("dTopic");
			
			// start the connection
			connection.start();
			
			// Create a receive
			MessageConsumer consumer = session.createConsumer(destination);
			
			// Receive the message
			consumer.setMessageListener((MessageListener) new MessageListener() {
				public void onMessage(Message message) {
					try {
						if(message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage) message;
							String text = textMessage.getText();
							System.out.println("Received : " + text);
						} else {
							System.out.println("Received : " + message);
						}
					}
					catch(JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
