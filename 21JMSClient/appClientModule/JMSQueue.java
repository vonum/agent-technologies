import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * 
 * @author minja
 *
 */

public class JMSQueue implements MessageListener {
    QueueReceiver receiver;

	public JMSQueue() {
		try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context
					.lookup("jms/queue/mojQueue");
			context.close();
			Connection connection = cf.createConnection("guest", "guestguest");
			final Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			connection.start();

			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(this);

		    TextMessage msg = session.createTextMessage("shoneAgent");
		 
		    
			MessageProducer producer = session.createProducer(queue);
			producer.send(msg);
			Thread.sleep(1000);
			System.out.println("Message published. Please check application server's console to see the response from MDB.");

			producer.close();
			consumer.close();
			connection.stop();
		    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new JMSQueue();
	}

	public void onMessage(Message msg) {
	    try {
	        TextMessage tmsg = (TextMessage) msg;
	        try {
	            String text = tmsg.getText();
	            long time = tmsg.getLongProperty("sent");
	            System.out.println("Received new message from Queue : " + text + ", with timestamp: " + time);
	        } catch (JMSException e) {
	            e.printStackTrace();
	        }
	     } catch (Exception e) {
	        e.printStackTrace ();
	     }
	}
}









