package interfaces;

import java.util.Map;

import model.AgentCenter;

public interface NodeRemote {

	public Map<String, AgentCenter> registerAgentCenter(AgentCenter center);
	
	//public String registerExistingAgentCenters(List<AgentCenter> centers);
	
	public String hiMaster();
	
}
