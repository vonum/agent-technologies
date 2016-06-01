package agents.contractnet;

import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import interfaces.MessageLoggerRemote;
import interfaces.MessageManagerRemote;
import model.ACLMessage;
import model.AIDS;
import model.Performative;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class Participant extends SirAgent
{

	@EJB
	MessageLoggerRemote logger;
	
	@EJB
	MessageManagerRemote messageManager;
	
	public void respondToProposal(ACLMessage msg)
	{
		logger.logMessage("Agent " + aids.getName() + " got a request for a proposal from " 
						+ msg.getReceivers()[0].getName());
		
		long timeEstimate = getEstimate();
		
		Proposal proposalResponse = new Proposal();
		proposalResponse.setTimeEstimate(timeEstimate);
		proposalResponse.setParticipant(aids);
		
		ACLMessage proposalMsg = new ACLMessage();
		proposalMsg.setPerformative(Performative.PROPOSE);
		proposalMsg.setContentObject(proposalResponse);
		proposalMsg.setSender(msg.getReceivers()[0]);
		proposalMsg.setReceivers(new AIDS[] { aids } );
		
		messageManager.post(proposalMsg);
	}
	
	private void acceptProposal(ACLMessage msg)
	{
		ACLMessage informMsg =  new ACLMessage();
		informMsg.setPerformative(Performative.INFORM);
		informMsg.setSender(msg.getReceivers()[0]);
		informMsg.setReceivers(new AIDS[] { aids } );
		
		messageManager.post(informMsg);
	
	}
	
	@Override
	public void handleMessage(ACLMessage msg)
	{
		switch(msg.getPerformative())
		{
			case CALL_FOR_PROPOSAL:
				respondToProposal(msg);
				break;
			case REJECT_PROPOSAL:
				// handleRejectProposal();
				break;
			case ACCEPT_PROPOSAL:
				acceptProposal(msg);
				break;
			default:
				break;
		}
		
	}
	
	//returns a number between 1 and 100
	public long getEstimate()
	{
		Random r = new Random();
		
		return (long) (r.nextInt(100) + 1);
	}
}
