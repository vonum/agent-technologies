package agents;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.NodeRemote;
import model.ACLMessage;
import model.AIDS;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class PongAgent extends SirAgent
{
	//java:app[/module name]/enterprise bean name[/interface name]
	private int counter;
	
	public PongAgent()
	{
		counter = 0;
	}
	
	@EJB
	NodeRemote center;
	
	public void init(AIDS aids)
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void handleMessage(ACLMessage msg)
	{
		System.out.println("PongAgent got hit son!");
		System.out.println(++counter);
		System.out.println(this.hashCode());
		counter = 5;
		//System.out.println(center.getCurNode().getAlias());
	}
}
