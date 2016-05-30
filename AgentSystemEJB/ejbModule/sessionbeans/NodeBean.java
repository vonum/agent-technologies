package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
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
import interfaces.NodeRemote;
import model.AgentCenter;
import model.AgentType;
import util.Utility;

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
	
	private Map<String, AgentCenter> centers;
	//private Map<String, AgentType> types;
	
	@EJB
	AgentManagerRemote agentManager;
    /**
     * Default constructor. 
     */
    public NodeBean() {
        // TODO Auto-generated constructor stub
    	master = new AgentCenter("192.168.0.15", "master");
    	curNode = master;
    	centers = new HashMap<String, AgentCenter>();
    	//centers.put(master.getAlias(), master);
    }

	@PostConstruct
    public void init()
    {
    	System.out.println("NAPRAVLJENI SMO FAK JEA");
    	System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
    	//types = agentManager.agentTypes();
    	//System.out.println(types.size());
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
    			target.request().post(Entity.entity(agentManager.getRunningAgents(), MediaType.APPLICATION_JSON));
    			
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
			//izbaci cvor
			centers.remove(alias);
			//javi ostalima da izbace cvor
			for(AgentCenter center : centers.values())
			{
		        ResteasyClient client = new ResteasyClientBuilder().build();
		        ResteasyWebTarget target = client.target("http://" + center.getAddress() + ":8080/AgentSystemClient/rest/node/unregister");
		        target.request().post(Entity.entity(alias, MediaType.TEXT_PLAIN));
			}
		}
		else
		{
			//izbaci cvor
			centers.remove(alias);
		}
		
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

}
