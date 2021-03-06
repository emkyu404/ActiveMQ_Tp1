package sender;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MySender {

	public static void main(String[] args) {
		
		try{
			
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
			QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");
			
			Queue queue = (Queue) applicationContext.getBean("queue");
			Topic topic = (Topic) applicationContext.getBean("topic");
 
			// Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			Connection connection = connectionFactory.createConnection();

			
			// Open a session without transaction and acknowledge automatic
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//Destination destination = session.createQueue("myQueue");
			Destination destination = session.createTopic("dTopic");
			
			// Start the connection
			connection.start();
			// Create a sender
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			//Test du time to live
			//producer.setTimeToLive(10000);
			
			// Create a message
			String text = "Message 1 ! From : Sender 1";
			TextMessage message = session.createTextMessage(text);
			
			// Send the message
			producer.send(message);
		
			/* envoie de plusieurs message */
			//text = "Message 2 ! From : Sender 1";
			//message = session.createTextMessage(text);
			
			// Send the message
			//producer.send(message);
			
			//text = "Message 3 ! From : Sender 1";
			//message = session.createTextMessage(text);
			
			// Send the message
			//producer.send(message);
			
			// Transaction mode = true
			//session.commit();

			
			// Close the session
			session.close();
			
			// Close the connection
			connection.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
