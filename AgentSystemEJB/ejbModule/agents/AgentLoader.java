package agents;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import interfaces.Agent;
/**
 * 
 * Klasa koja bi se bavila rukovanjem postojecih agentima, nalazi ih kao beanove i startuje/stopuje
 * AgentManager znaci upravlja sa agentima i pruza rest interfejs ovo ih nalazi i poziva/stopira
 *
 */
public class AgentLoader 
{

	public void startAgent() 
	{
		try {

			Hashtable<String, Object> jndiProps = new Hashtable<>();
			jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			jndiProps.put("jboss.naming.client.ejb.context", true);
			InitialContext ctx = new InitialContext(jndiProps);
			
			
			String agentClass = Agent.class.getName();
			
			String pingClass = PingAgent.class.getSimpleName();
			
			String lookupString = "ejb:AgentSystemEJB/AgentSystemEJB//" + pingClass + "!" + agentClass + "?stateful";
			
			System.out.println(lookupString);
			
			ctx.lookup(lookupString);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
