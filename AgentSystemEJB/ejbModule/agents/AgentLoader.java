package agents;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import interfaces.Agent;
import model.AIDS;
import model.AgentCenter;
import model.AgentType;
import model.SirAgent;
/**
 * 
 * Klasa koja bi se bavila rukovanjem postojecih agentima, nalazi ih kao beanove i startuje/stopuje
 * AgentManager znaci upravlja sa agentima i pruza rest interfejs ovo ih nalazi i poziva/stopira
 *
 */
public class AgentLoader 
{

	 ArrayList<SirAgent> runningAgents;
	
	public AgentLoader( ArrayList<SirAgent> runningAgents)
	{
		this.runningAgents = runningAgents;
	}

	
	public void startAgent(AgentType type, String name)
	{
		try {

			Hashtable<String, Object> jndiProps = new Hashtable<>();
			jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			jndiProps.put("jboss.naming.client.ejb.context", true);
			InitialContext ctx = new InitialContext(jndiProps);
			
			String agentClass = Agent.class.getName();
				
			String pingClass = type.getName();

			String lookupString = "ejb:AgentSystemEAR/AgentSystemEJB//" + pingClass + "!" + agentClass + "?stateful";
			
			System.out.println(lookupString);
			Object o = ctx.lookup(lookupString);

			//sad kad smo pokrenuli bean upismo informacije o datom agentu
			SirAgent agent = new SirAgent();
			
			AIDS aids = new AIDS();
			
			aids.setName(name);
			aids.setHost(new AgentCenter("", "master"));
			aids.setType(type);
			agent.setAids(aids);
			
			runningAgents.add(agent);
			
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
