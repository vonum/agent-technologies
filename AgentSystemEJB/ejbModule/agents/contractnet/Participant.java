package agents.contractnet;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import model.ACLMessage;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class Participant extends SirAgent
{

	@Override
	public void handleMessage(ACLMessage msg)
	{
		switch(msg.getPerformative())
		{
			case CALL_FOR_PROPOSAL:
				//cfp
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
