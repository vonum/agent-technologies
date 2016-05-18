package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import interfaces.Agent;
import model.SirAgent;

@Stateful
@Remote(Agent.class)
public class PingAgent extends SirAgent
{

}
