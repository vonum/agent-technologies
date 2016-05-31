package interfaces;

import java.util.List;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Remote
public interface MessageLoggerRemote 
{
	public List<String> getNewMessages(String count);
	
	public void logMessage(String msg);
	
	public void addExternalMessage(String msg);
}
