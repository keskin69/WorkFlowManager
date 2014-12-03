package test;

import data.Characteristic;
import data.WorkData;
import node.ANode;

public class Begin extends ANode {

	public void work(WorkData data) {
		Characteristic c = (Characteristic) data.get("Log");
		c.value = c.value.concat("+" + this.getClass().getName());

		System.out.println(data.getValue("Log"));
		
		sendResponse(data);
	}

}
