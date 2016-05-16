package model;

/**
 * 
 * Agents id secure
 * 
 * Each agent has AIDS
 */

public class AIDS {

	private String name;
	
	private AgentCenter host;
	
	private AgentType type;
	
	public AIDS() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentCenter getHost() {
		return host;
	}

	public void setHost(AgentCenter host) {
		this.host = host;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}
	
	
	
	
	
}
