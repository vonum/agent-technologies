package agents.pingpong;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.MessageLoggerRemote;
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
		
		ACLMessage reply = new ACLMessage();
		Map<String, Object> userArgs = new HashMap<String, Object>();
		
		userArgs.put("Pong activated : ", aids.getName());
		userArgs.put("Pong activated by : ", msg.getReceivers()[0].getName());
		//userArgs.put("Pong activated on : ", msg.getReceivers()[0].getHost().getAddress());
		//userArgs.put("Pong working on : ", center.getCurNode().getAddress());
		userArgs.put("Pong counter : ", ++counter);
		
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
