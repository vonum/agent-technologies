package agents;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
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
	
	public void init(AIDS aids)
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void handleMessage(ACLMessage msg)
	{
		System.out.println("Ping got hit son!");

		System.out.println("WUT");
		
		System.out.println(msg.getPerformative());
		
		switch(msg.getPerformative())
		{
			case REQUEST:
			{
				System.out.println("REQUEST");
				//Reciever type
				AgentType recieverType = new AgentType(PongAgent.class.getSimpleName(), "w/e");
				
				//Reciever ID
				AIDS recieverAIDS = new AIDS();
				recieverAIDS.setName(msg.getContent());
				recieverAIDS.setType(recieverType);
				
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
				System.out.println("INFORM");
				System.out.println(msg.getUserArgs().size());
			}
			break;
			
			default : 
			{
				System.out.println("UNSUPPORTED PERFORMATIVE");
			}
		}
		
	}
	
}
