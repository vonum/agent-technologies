package agents.contractnet;

import java.io.Serializable;

import model.AIDS;

public class Proposal implements Serializable
{
	private String content;
	private Serializable contentObj;
	private AIDS participant;
	private AIDS initiator;
	private boolean proposing;
	private long timeEstimate;
	
	public Proposal() {}

	public Proposal(String content, Serializable contentObj, AIDS participant, AIDS initiator, boolean proposing,
			long timeEstimate) {
		super();
		this.content = content;
		this.contentObj = contentObj;
		this.participant = participant;
		this.initiator = initiator;
		this.proposing = proposing;
		this.timeEstimate = timeEstimate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Serializable getContentObj() {
		return contentObj;
	}

	public void setContentObj(Serializable contentObj) {
		this.contentObj = contentObj;
	}

	public AIDS getParticipant() {
		return participant;
	}

	public void setParticipant(AIDS participant) {
		this.participant = participant;
	}

	public AIDS getInitiator() {
		return initiator;
	}

	public void setInitiator(AIDS initiator) {
		this.initiator = initiator;
	}

	public boolean isProposing() {
		return proposing;
	}

	public void setProposing(boolean proposing) {
		this.proposing = proposing;
	}

	public long getTimeEstimate() {
		return timeEstimate;
	}

	public void setTimeEstimate(long timeEstimate) {
		this.timeEstimate = timeEstimate;
	}
	
}
