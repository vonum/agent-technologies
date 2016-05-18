package interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AIDS;
import model.AgentType;
import model.SirAgent;

public interface AgentManagerRemote 
{
	//dobavimo sve tipove agenta na sistemu
	public Map<String, AgentType> agentTypes();
	
	public String addTypes(Map<String, AgentType> types);
	
	//listu svih trenutno pokrenutih agenta
	public  ArrayList<SirAgent> runningAgents();
	
	//pokrenemo agent, zadamo mu kojeg je tipa i njegovo ime
	public String startAgent(AgentType type, String name);
	
	//zaustavimo odredjenog agenta
	public String stopAgent(String name);
	
	//posaljemo Acl poruku
	public void sendACLMessage();
	
	//dobavimo listu performativa
	public ArrayList<String> performatives();
	
	public Map<String, AgentType> getTypes();
	
	public void setTypes(Map<String, AgentType> types);
	
}
