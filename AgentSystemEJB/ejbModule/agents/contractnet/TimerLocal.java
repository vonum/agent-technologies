package agents.contractnet;

import java.io.Serializable;

import javax.ejb.Local;
import javax.ejb.Timer;

@Local
public interface TimerLocal 
{
	public void startTimer(int time, Serializable o);
	
	public void end(Timer timer);	
}
