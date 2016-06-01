package agents.contractnet;

import java.util.ArrayList;
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
	
	private ArrayList<Proposal> pendingProposals;
	
	private int numOfParticipants;
	
	private int numOfProposals;
	
	public Initiator()
	{
		pendingProposals = new ArrayList<Proposal>();
		numOfParticipants = 0;
		numOfProposals = 0;
	}
			
	public void callForProposal()
	{
		AIDS[] runningAgents = arrayOfAgents();
		
		Proposal proposal = new Proposal();
		proposal.setInitiator(aids);
		
		ACLMessage msg = new ACLMessage();
		msg.setPerformative(Performative.CALL_FOR_PROPOSAL);
		msg.setContentObject(proposal);
		
		sendToAllReceivers(runningAgents, msg);
		
	}
	
	public void addProposal(ACLMessage msg)
	{
		logger.logMessage("Got a proposal back from agent " + msg.getReceivers()[0].getName());
		
		Proposal participantProposal = (Proposal) msg.getContentObject();
		
		pendingProposals.add(participantProposal);
		
		numOfProposals++;
		
		//ako je broj vracenih proposala jednak ukupnom broju poslatih znaci da su se svi javili
		if(numOfProposals == numOfParticipants)
		{
			Proposal bestProposal = getBestProposal();
			
			logger.logMessage("Agent " + bestProposal.getParticipant().getName() + " had the best proposal  "
							+ bestProposal.getTimeEstimate());
			
			//posaljemo poruku nazad Participant sa najboljom ponudom
			ACLMessage msgToParticipant = new ACLMessage();
			msgToParticipant.setPerformative(Performative.ACCEPT_PROPOSAL);
			msgToParticipant.setReceivers(new AIDS[] { aids } );
			msgToParticipant.setSender(bestProposal.getParticipant());
			
			
			
			messageManager.post(msgToParticipant);
			
		}
	}
	
	private void taskDone(ACLMessage msg)
	{
		logger.logMessage("Agent " + msg.getReceivers()[0].getName() + "has done the task he was assinged");
		resetValues();
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
			addProposal(msg);
			break;
		case REFUSE:
			//refuse propsal
			break;
		case INFORM:
			taskDone(msg);
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
	
	//Returns a array of all running participants agents
	private AIDS[] arrayOfAgents()
	{
		HashMap<String, AIDS> agentsAids = (HashMap<String, AIDS>) agentManager.allAgents();
		
		ArrayList<AIDS> participants =  new ArrayList<AIDS>();
		
		//get just the participant agents
		for(AIDS a : agentsAids.values())
		{
			if(a.getType().getName().equals("Participant"))
			{
				participants.add(a);
			}
			
		}
		
		AIDS[] aidsArray =  new AIDS[participants.size()];
		
		return participants.toArray(aidsArray);
	}
	
	private void sendToAllReceivers(AIDS[] receivers, ACLMessage msg)
	{
		for(AIDS recvAids : receivers)
		{
			msg.setReceivers(new AIDS[] { aids } );
			msg.setSender(recvAids);
			
			System.out.println("SEND TO: " + recvAids.getName());
			
			messageManager.post(msg);
		}
		
		numOfParticipants = receivers.length;
		
		logger.logMessage("Agent " + aids.getName() + " sent a proposal to " 
				+ String.valueOf(numOfParticipants) + " Participant agents");
	}
	
	/**
	 *  Retturn a proposal with minimun timeEstimate
	 */
	private Proposal getBestProposal()
	{
		Proposal min = new Proposal();
		min.setTimeEstimate(100);
		
		for(Proposal p : pendingProposals)
		{
			if(p.getTimeEstimate() < min.getTimeEstimate())
			{
				min = p;
				min.setParticipant(p.getParticipant());
			}
		}
		
		return min;
	}
	
	private void resetValues()
	{
		pendingProposals.clear();;
		numOfParticipants = 0;
		numOfProposals = 0;
	}
}
