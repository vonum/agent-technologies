package agents;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import interfaces.Agent;
import interfaces.AgentManagerRemote;
import model.ACLMessage;

/**
 * 
 * Ovo ce biti jms consumer za prihvatanje poruka i slanje pravom agentu
 *
 */
@MessageDriven(activationConfig =
{
  @ActivationConfigProperty(propertyName="destinationType",
    propertyValue="javax.jms.Queue"),
  @ActivationConfigProperty(propertyName="destination",
    propertyValue="queue/mojQueue")
})
public class MDBConsumer implements MessageListener 
{

	@EJB
	AgentManagerRemote agentManager;
	
	  public void onMessage (Message msg) {
		    try {
		    	ObjectMessage omsg = (ObjectMessage) msg;
		    	System.out.println("MDB performative: " + ((ACLMessage)omsg.getObject()).getPerformative());
		      try {
		    	  //za sad je samo ime, posle cemo ceo AClMessage objekat dobijati
		          ACLMessage message = (ACLMessage) omsg.getObject();

		          System.out.println("MDB message content: " + message.getContent() );
		          
		          msgToAgent(message);
		          
		      } catch (JMSException e) {
		          e.printStackTrace();
		      }
		    } catch (Exception e) {
		      e.printStackTrace ();
		    }
		  }
	  
	  /**
	   * Based on the name of the agent we find him and forward him the acl message
	   * @param name
	   */
	  private void msgToAgent(ACLMessage message) 
	  {
		  Agent currAgent = agentManager.getRunningAgents().get(message.getSender().getName());
		  
		  if(currAgent != null)
		  {
			  currAgent.handleMessage(message);
		  }
		  else
		  {
			  System.out.println("No running agents");
		  }
	  }
	
}
