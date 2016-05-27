package interfaces;

import model.ACLMessage;
import model.AIDS;

public interface Agent
{
	public void init(AIDS aids);
	
	public void stop();
	
	public void handleMessage(ACLMessage msg);

	public String ping();
	
	public void setAids(AIDS aids);
	
	public AIDS getAids();
}
