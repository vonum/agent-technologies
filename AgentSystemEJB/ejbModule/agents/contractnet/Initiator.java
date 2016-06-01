package agents.contractnet;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.AgentManagerRemote;
import interfaces.MessageLoggerRemote;
import interfaces.MessageManagerRemote;
import model.ACLMessage;
import model.AIDS;
import model.Performative;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class Initiator extends SirAgent
{
	
	@EJB
	AgentManagerRemote agentManager;
	
	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	MessageLoggerRemote logger;
	
	public void callForProposal()
	{
		//why the fuck doesn't this work?
		//	AIDS[] runningAgents = (AIDS[]) agentManager.allAgents().values().toArray();
		
		
		AIDS[] runningAgents = arrayOfAgents();
		
		ACLMessage msg = new ACLMessage();
		msg.setPerformative(Performative.CALL_FOR_PROPOSAL);
		
		sendToAllReceivers(runningAgents, msg);
		
	}
	
	@Override
	public void handleMessage(ACLMessage msg)
	{
		switch(msg.getPerformative())
		{
		case REQUEST:
			callForProposal();
			break;
		case PROPOSE:
			// proposal
			break;
		case REFUSE:
			//refuse propsal
			break;
		case INFORM:
			//we are done
			break;
		case FAILURE:
			//fail
			break;
		case CALL_FOR_PROPOSAL:
			break;
		default:
			break;
		}
	}
	
	public AIDS[] arrayOfAgents()
	{
		HashMap<String, AIDS> agentsAids = (HashMap<String, AIDS>) agentManager.allAgents();
		
		AIDS[] aidsArray =  new AIDS[agentsAids.values().size()];
		int i = 0;
		
		for(AIDS a : agentsAids.values())
		{
			aidsArray[i] = a;
			i++;
		}
		
		return aidsArray;
	}
	
	public void sendToAllReceivers(AIDS[] receivers, ACLMessage msg)
	{
		for(AIDS recvAids : receivers)
		{
			msg.setReceivers(new AIDS[] { aids } );
			msg.setSender(recvAids);
			
			System.out.println("SEND TO: " + recvAids.getName());
			
			messageManager.post(msg);
		}
		
		logger.logMessage("Send a request for proposal to all agents");
	}
}
