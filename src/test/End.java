package test;

import node.ANode;
import data.Characteristic;
import data.WorkData;

public class End extends ANode {

	public void work(WorkData data) {
		Characteristic c = (Characteristic) data.get("Log");
		c.value = c.value.concat("+" + this.getClass().getName());

		System.out.println(data.getValue("Log"));
		
		sendResponse(data);
	}

}
