package interfaces;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface MessageLoggerRemote 
{
	public List<String> getNewMessages(String count);
	
	public void logMessage(String msg);
}
