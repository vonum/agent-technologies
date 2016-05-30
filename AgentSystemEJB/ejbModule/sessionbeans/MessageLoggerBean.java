package sessionbeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import interfaces.MessageLoggerRemote;

@Singleton
@Remote
@Path("/logger")
public class MessageLoggerBean implements MessageLoggerRemote
{

	private List<String> msgList;
	
    public MessageLoggerBean() 
	{
		msgList =  new ArrayList<String>();
	}
	
	
    @Override
    public void logMessage(String msg)
    {
    	msgList.add(msg);
    	System.out.println("Added to msg list");
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
	
}
