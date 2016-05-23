package model;

import java.io.Serializable;

import interfaces.Agent;

public class SirAgent implements Agent, Serializable
{
	private AIDS aids;

	public SirAgent(){}
	
	@Override
	public void init(AIDS aids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(ACLMessage msg) {
		// TODO Auto-generated method stub
		System.out.println("You said hi to sir agent, sir");
		
	}

	@Override
	public String ping() {
		// TODO Auto-generated method stub
		return null;
	}

	public AIDS getAids() {
		return aids;
	}

	public void setAids(AIDS aids) {
		this.aids = aids;
	}
	
	
}
