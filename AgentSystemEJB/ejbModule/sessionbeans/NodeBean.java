package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import interfaces.AgentManagerRemote;
import interfaces.MessageLoggerRemote;
import interfaces.NodeRemote;
import model.AIDS;
import model.AgentCenter;
import model.AgentType;

/**
 * Session Bean implementation class NodeBean
 * 
 * Ovo ce biti onda klasa gde upravljamo razlicitim nodovima u serveru,
 * pored registracije, morace da dele listu aktivnih agenta/tipova agenta...
 * i podrzava rollback ako dodje do gresaka
 * 
 */
@Singleton
@LocalBean
@Lock(LockType.WRITE)
@Path("/node")
public class NodeBean implements NodeRemote{

	private AgentCenter master;
	private AgentCenter curNode;
	private boolean registered;
	
	private Map<String, AgentCenter> centers;
	
	@EJB
	AgentManagerRemote agentManager;
	
	@EJB
	MessageLoggerRemote logger;
    /**
     * Default constructor. 
     */
    public NodeBean() {
        // TODO Auto-generated constructor stub
    	master = new AgentCenter("192.168.0.15", "master");
    	curNode = master;
    	centers = new HashMap<String, AgentCenter>();
    	registered = false;
    }

	@PostConstruct
    public void init()
    {
    	System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
    }

    @POST
    @Path("/register")	//masteru se posalje nov AgentCenter, on ga doda, i salje svim ostalim cvorovima da ga dodaju(/add)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<AgentCenter> registerAgentCenter(AgentCenter center) {
		// TODO Auto-generated method stub

    	System.out.println("Handling register request");
    	if(curNode.getAlias().equals("master"))
    	{
    		if(!centers.containsKey(center.getAlias()))
    		{
        		System.out.println("Adding center");
        		System.out.println("Sending http to other nodes");
        		
        		//2. master trazi koje tipove agenata podrzava novi cvor
		        ResteasyClient client = new ResteasyClientBuilder().build();
		        ResteasyWebTarget target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/agents/classes");
		        Response rsp = target.request(MediaType.APPLICATION_JSON).get();
		        
		        Map<String, AgentType> retTypes = rsp.readEntity(HashMap.class);
		        Map<String, AgentType> types = agentManager.getTypes();
		        
		        //dodavanje agenata u listu postojecih
	            for(Object map : retTypes.values())
	            {
	                if(map instanceof LinkedHashMap)
	                {
	                    LinkedHashMap type = (LinkedHashMap) map;
	                    types.put((String)type.get("name"), new AgentType((String)type.get("name"), (String)type.get("module")));
	                    agentManager.setTypes(types);
	                }
	            }

    			//3. master javlja ostalim cvorovima da dodaju taj cvor
    			for(AgentCenter cnt : centers.values())
    			{
    		        target = client.target("http://" + cnt.getAddress() + ":8080/AgentSystemClient/rest/node/register");
    		        Response response = target.request().post(Entity.entity(center, MediaType.APPLICATION_JSON));
    			}
    			
    			//4. master dostavlja svima listu tipova agenata koje svi zajedno podrzavaju
		        for(AgentCenter cnt : centers.values())
		        {
    		        target = client.target("http://" + cnt.getAddress() + ":8080/AgentSystemClient/rest/agents/classes");
    		        Response response = target.request().post(Entity.entity(types, MediaType.APPLICATION_JSON));
    		        System.out.println(response.readEntity(String.class));
		        }
		        
		        //5. master novom cvoru dostavlja listu svih podrzanih tipova agenata
		        target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/agents/classes");
		        Response response = target.request().post(Entity.entity(types, MediaType.APPLICATION_JSON));
		        System.out.println(response.readEntity(String.class));
    			
    			centers.put(center.getAlias(), center);	
    			
    			//6. master cvor dostavlja listu pokrenutih agenata novom cvoru
    			target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/agents/running");
    			target.request().post(Entity.entity(agentManager.getAllAgents(), MediaType.APPLICATION_JSON));
    			
    			//7. na kraju vrati listu cvorova novom cvoru 
    			return new ArrayList(centers.values());
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else	//ako nije master, znaci da samo treba da doda nov centar u mapu
    	{
    		System.out.println("Adding center, nomaster");
    		centers.put(center.getAlias(), center);
    	}
    	
    	return null;
	}

	@GET
	@Path("/hello")	//sa frontenda gadjamo ovu metodu da bi se poslao zahtev na master za registraciju
	@SuppressWarnings("unchecked")
	@Override
	public String hiMaster() {
		// TODO Auto-generated method stub
        if(!curNode.getAlias().equals("master"))
        {
        	System.out.println("Sending register message");
	        ResteasyClient client = new ResteasyClientBuilder().build();
	        ResteasyWebTarget target = client.target("http://" + master.getAddress() + ":8080/AgentSystemClient/rest/node/register");
	        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(curNode, MediaType.APPLICATION_JSON));
	        ArrayList<AgentCenter> ret = response.readEntity(ArrayList.class);

	        if(ret != null)
	        {
	        	System.out.println("Register response recieved");
	            for(Object temp : ret)
	            {
	                if(temp instanceof LinkedHashMap)
	                {
	                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) temp;
	                    centers.put((String)map.get("alias"), new AgentCenter((String)map.get("address"), (String)map.get("alias")));
	                }
	            }
	            registered = true;
	            return "true";
	        }
	        return "false";
        }
        else
        {
        	return "false";
        }
	}

	@POST
	@Path("/unregister")
	@Override
	public String unregisterAgentCenter(String alias) {
		// TODO Auto-generated method stub
		if(curNode.getAlias().equals("master"))
		{
			//izbaci cvor i javi ostalima da izbace cvor
			ripNode(alias);
		}
		else
		{
			//izbaci cvor
			centers.remove(alias);
		}
		
		removeRunningAgents(alias);
		
		return "true";

	}

	@GET
	@Path("/bye")
	@Override
	public String byeMaster() {
		// TODO Auto-generated method stub
    	System.out.println("Sending unregister message");
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://" + master.getAddress() + ":8080/AgentSystemClient/rest/node/unregister");
        Response response = target.request().post(Entity.entity(curNode.getAlias(), MediaType.TEXT_PLAIN));
		
		return response.readEntity(String.class);
	}
	
	@GET
	@Path("/nodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, AgentCenter> getNodes() //test za gledanje stanja liste centara
	{
		return centers;
	}

	@Override
    public AgentCenter getMaster() {
		return master;
	}

	@Override
	public AgentCenter getCurNode() {
		return curNode;
	}
	
	@Override
	public Map<String, AgentCenter> getCenters() {
		return centers;
	}

	@GET
	@Path("/registered")
	@Override
	public String isRegistered() {
		// TODO Auto-generated method stub
		return registered ? "true" : "false";
	}
	
	@GET
	@Path("/master")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public AgentCenter isMaster()
	{
		if(curNode.getAlias().equals(master.getAlias()))
		{
			return null;
		}
		else
		{
			return master;
		}
	}

	@Schedule(hour = "*", minute = "*", second = "*/20", info = "every tenth second")
	@Override
	public void callNigga() {
		// TODO Auto-generated method stub
		//logger.logMessage("pls");
		
		//svakih n sekundi se salje zahtev svim cvorovima da vide da li je negro ziv
		if(curNode.getAlias().equals("master"))
		{
	        ResteasyClient client = new ResteasyClientBuilder().build();
	        ResteasyWebTarget target;
	        
	        for(AgentCenter center : centers.values())
	        {
	        	try
	        	{
			        target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/node/roar");
			        Response response = target.request(MediaType.TEXT_PLAIN).get();
			        if(!response.readEntity(String.class).equals("roar"))
			        {
			        	ripNode(center.getAlias());
			        	removeRunningAgents(center.getAlias());
			        }
	        	} catch(Exception e)
	        	{
	        		ripNode(center.getAlias());
	        		removeRunningAgents(center.getAlias());
	        	}
	        }
		}
	}

	@GET
	@Path("/roar")
	@Override
	public String hearthBear() {
		// TODO Auto-generated method stub
		return "roar";
	}
	
	private void ripNode(String alias)
	{
		//izbaci cvor
		centers.remove(alias);
		
		for(AgentCenter center : centers.values())
		{
	        ResteasyClient client = new ResteasyClientBuilder().build();
	        ResteasyWebTarget target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/node/unregister");
	        target.request().post(Entity.entity(alias, MediaType.TEXT_PLAIN));
		}
	}
	
	private void removeRunningAgents(String alias)
	{
		Map<String, AIDS> tmp = agentManager.getAllAgents();
		
		for(Entry<String, AIDS> aids : tmp.entrySet())
		{
			if(aids.getValue().getHost().getAlias().equals(alias))
			{
				tmp.remove(aids.getKey());
			}
		}
		
		agentManager.setAllAgents(tmp);
	}
	
}
