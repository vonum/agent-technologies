package sessionbeans;

import java.util.ArrayList;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.websocket.server.PathParam;
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

@Singleton
@Remote
@Lock(LockType.WRITE)
@Path("/agents")
public class AgentManagerBean implements AgentManagerRemote
{
    @GET
    @Path("/classes")
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public ArrayList<AgentType> agentTypes() 
	{
		return null;
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
