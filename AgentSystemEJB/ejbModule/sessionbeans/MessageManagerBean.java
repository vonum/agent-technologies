package sessionbeans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import interfaces.MessageManagerRemote;
import interfaces.NodeRemote;
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
	
	@EJB
	NodeRemote node;
	
    public MessageManagerBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public void post(ACLMessage aclMessage) {
		// TODO Auto-generated method stub
		
		System.out.println("POST value for performative" + aclMessage.getPerformative());
		
		if(aclMessage.getSender().getHost().getAlias().equals(node.getCurNode().getAlias()))
		{
			System.out.println("MsgManager : reciever on the same node, sending message through queue");
			try {
				//creating connection
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
	
				//creating consumer
				MessageConsumer consumer = session.createConsumer(queue);
				
				//setuping message
				ObjectMessage omsg = session.createObjectMessage();
				omsg.setObject(aclMessage);
			    
				//posting message
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
		else
		{
			System.out.println("MsgManager : reciever not on same queue, sending acl to other node");
			postToCenter(aclMessage);
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

	@Override
	public void postToCenter(ACLMessage aclMessage) {
		// TODO Auto-generated method stub
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target;
        
		target = client.target("http://" + aclMessage.getSender().getHost().getAddress() + ":8080/AgentSystemClient/rest/agents/messages");
	    target.request().post(Entity.entity(aclMessage, MediaType.APPLICATION_JSON));
	}

}
