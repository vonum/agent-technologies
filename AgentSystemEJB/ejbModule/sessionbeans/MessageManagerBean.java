package sessionbeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import interfaces.MessageManagerRemote;
import model.ACLMessage;
import model.Performative;

/**
 * Session Bean implementation class MessageManagerBean
 */
@Stateless
@LocalBean
public class MessageManagerBean implements MessageManagerRemote {

    /**
     * Default constructor. 
     */
    public MessageManagerBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public void post(ACLMessage aclMessage) {
		// TODO Auto-generated method stub
		
		System.out.println("POST value for performative" + aclMessage.getPerformative());
		
		try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("java:jboss/exported/jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context
					.lookup("java:jboss/exported/jms/queue/mojQueue");
			context.close();
			Connection connection = cf.createConnection("guest", "guestguest");
			final Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			connection.start();

			MessageConsumer consumer = session.createConsumer(queue);

			//ACLMessage message = messageManager.setupMessage(name);
			
			ObjectMessage omsg = session.createObjectMessage();
			omsg.setObject(aclMessage);
		    
			MessageProducer producer = session.createProducer(queue);
			producer.send(omsg);

			//session.close();
			producer.close();
			consumer.close();
			connection.stop();
		    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public ACLMessage setupMessage(String content) {
		// TODO Auto-generated method stub
		ACLMessage message = new ACLMessage();
		
		message.setContent(content);
		message.setPerformative(Performative.REQUEST);
		
		return message;
	}

}
