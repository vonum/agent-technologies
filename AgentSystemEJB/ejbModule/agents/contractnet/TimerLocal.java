package agents.contractnet;

import java.io.Serializable;

import javax.ejb.Remote;
import javax.ejb.Timer;

@Remote
public interface TimerLocal 
{
	public void startTimer(int time, Serializable o);
	
	public void end(Timer timer);	
}
