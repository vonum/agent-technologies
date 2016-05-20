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
import javax.ws.rs.core.MediaType;

import agents.AgentLoader;
import interfaces.AgentManagerRemote;
import interfaces.NodeRemote;
import model.AgentType;
import model.SirAgent;
import util.Utility;

@Singleton()
@Remote
@Lock(LockType.WRITE)
@Path("/agents")
public class AgentManagerBean implements AgentManagerRemote
{
	private Map<String, AgentType> types;
	
	private  ArrayList<SirAgent> runningAgents;
	
	//ovo je samo da imena agenta budu lel1, lel2, lel3 izmenecemo kasnije
	private static int count = 0;
	
	//@IgnoreDependency
	@EJB
	NodeRemote nodeBean;
	
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

    @PUT
    @Path("/running/")
    @Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String startAgent(@PathParam("type") AgentType type, 
							@PathParam("name") String name) 
	{
    	
    	name = "lel" + count;
    	
    	count++;
    	
    	System.out.println(name);
    	
    	//primer nekog poziva koj delegira nalazenje i pozivanje agenta ka AgentLoader
    	AgentLoader agentLoader = new AgentLoader(runningAgents);
    	agentLoader.startAgent(type, name);
    	
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
