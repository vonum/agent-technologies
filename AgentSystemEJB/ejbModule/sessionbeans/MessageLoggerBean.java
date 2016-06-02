package sessionbeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import interfaces.MessageLoggerRemote;
import interfaces.NodeRemote;
import model.AgentCenter;

@Singleton
@Remote
@Path("/logger")
public class MessageLoggerBean implements MessageLoggerRemote
{
	
	@EJB
	NodeRemote node;
	
	private List<String> msgList;
	
    public MessageLoggerBean() 
	{
		msgList =  new ArrayList<String>();
	}
	
    @Override
    public void logMessage(String msg)
    {
    	msgList.add(msg);
    	
    	sendMessageToNodes(msg);
    }
    
    
    @GET
    @Path("/addmsg/{msg}")
    @Override
    public void addExternalMessage(@PathParam("msg") String msg)
    {
    	msgList.add(msg);
    }
    
	@GET
	@Path("/messages/{count}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<String> getNewMessages(@PathParam("count") String count)
	{
		int cnt = Integer.valueOf(count);
		
		//if the list size on the fronted doesn't match the one on the server
		if(cnt !=  msgList.size())
		{
			//if the list is empty
			if(cnt == 0)
			{
				return msgList;
			}
			else
			{
				int lowerBound = cnt;
				int upperBound = msgList.size();
				
				return getNewMessages(lowerBound, upperBound);
			}
			
		}
		
		return null;
	}
	
	/**
	 *  Adds new messages to the list to send to users, subList wasn't working
	 */
	private ArrayList<String> getNewMessages(int lowerBound, int upperBound)
	{
		ArrayList<String> newMessages =  new ArrayList<String>();
		
		for(int i = lowerBound; i < upperBound;++i)
		{
			newMessages.add(msgList.get(i));
		}
		
		return newMessages;
	}
	
	/**
	 *  Sends our local msg to all the other nodes
	 */
	private void sendMessageToNodes(String msg)
	{
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target;
        
        System.out.println("ODJE size: " + node.getCenters().size());
        
        if(!node.getCurNode().getAlias().equals("master"))
        {
			target = client.target("http://" + node.getMaster().getAddress() + 
		               ":8080/AgentSystemClient/rest/logger/addmsg/" + msg);
			target.request().get();
        }
        
		for(AgentCenter center : node.getCenters().values())
    	{
			System.out.println("ODJE centar: " + center.getAlias());
			
			if(!center.getAlias().equals(node.getCurNode().getAlias()))
			{
				System.out.println("ODJE: ");
				
				target = client.target("http://" + center.getAddress() + 
			               ":8080/AgentSystemClient/rest/logger/addmsg/" + msg);
				target.request().get();
			}
			
    	}
	}
	
}
