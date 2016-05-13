package interfaces;

import java.util.List;

import model.AgentCenter;

public interface NodeRemote {

	public Object registerAgentCenter(AgentCenter center);
	
	//public String registerExistingAgentCenters(List<AgentCenter> centers);
	
	public String hiMaster();
	
}
