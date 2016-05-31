package agents.mapreduce;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import agents.AgentLoader;
import interfaces.Agent;
import interfaces.MessageManagerRemote;
import interfaces.NodeRemote;
import model.ACLMessage;
import model.AIDS;
import model.AgentType;
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
    	AgentLoader loader = new AgentLoader();
    	
    	System.out.println(msg.getContent());
    	System.out.println("WE GOT HIT");
    	
    	File folder = new File(msg.getContent());
    	File[] listOfFiles = folder.listFiles();

    	for (int i = 0; i < listOfFiles.length; i++) 
    	{
    		if (listOfFiles[i].isFile()) 
    		{
    			System.out.println("File " + listOfFiles[i].getName());
    			Agent agent = loader.startAgent(new AgentType("MapAgent", "pls"), "");
    			
    			/*AIDS agentAIDS = new AIDS();
    			agentAIDS.setName("map" + Integer.toString(nameGenerator++));
    			agentAIDS.setType(new AgentType("MapAgent", "pls"));
    			agentAIDS.setHost(center.getCurNode());
    			
    			agent.setAids(agentAIDS);*/
    			ACLMessage message = new ACLMessage();
    			message.setContent(msg.getContent() + "\\" + listOfFiles[i].getName());
    			agent.handleMessage(message);
    			
    	    } else if (listOfFiles[i].isDirectory()) {
    	    	System.out.println("Directory " + listOfFiles[i].getName());
    	    }
    	}
    }

}
