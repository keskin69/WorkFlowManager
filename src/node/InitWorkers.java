package node;

import manager.FlowDefinition;
import manager.NodeDefinition;

public class InitWorkers {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new InitWorkers(args[0]);

	}

	public InitWorkers(String defFile) {
		FlowDefinition flowDefinition = new FlowDefinition(defFile);

		// activate all nodes
		for (NodeDefinition node : flowDefinition) {
			// check if worker implemented here
			try {
				ANode worker = (ANode) Class.forName(node.className)
						.newInstance();
				worker.init(node);
				worker.start();
			} catch (ClassNotFoundException ex) {

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
