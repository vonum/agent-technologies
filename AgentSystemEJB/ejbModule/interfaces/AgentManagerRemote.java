package interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import model.ACLMessage;
import model.AIDS;
import model.AgentType;
import model.AgentWrapper;
import model.Performative;

@Remote
public interface AgentManagerRemote 
{
	//dobavimo sve tipove agenta na sistemu
	public Map<String, AgentType> agentTypes();
	
	//dodavanje novoih tipova koji se podrzavaju
	public String addTypes(Map<String, AgentType> types);
	
	//listu svih trenutno pokrenutih agenta
	public  Map<String, Agent> runningAgents();
	
	//kada se podigne cvor, dobije listu pokrenutih agenata
	public void setRunningAgents(Map<String, AIDS> agents);
	
	//pokrenemo agent, zadamo mu kojeg je tipa i njegovo ime
	public String startAgent(AgentWrapper rapper);
	
	//dodaj pokrenutog agenta u listu pokrenutih agenata
	public void addRunningAgent(AIDS agent);
	
	//zaustavimo odredjenog agenta
	public String stopAgent(String name);
	
	//posaljemo Acl poruku
	public void sendACLMessage(ACLMessage acl);
	
	//dobavimo listu performativa
	public List<Performative> performatives();
	
	public Map<String, AgentType> getTypes();
	
	public void setTypes(Map<String, AgentType> types);
	
	public Map<String, Agent> getRunningAgents();
	
	public Map<String, AIDS> allAgents(); 
	
	public Map<String, AIDS> getAllAgents();
	
	public void setAllAgents(Map<String, AIDS> allAgents);
	
	public void removeAgents(String alias);
	
	public void resetAgents();
	
}
