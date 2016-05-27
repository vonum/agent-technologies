package agents;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
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
    	System.out.println(msg.getContent());
    	System.out.println("MAP GOT HIT");
    	
    	FileInputStream fileInput;
		try {
			fileInput = new FileInputStream(msg.getContent());
			
	    	int r;
	    	while ((r = fileInput.read()) != -1) {
	    	   char c = (char) r;
	    	   System.out.println(c);
	    	}
	    	fileInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found : " + msg.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file");
		}

    }

}
