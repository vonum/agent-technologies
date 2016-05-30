package sessionbeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import interfaces.MessageLoggerRemote;

@Singleton
@LocalBean
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
			return msgList.subList(cnt - 1, msgList.size() - 1);
		}
		
		return null;
	}
	
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String getMessageCount()
	{
		return String.valueOf(msgList.size());
	}
	
}
