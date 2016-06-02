package sockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import interfaces.AgentManagerRemote;
import interfaces.MessageManagerRemote;
import interfaces.NodeRemote;
import model.ACLMessage;
import model.AgentWrapper;

@ServerEndpoint("/websocket")
@Singleton
public class WebSocketManager {

	ObjectMapper mapper;
	
	@EJB
	NodeRemote node;
	
	@EJB
	AgentManagerRemote agentManager;
	
	@EJB
	MessageManagerRemote messageManager;
	
	private List<Session> sessions;
	
	public WebSocketManager()
	{
		sessions = new ArrayList<Session>();
		mapper = new ObjectMapper();
	}
	
	@OnOpen
	public void onOpen(Session session) 
	{
		if(!sessions.contains(session))
		{
			sessions.add(session);
		}
	}
	
	@OnMessage
	public void handleMessage(Session session, String message, boolean last) throws JsonParseException, JsonMappingException, IOException
	{
		if(session.isOpen())
		{
			String[] parts = message.split(":");
			
			if(parts[0].equals("startAgent"))
			{
				AgentWrapper wrapper = mapper.readValue(parts[1], AgentWrapper.class);
				agentManager.startAgent(wrapper);
			}
			else if(parts[0].equals("stopAgent"))
			{
				agentManager.stopAgent(parts[1]);
			}
			else if(parts[0].equals("sendMessage"))
			{
				ACLMessage msg = mapper.readValue(parts[1], ACLMessage.class);
				agentManager.sendACLMessage(msg);
			}
			else if(parts[0].equals("register"))
			{
				
			}
			else if(parts[0].equals("unregister"))
			{
				
			}
		}	
		
	}
	
	@OnClose
	public void close(Session session)
	{
		sessions.remove(session);
	}
	
	@OnError
	public void error(Session session, Throwable t)
	{
		sessions.remove(session);
		t.printStackTrace();
	}
	
}
