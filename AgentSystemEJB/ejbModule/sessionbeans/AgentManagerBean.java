package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import agents.AgentLoader;
import interfaces.AgentManagerRemote;
import model.AIDS;
import model.AgentType;
import model.SirAgent;
import util.Utility;

@Singleton
@Remote
@Lock(LockType.WRITE)
@Path("/agents")
public class AgentManagerBean implements AgentManagerRemote
{
	private Map<String, AgentType> types;
	
	public AgentManagerBean()
	{
		types = new HashMap<String, AgentType>();
	}
	
	@PostConstruct
	public void init()
	{
		types = Utility.readAgentTypesFromFile("AgentSystemResources/types.txt");
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
    public String addTypes(Map<String, AgentType> types)
    {
    	this.types = types;
    	
    	return "added";
    }

    @GET
    @Path("/running")
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public ArrayList<SirAgent> runningAgents() 
	{

		return null;
	}

    @PUT
    @Path("/running/{type}/{name}")
	@Override
	public String startAgent(@PathParam("type") AgentType type, 
							@PathParam("name") String name) 
	{
    	//primer nekog poziva koj delegira nalazenje i pozivanje agenta ka AgentLoader
    	AgentLoader agentLoader = new AgentLoader();
    	agentLoader.startAgent(type, name);
    	
		return null;
	}

    @DELETE
    @Path("/running/{aids}")
	@Override
	public String stopAgent(@PathParam("aids") AIDS aids) 
	{
		return null;
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

}
