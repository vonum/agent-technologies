package model;

/**
 * 
 * Tipovi agenta
 *
 */

public class AgentType {
	private String name;
	private String module;
	
	public AgentType(){}
	
	public AgentType(String name, String module) {
		this.name = name;
		this.module = module;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return module;
	}
	public void setType(String module) {
		this.module = module;
	}
	
	
	
}
