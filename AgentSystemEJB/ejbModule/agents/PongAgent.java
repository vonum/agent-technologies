package agents;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.MessageManagerRemote;
import interfaces.NodeRemote;
import model.ACLMessage;
import model.AIDS;
import model.Performative;
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
		System.out.println("PongAgent got hit son!");
		System.out.println(++counter);
		System.out.println(this.hashCode());
		System.out.println(center.getCurNode().getAlias() != null);
		
		ACLMessage reply = new ACLMessage();
		Map<String, Object> userArgs = new HashMap();
		
		userArgs.put("pongCreatedOn", center.getCurNode().getAlias());
		userArgs.put("pongWorkingOn", center.getCurNode().getAlias());
		userArgs.put("pongCounter", ++counter);
		
		reply.setPerformative(Performative.INFORM);
		reply.setUserArgs(userArgs);
		reply.setSender(msg.getReceivers()[0]);
		
		messageManager.post(reply);
		/*reply.userArgs.put("pongCreatedOn", nodeName);
		reply.userArgs.put("pongWorkingOn", getNodeName());
		reply.userArgs.put("pongCounter", ++counter);
		msm().post(reply);*/
	}
}
