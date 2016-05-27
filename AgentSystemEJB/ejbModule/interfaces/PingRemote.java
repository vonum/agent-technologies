package interfaces;

import javax.ejb.Remote;

@Remote
public interface PingRemote 
{
	public void hi();
}
