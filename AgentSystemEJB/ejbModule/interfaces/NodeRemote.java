package interfaces;

import java.util.List;
import java.util.Map;

import model.AgentCenter;

public interface NodeRemote {

	public Object registerAgentCenter(AgentCenter center);
	
	//public String registerExistingAgentCenters(List<AgentCenter> centers);
	
	public String unregisterAgentCenter(String alias);
	
	public String hiMaster();
	
	public String byeMaster();
	
	public AgentCenter getMaster();
	
	public AgentCenter getCurNode();
	
	public Map<String, AgentCenter> getCenters();
	
}
