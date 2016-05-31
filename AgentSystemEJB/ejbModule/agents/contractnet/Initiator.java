package agents.contractnet;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import model.ACLMessage;
import model.AIDS;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class Initiator extends SirAgent
{
	
	@Override
	public void handleMessage(ACLMessage msg)
	{
		switch(msg.getPerformative())
		{
		case REQUEST:
			// create cfp
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
}
