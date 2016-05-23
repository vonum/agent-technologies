package mdb.requestresponse;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="topic/mojTopic2")                
        })
        
public class PrimalacPosiljalacMDB implements MessageListener {
    
    public PrimalacPosiljalacMDB() {
        System.out.println("PrimalacMDB created");
    }
    
    @Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

    
    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            TextMessage tm = (TextMessage) msg;
            try {
               String text = tm.getText();
               System.out.println(">>>RequestResponseMDB: Received new message : " + text);
               // malo sacekamo...
               Thread.sleep(3000);
               // pa posaljemo odgovor
               Destination odKoga = msg.getJMSReplyTo();

               Topic topic = (Topic) odKoga;  
       		
               TopicConnection connection = (TopicConnection)connectionFactory.createConnection("guest", "guestguest");
               TopicSession session = connection.createTopicSession(false,
                       Session.AUTO_ACKNOWLEDGE);
               
               TopicPublisher publisher = session.createPublisher(topic);
               // create and publish a message
               TextMessage message = session.createTextMessage();
               message.setText("This is a reply message.");
               publisher.publish(message);
              
               System.out.println(">>>RequestResponse MDB: Reply sent.");
               publisher.close();
               connection.close();
                  
              } catch (JMSException e) {
                  e.printStackTrace();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
        }
    }
    
    @PreDestroy
    public void remove() {
        System.out.println("PrimalacMDB destroyed.");
    }
}