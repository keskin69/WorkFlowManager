package test;


import data.Characteristic;
import data.WorkData;
import node.NodeGUI;

public class Node2 extends NodeGUI {

	public void work(WorkData data) {
		this.data = data;
		dataPending = true;

		initGUI();
		Characteristic c = (Characteristic) data.get("Log");
		c.value = c.value.concat("+" + this.getClass().getName());

		System.out.println(data.getValue("Log"));
	}

}
