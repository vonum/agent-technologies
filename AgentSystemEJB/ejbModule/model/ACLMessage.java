package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * Poruka koju agenti razmenjuje između sebe i u komunikaciji sa agentskim centrom
 * 
 *
 */


public class ACLMessage implements Serializable{
	
	private Performative performative;
	
	private AIDS sender;
	
	private AIDS[] receivers;
	
	private AIDS replyTo;
	
	private String content;
	
	private Object contentObject;
	
	private HashMap<String, Object> userArgs;
	
	private String language;
	
	private String encoding;
	
	private String ontology;
	
	private String protocol;
	
	private String conversationId;
	
	private String replayWith;
	
	private String inReplayTo;
	
	private long replyBy;
	
	public ACLMessage() {}

	public ACLMessage(Performative performative, AIDS sender, AIDS[] receivers, AIDS replyTo, String content,
			Object contentObject, HashMap<String, Object> userArgs, String language, String encoding, String ontology,
			String protocol, String conversationId, String replayWith, String inReplayTo, long replyBy) {
		super();
		this.performative = performative;
		this.sender = sender;
		this.receivers = receivers;
		this.replyTo = replyTo;
		this.content = content;
		this.contentObject = contentObject;
		this.userArgs = userArgs;
		this.language = language;
		this.encoding = encoding;
		this.ontology = ontology;
		this.protocol = protocol;
		this.conversationId = conversationId;
		this.replayWith = replayWith;
		this.inReplayTo = inReplayTo;
		this.replyBy = replyBy;
	}

	public Performative getPerformative() {
		return performative;
	}

	public void setPerformative(Performative performative) {
		this.performative = performative;
	}

	public AIDS getSender() {
		return sender;
	}

	public void setSender(AIDS sender) {
		this.sender = sender;
	}

	public AIDS[] getReceivers() {
		return receivers;
	}

	public void setReceivers(AIDS[] receivers) {
		this.receivers = receivers;
	}

	public AIDS getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(AIDS replyTo) {
		this.replyTo = replyTo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getContentObject() {
		return contentObject;
	}

	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
	}

	public HashMap<String, Object> getUserArgs() {
		return userArgs;
	}

	public void setUserArgs(HashMap<String, Object> userArgs) {
		this.userArgs = userArgs;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getReplayWith() {
		return replayWith;
	}

	public void setReplayWith(String replayWith) {
		this.replayWith = replayWith;
	}

	public String getInReplayTo() {
		return inReplayTo;
	}

	public void setInReplayTo(String inReplayTo) {
		this.inReplayTo = inReplayTo;
	}

	public long getReplyBy() {
		return replyBy;
	}

	public void setReplyBy(long replyBy) {
		this.replyBy = replyBy;
	}
	
	
	
}
