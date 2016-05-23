package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
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
import agents.PingAgent;
import agents.PongAgent;
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
	
	private  Map<String, SirAgent> runningAgents;

	
	@EJB
	NodeRemote node;
	
	public AgentManagerBean()
	{
		types = new HashMap<String, AgentType>();
		runningAgents = new  HashMap<String, SirAgent>();
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
	public Map<String, SirAgent> runningAgents() 
	{
		return runningAgents;
	}
    
    @POST
    @Path("/running")
    @Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void setRunningAgents(Map<String, SirAgent> agents) {
		// TODO Auto-generated method stub
		this.runningAgents = (Map<String, SirAgent>) agents;
	}
    
    @POST
    @Path("/agent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public void addRunningAgent(SirAgent agent)
    {	
    	this.runningAgents.put(agent.getAids().getName(), agent);
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
		
		String agentType = rapper.getType().getName();
		
		//cast proper agent type
		if(agentType.equals("PingAgent"))
		{
			agent = (PingAgent) new PingAgent();
		} 
		else if(agentType.equals("PingAgent"))
		{
			agent = (PongAgent) new PongAgent();
		}
		
		AIDS aids = new AIDS();
		
		aids.setName(rapper.getName());
		aids.setHost(node.getCurNode());
		aids.setType(rapper.getType());
		agent.setAids(aids);
		
		//dodamo agenta u listu pokrenutih
		runningAgents.put(agent.getAids().getName(), agent);
    	
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
    @Path("/stop/{aids}")
	@Override
	public String stopAgent(@PathParam("aids") String name) 
	{
    	boolean tmp;
    	tmp = name.endsWith("*");
    	
    	System.out.println("Pre: " + runningAgents.size());
    	System.out.println("Ime agenta kojeg izbacujemo: " + name);
    	//System.out.println(runningAgents.get(name).getAids().getName());
    	//remove agent from the map
    	runningAgents.remove(name);
    	
    	if(!tmp)
    	{   
    		ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target;
    		
            //javimo masteru da izbaci pokrenutog agenta, ako nije master
    		if(!node.getCurNode().getAlias().equals("master"))
    		{
    	        target = client.target("http://" + node.getMaster().getAddress() + ":8080/AgentSystemClient/rest/agents/stop/" + name + "*");
    	        target.request().get();
    		}
    		
            //javimo svim ostalim cvorovima da izbace pokrenutog agenta
        	for(AgentCenter center : node.getCenters().values())
        	{
        		if(!center.getAlias().equals(node.getCurNode().getAlias()))
        		{
        	        target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/agents/stop/" + name + "*");
        	        target.request().get();
        		}
        	}
    	}
    	else
    	{
    		name = name.substring(0, name.length() - 1);
    	}
    	
	    
    	
	    System.out.println("Posle " + runningAgents.size());
    	
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
	
		try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context
					.lookup("jms/queue/mojQueue");
			context.close();
			Connection connection = cf.createConnection("guest", "guestguest");
			final Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			connection.start();

			MessageConsumer consumer = session.createConsumer(queue);

		    TextMessage msg = session.createTextMessage("Queue message!");
		    // The sent timestamp acts as the message's ID
		    long sent = System.currentTimeMillis();
		    msg.setLongProperty("sent", sent);
		    
			MessageProducer producer = session.createProducer(queue);
			producer.send(msg);
			Thread.sleep(1000);
			System.out.println("Message published. Please check application server's console to see the response from MDB.");

			producer.close();
			consumer.close();
			connection.stop();
		    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
	public Map<String, SirAgent> getRunningAgents() {
		// TODO Auto-generated method stub
		return this.runningAgents;
	}


    
}
