package interfaces;

import javax.ejb.Remote;

import model.ACLMessage;

@Remote
public interface MessageManagerRemote {

	public void post(ACLMessage aclMessage);
	
	public void postToCenter(ACLMessage aclMessage);
	
	public ACLMessage setupMessage(String content);
	
}
