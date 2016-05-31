package agents;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import interfaces.Agent;
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
	
	public AgentLoader()
	{
	}
	
	public Agent startAgent(AgentType type, String name)
	{
		try {

			Hashtable<String, Object> jndiProps = new Hashtable<>();
			jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			jndiProps.put("jboss.naming.client.ejb.context", true);
			InitialContext ctx = new InitialContext(jndiProps);
			
			//interface za stateful bean
			String agentInterface = Agent.class.getName();
			
			//class za stateful bean
			String agentClass = type.getName();

			String lookupString = "ejb:AgentSystemEAR/AgentSystemEJB//" + agentClass + "!" + agentInterface + "?stateful";
			
			System.out.println(lookupString);
			Object agent = ctx.lookup(lookupString);
			
			return (Agent) agent;
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}
}
