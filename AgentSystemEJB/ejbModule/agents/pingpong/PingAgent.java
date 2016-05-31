package agents.pingpong;

import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.MessageLoggerRemote;
import interfaces.MessageManagerRemote;
import model.ACLMessage;
import model.AIDS;
import model.AgentType;
import model.Performative;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class PingAgent extends SirAgent
{

	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	MessageLoggerRemote logger;
	
	public void init(AIDS aids)
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void handleMessage(ACLMessage msg)
	{
		
		switch(msg.getPerformative())
		{
			case REQUEST:
			{				
				System.out.println("Handling message for ping agent " + msg.getSender().getName());
				System.out.println("Content : " + msg.getContent());
				System.out.println("Performative : " + msg.getPerformative());
				
				logger.logMessage("Ping " + aids.getName() + " activated");
				logger.logMessage("Sending message to " + msg.getReceivers()[0].getName() + ", on node " + msg.getReceivers()[0].getHost().getAddress());
				
				
				//Reciever type
				AgentType recieverType = new AgentType(PongAgent.class.getSimpleName(), "w/e");
				
				//Reciever ID
				AIDS recieverAIDS = new AIDS();
				recieverAIDS.setName(msg.getReceivers()[0].getName());
				recieverAIDS.setType(recieverType);
				recieverAIDS.setHost(msg.getReceivers()[0].getHost());
				
				//message to post
				ACLMessage message = new ACLMessage();
				message.setPerformative(Performative.REQUEST);
				message.setSender(recieverAIDS);  //onaj ko treba da obradi poruku
				message.setReceivers(new AIDS[] { aids } );	//oni kojima treba da vrati poruku
				
				//post message
				messageManager.post(message);
			}
			break;
			
			case INFORM:
			{
				System.out.println("Handling message for ping agent " + msg.getSender().getName());
				System.out.println("Content : " + msg.getContent());
				System.out.println("Performative : " + msg.getPerformative());
				
				for(Entry<String, Object> e : msg.getUserArgs().entrySet())
				{
					logger.logMessage(e.getKey() + e.getValue());
				}
				
				logger.logMessage("Ping reactivated on : " + aids.getHost().getAddress());
				logger.logMessage("Ping reactivated : " + aids.getName());
				
				//System.out.println(msg.getUserArgs().size());
			}
			break;
			
			default : 
			{
				System.out.println("UNSUPPORTED PERFORMATIVE");
			}
		}
		
	}
	
}
