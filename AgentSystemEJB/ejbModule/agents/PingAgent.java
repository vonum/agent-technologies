package agents;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import model.SirAgent;

@Stateless(mappedName="beanName")
@LocalBean
public class PingAgent extends SirAgent
{

}
