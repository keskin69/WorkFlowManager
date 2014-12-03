package data;

import java.io.Serializable;
//import java.util.Map;

public class Characteristic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -899160429596098338L;
	public String name;
	public String value = "";
	public String type;
	public boolean mandatory = false;
	public boolean displayOnly = true;
	//public Map<String, String> values = null;
	//public Map<String, String> labels = null;
	//public Map<String, String> tooltip = null;
}
