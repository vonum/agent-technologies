package agents;

import java.util.HashMap;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import interfaces.AgentManagerRemote;
import model.ACLMessage;
import model.SirAgent;

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
		      TextMessage tmsg = (TextMessage) msg;
		      try {
		    	  //za sad je samo ime, posle cemo ceo AClMessage objekat dobijati
		          String name = tmsg.getText();

		          System.out.println("Received the name from the client: " + name );
		          
		          msgToAgent(name);
		          
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
	  private void msgToAgent(String name) 
	  {
		  SirAgent currAgent = agentManager.getRunningAgents().get(name);
		  
		  //test object for now
		  ACLMessage msg = new ACLMessage();
		  msg.setConversationId("max kek");
		  
		  if(currAgent != null)
		  {
			  currAgent.handleMessage(msg);
		  }
		  else
		  {
			  System.out.println("No running agents");
		  }
	  }
	
}
