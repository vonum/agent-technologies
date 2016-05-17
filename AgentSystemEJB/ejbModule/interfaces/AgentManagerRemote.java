package interfaces;

import java.util.ArrayList;
import java.util.List;

import model.AIDS;
import model.AgentType;
import model.SirAgent;

public interface AgentManagerRemote 
{
	//dobavimo sve tipove agenta na sistemu
	public List<AgentType> agentTypes();
	
	//listu svih trenutno pokrenutih agenta
	public List<SirAgent> runningAgents();
	
	//pokrenemo agent, zadamo mu kojeg je tipa i njegovo ime
	public String startAgent(AgentType type, String name);
	
	//zaustavimo odredjenog agenta
	public String stopAgent(AIDS aids);
	
	//posaljemo Acl poruku
	public void sendACLMessage();
	
	//dobavimo listu performativa
	public ArrayList<String> performatives();
}
