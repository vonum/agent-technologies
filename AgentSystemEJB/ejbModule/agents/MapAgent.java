package agents;

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
    }

}
