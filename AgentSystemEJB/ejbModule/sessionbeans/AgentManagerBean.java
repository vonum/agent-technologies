package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AgentLoader;
import interfaces.AgentManagerRemote;
import interfaces.NodeRemote;
import model.AIDS;
import model.AgentCenter;
import model.AgentType;
import model.AgentWrapper;
import model.SirAgent;
import util.Utility;


@Singleton
@Remote
@Lock(LockType.WRITE)
@Path("/agents")
public class AgentManagerBean implements AgentManagerRemote
{
	private Map<String, AgentType> types;
	
	private  ArrayList<SirAgent> runningAgents;
	
	//ovo je samo da imena agenta budu lel1, lel2, lel3 izmenecemo kasnije
	private static int count = 0;
	
	@EJB
	NodeRemote node;
	
	public AgentManagerBean()
	{
		types = new HashMap<String, AgentType>();
		runningAgents = new  ArrayList<SirAgent>();
	}
	
	@PostConstruct
	public void init()
	{
		types = Utility.readAgentTypesFromFile("AgentSystemResources/types.txt");
		
		System.out.println("WE RE HERE");
		
	}
	
    @GET
    @Path("/classes")
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public Map<String, AgentType> agentTypes() 
	{
		return types;
	}

	@POST
    @Path("/classes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public String addTypes(Map<String, AgentType> types)
    {
    	System.out.println("we are here");
    	System.out.println(types.getClass());
    	this.types = types;
    	
    	return "added";
    }

    @GET
    @Path("/running")
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<SirAgent> runningAgents() 
	{
		return runningAgents;
	}
    
    @POST
    @Path("/running")
    @Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void setRunningAgents(List<SirAgent> agents) {
		// TODO Auto-generated method stub
		this.runningAgents = (ArrayList<SirAgent>) agents;
	}
    
    @POST
    @Path("/agent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public void addRunningAgent(SirAgent agent)
    {	
    	this.runningAgents.add(agent);
    }

    @PUT
    @Path("/running")
    @Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String startAgent(AgentWrapper rapper) 
	{	
    	//primer nekog poziva koj delegira nalazenje i pozivanje agenta ka AgentLoader
    	AgentLoader agentLoader = new AgentLoader();
    	agentLoader.startAgent(rapper.getType(), rapper.getName());
    	
		//sad kad smo pokrenuli bean upisemo informacije o datom agentu
		SirAgent agent = new SirAgent();
		
		AIDS aids = new AIDS();
		
		aids.setName(rapper.getName());
		aids.setHost(node.getCurNode());
		aids.setType(rapper.getType());
		agent.setAids(aids);
		
		//dodamo agenta u listu pokrenutih
		runningAgents.add(agent);
    	
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target;
		
		//javimo masteru da doda pokrenutog agenta, ako nije master
		if(!node.getCurNode().getAlias().equals("master"))
		{
	        target = client.target("http://" + node.getMaster().getAddress() + ":8080/AgentSystemClient/rest/agents/agent");
	        target.request().post(Entity.entity(agent, MediaType.APPLICATION_JSON));
		}
		
        //javimo svim ostalim cvorovima da dodaju pokrenutog agenta
    	for(AgentCenter center : node.getCenters().values())
    	{
    		if(!center.getAlias().equals(node.getCurNode().getAlias()))
    		{
    	        target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/agents/agent");
    	        target.request().post(Entity.entity(agent, MediaType.APPLICATION_JSON));
    		}
    	}
    	
    	System.out.println(runningAgents.size());
    	
		return "true";
	}

    @GET
    @Path("/stop")
	@Override
	public String stopAgent(@PathParam("aids") String name) 
	{
    	int deletedIndex = -1;
    	
    	for(SirAgent milan : runningAgents)
    	{
    		//this is true in real life, sad story milan gud bro
    		if(milan.getAids().getName().equals(name))
    		{
    			break;
    		}
    		deletedIndex++;
    	}
    	
    	//rip milan
    	runningAgents.remove(deletedIndex);
    	System.out.println(runningAgents.size());
    	
		return "";
	}

	@POST
	@Path("/messages")
	@Override
	public void sendACLMessage() 
	{
		
	}

	@GET
	@Path("/messages")
	@Override
	public ArrayList<String> performatives() 
	{
		
		return null;
	}

	@Override
	public Map<String, AgentType> getTypes() {
		return types;
	}
	
	@Override
    public void setTypes(Map<String, AgentType> types) {
		this.types = types;
	}

	@Override
	public List<SirAgent> getRunningAgents() {
		// TODO Auto-generated method stub
		return this.runningAgents;
	}


    
}
