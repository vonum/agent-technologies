package agents.contractnet;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.MessageLoggerRemote;
import model.ACLMessage;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class Participant extends SirAgent
{

	@EJB
	MessageLoggerRemote logger;
	
	public void respondToProposal()
	{
		logger.logMessage("Got a request for a proposal from the initiator");
	}
	
	@Override
	public void handleMessage(ACLMessage msg)
	{
		switch(msg.getPerformative())
		{
			case CALL_FOR_PROPOSAL:
				respondToProposal();
				break;
			case REJECT_PROPOSAL:
				// handleRejectProposal();
				break;
			case ACCEPT_PROPOSAL:
				//acceptProposal
				break;
			default:
				break;
		}
		
			
	}
}
