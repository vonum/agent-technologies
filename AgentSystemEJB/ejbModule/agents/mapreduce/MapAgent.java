package agents.mapreduce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import model.SirAgent;

/**
 * Session Bean implementation class MapAgent
 */
@Stateful
@Remote(Agent.class)
public class MapAgent extends SirAgent {
       
	@EJB
	NodeRemote center;
	
	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	MessageLoggerRemote logger;
	
    /**
     * @see SirAgent#SirAgent()
     */
    public MapAgent() {
        super();
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
    	logger.logMessage("Map agent " + aids.getName() + " activated");
    	logger.logMessage("Activated on : " + center.getCurNode().getAddress());
    	logger.logMessage("File URL : " + msg.getContent());
    	
    	Map<Character, Integer> occur = new HashMap<Character, Integer>();
    	
    	FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(msg.getContent());
			
	    	int r;
	    	while ((r = fileInput.read()) != -1) 
	    	{
	    	   Character c = new Character((char) r);
	    	   if(occur.containsKey(c))
	    	   {
	    		   occur.put(c, occur.get(c) + 1);
	    	   }
	    	   else
	    	   {
	    		   occur.put(c, 0);
	    	   }
	    	}
	    	fileInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found : " + msg.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file");
		}

		for(Entry<Character, Integer> e : occur.entrySet())
		{
			logger.logMessage(e.getKey() + " : " + e.getValue());
		}
    }

}
