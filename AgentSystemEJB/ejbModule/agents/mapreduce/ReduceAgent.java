package agents.mapreduce;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import agents.AgentLoader;
import interfaces.Agent;
import interfaces.MessageLoggerRemote;
import interfaces.MessageManagerRemote;
import interfaces.NodeRemote;
import model.ACLMessage;
import model.AIDS;
import model.AgentType;
import model.Pair;
import model.SirAgent;

/**
 * Session Bean implementation class ReduceAgent
 */
@Stateful
@Remote(Agent.class)
public class ReduceAgent extends SirAgent{

	@EJB
	NodeRemote center;
	
	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	MessageLoggerRemote logger;
	
	public static int nameGenerator = 0;
	
    /**
     * Default constructor. 
     */
    public ReduceAgent() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init(AIDS aids)
	{
		
	}
	
    @Override
	public void stop()
	{
		
	}
    
    @Override
    public void handleMessage(ACLMessage msg)
    {
		switch(msg.getPerformative())
		{
		
			case REQUEST:
			{
		    	logger.logMessage("Reduce agent " + msg.getSender().getName() + " activated");
		    	logger.logMessage("Activated on : " + center.getCurNode().getAddress());
		    	logger.logMessage("Folder URL : " + msg.getContent());	
		    	
		    	AgentLoader loader = new AgentLoader();
		    	
		    	File folder = new File(msg.getContent());
		    	File[] listOfFiles = folder.listFiles();
		
		    	for (int i = 0; i < listOfFiles.length; i++) 
		    	{
		    		if (listOfFiles[i].isFile()) 
		    		{
		    			System.out.println("File " + listOfFiles[i].getName());
		    			Agent agent = loader.startAgent(new AgentType("MapAgent", "pls"), "");
		    			
		    			AIDS agentAIDS = new AIDS();
		    			agentAIDS.setName(aids.getName() + "Slave" + Integer.toString(nameGenerator++));
		    			agentAIDS.setType(new AgentType("MapAgent", "pls"));
		    			agentAIDS.setHost(center.getCurNode());
		    			
		    			agent.setAids(agentAIDS);
		    			ACLMessage message = new ACLMessage();
		    			message.setContent(msg.getContent() + "\\" + listOfFiles[i].getName());
		    			message.setSender(agentAIDS);
		    			message.setReceivers(new AIDS[] { aids });
		    			agent.handleMessage(message);
		    			
		    	    } else if (listOfFiles[i].isDirectory()) {
		    	    	System.out.println("Directory " + listOfFiles[i].getName());
		    	    	logger.logMessage("Directory " + listOfFiles[i].getName());
		    	    }
		    	}
			}
			break;
			
			case INFORM:
			{
				
				Pair winner = (Pair) msg.getUserArgs().get("Occurences");
				String url = (String) msg.getUserArgs().get("URL");
				String agent = (String) ((AIDS) msg.getUserArgs().get("Agent")).getName();
				
				logger.logMessage("MapAgent : " + agent);
				logger.logMessage("Most occurences for file URL : " + url);
				logger.logMessage("Character : " + winner.getKey());
				logger.logMessage("Occurences : " + winner.getValue());
			}
    	}
    }

}
