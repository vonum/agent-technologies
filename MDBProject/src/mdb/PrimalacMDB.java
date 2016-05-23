package mdb;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import session.LoggerLocal;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="topic/mojTopic")                
        })
        
public class PrimalacMDB implements MessageListener {
    
    public PrimalacMDB() {
        System.out.println("PrimalacPosiljalacMDB created");
    }
    
    @Resource
    MessageDrivenContext ctx;
    
    @EJB
    LoggerLocal log;
    
    public void onMessage(Message msg) { 
      if (msg instanceof TextMessage) {
          TextMessage tm = (TextMessage) msg;
          try {
              String text = tm.getText();
              System.out.println("Received new message : " + text);
              log.log(text);
              // izazovemo retransmisiju, (ovo nece potrditi prijem cak i 
              // da postavimo acknowledge na AUTO, sto je default)
              Thread.sleep(3000);
//              ctx.setRollbackOnly();
//              throw new EJBException("Pucanj!");
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