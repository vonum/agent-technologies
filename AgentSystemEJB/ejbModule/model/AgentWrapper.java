package model;

import java.io.Serializable;

public class AgentWrapper implements Serializable
{
	private AgentType type;
	private String name;
	
	public AgentWrapper() {}

	public AgentWrapper(AgentType type, String name) {
		super();
		this.type = type;
		this.name = name;
	}


	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
