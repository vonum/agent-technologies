package interfaces;

import java.util.ArrayList;

import model.AIDS;
import model.SirAgent;
import model.AgentType;

public interface AgentManagerRemote 
{
	//dobavimo sve tipove agenta na sistemu
	public ArrayList<AgentType> agentTypes();
	
	//listu svih trenutno pokrenutih agenta
	public ArrayList<SirAgent> runningAgents();
	
	//pokrenemo agent, zadamo mu kojeg je tipa i njegovo ime
	public String startAgent(AgentType type, String name);
	
	//zaustavimo odredjenog agenta
	public String stopAgent(AIDS aids);
	
	//posaljemo Acl poruku
	public void sendACLMessage();
	
	//dobavimo listu performativa
	public ArrayList<String> performatives();
}
