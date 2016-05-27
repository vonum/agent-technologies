package model;

import java.io.Serializable;

/**
 * 
 * Agent id SERBIA
 * 
 * Each agent has AIDS
 */

public class AIDS implements Serializable {

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
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return this.name.equals(o);
	}
	
	
	
}
