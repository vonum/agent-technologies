package sessionbeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import interfaces.NodeRemote;
import model.AgentCenter;

/**
 * Session Bean implementation class NodeBean
 */
@Singleton
@LocalBean
@Lock(LockType.WRITE)
@Path("/node")
public class NodeBean implements NodeRemote{

	private AgentCenter master;
	private AgentCenter curNode;
	
	private Map<String, AgentCenter> centers;
    /**
     * Default constructor. 
     */
    public NodeBean() {
        // TODO Auto-generated constructor stub
    	master = new AgentCenter("192.168.0.14", "master");
    	curNode = master;
    	centers = new HashMap<String, AgentCenter>();
    	//centers.put(master.getAlias(), master);
    }

    @POST
    @Path("/register")	//masteru se posalje nov AgentCenter, on ga doda, i salje svim ostalim cvorovima da ga dodaju(/add)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<AgentCenter> registerAgentCenter(AgentCenter center) {
		// TODO Auto-generated method stub

    	System.out.println("Handling request");
    	if(curNode.getAlias().equals("master"))
    	{
    		System.out.println("Adding center");
    		if(!centers.containsKey(center.getAlias()))
    		{
    			//javi ostalim cvorovima da dodaju taj cvor
    			for(AgentCenter cnt : centers.values())
    			{
    		        ResteasyClient client = new ResteasyClientBuilder().build();
    		        ResteasyWebTarget target = client.target("http://" + cnt.getAddress() + ":8080/AgentSystemClient/rest/node/register");
    			}
    			
    			centers.put(center.getAlias(), center);	
    			
    			return new ArrayList(centers.values());
    		}
    		else
    		{
    			return null;
    		}
    	}
    	else	//ako nije master, znaci da samo treba da doda nov centar u mapu
    	{
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
        	System.out.println("Sending message");
	        ResteasyClient client = new ResteasyClientBuilder().build();
	        ResteasyWebTarget target = client.target("http://" + master.getAddress() + ":8080/AgentSystemClient/rest/node/register");
	        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(curNode, MediaType.APPLICATION_JSON));
	        ArrayList<AgentCenter> ret = response.readEntity(ArrayList.class);

	        if(ret != null)
	        {
	        	System.out.println("Message recieved");
	            for(Object temp : ret)
	            {
	                if(temp instanceof LinkedHashMap)
	                {
	                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) temp;
	                    centers.put((String)map.get("alias"), new AgentCenter((String)map.get("address"), (String)map.get("alias")));
	                }
	            }
	            return "success";
	        }
	        return "error";
        }
        else
        {
        	return "master pls";
        }
	}

	
	@GET
	@Path("/nodes")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, AgentCenter> getNodes() //test za gledanje stanja liste centara
	{
		return centers;
	}
	
}
