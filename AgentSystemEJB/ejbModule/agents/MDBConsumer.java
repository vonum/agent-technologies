package agents;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

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

	  public void onMessage (Message msg) {
		    try {
		      TextMessage tmsg = (TextMessage) msg;
		      try {
		          String text = tmsg.getText();
		          long time = tmsg.getLongProperty("sent");
		          System.out.println("Received new message from Queue : " + text + ", with timestamp: " + time);
		      } catch (JMSException e) {
		          e.printStackTrace();
		      }
		    } catch (Exception e) {
		      e.printStackTrace ();
		    }
		  }
	
}
