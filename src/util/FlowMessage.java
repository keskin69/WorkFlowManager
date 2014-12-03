package util;

import java.io.Serializable;

import data.WorkData;

public class FlowMessage implements IMessage, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6299010123048368213L;
	
	public String target;
	public String source;
	public WorkData data;
	
	public FlowMessage(String source, String target, WorkData data) {
		this.source = source;
		this.target = target;
		this.data = data;
	}

}
