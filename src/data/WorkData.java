package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import util.Util.MessageType;

public class WorkData extends HashMap<String, Characteristic> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2015856810075205704L;
	public long uuid;
	public MessageType messageType;
	
	
	public String getValue(String str) {
		return get(str).value;
	}
	
	public void put(Characteristic att) {
		this.put(att.name, att);
	}
	
	public WorkData() {
		 uuid = UUID.randomUUID().getMostSignificantBits();
	}
}
