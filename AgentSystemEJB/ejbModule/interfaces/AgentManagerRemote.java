package interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.websocket.server.PathParam;

import model.AgentType;
import model.AgentWrapper;
import model.SirAgent;

@Remote
public interface AgentManagerRemote 
{
	//dobavimo sve tipove agenta na sistemu
	public Map<String, AgentType> agentTypes();
	
	//dodavanje novoih tipova koji se podrzavaju
	public String addTypes(Map<String, AgentType> types);
	
	//listu svih trenutno pokrenutih agenta
	public  Map<String, SirAgent> runningAgents();
	
	//kada se podigne cvor, dobije listu pokrenutih agenata
	public void setRunningAgents(Map<String, SirAgent> agents);
	
	//pokrenemo agent, zadamo mu kojeg je tipa i njegovo ime
	public String startAgent(AgentWrapper rapper);
	
	//dodaj pokrenutog agenta u listu pokrenutih agenata
	public void addRunningAgent(SirAgent agent);
	
	//zaustavimo odredjenog agenta
	public String stopAgent(String name);
	
	//posaljemo Acl poruku
	public void sendACLMessage(String name);
	
	//dobavimo listu performativa
	public ArrayList<String> performatives();
	
	public Map<String, AgentType> getTypes();
	
	public void setTypes(Map<String, AgentType> types);
	
	public Map<String, SirAgent> getRunningAgents();
	
}
