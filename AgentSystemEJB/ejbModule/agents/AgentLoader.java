package agents;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import model.AgentType;
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
			InitialContext ctx = new InitialContext();
			Object o = ctx.lookup("java:comp/env/ejb/agentBean");
			
			System.out.println(o);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
