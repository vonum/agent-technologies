package agents.contractnet;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

@Local
@Stateless
public class TimerBean implements TimerLocal 
{
	@Resource
    TimerService timerService;
	
	
	public void startTimer(int time, Serializable o)
	{

		timerService.createTimer(time, o);
	}
	
	@Timeout
	public void end(Timer timer)
	{
		Initiator init = (Initiator) timer.getInfo();
		init.finishWait();
	}
}
