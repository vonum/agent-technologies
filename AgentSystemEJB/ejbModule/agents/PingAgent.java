package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import model.ACLMessage;
import model.AIDS;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class PingAgent extends SirAgent
{

	public void init(AIDS aids)
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void handleMessage(ACLMessage msg)
	{
		System.out.println("Ping got hit son!");
		System.out.println(msg.getConversationId());
	}
	
}
