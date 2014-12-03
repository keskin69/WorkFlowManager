package manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import util.FlowMessage;
import util.SocketQueue;
import util.Util;
import data.WorkData;

public class FlowManager extends Thread  {
	protected MulticastSocket socket = null;
	protected static InetAddress group = null;
	protected SocketQueue queue = null;
	protected FlowDefinition flowDefinition = null;

	public void init(FlowDefinition flowDefinition) {
		try {
			this.flowDefinition = flowDefinition;
			socket = new MulticastSocket(Util.COMMUNICATION_PORT);
			group = InetAddress.getByName(Util.GROUP_NAME);
			socket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		queue = new SocketQueue(this.getClass().getName(),
				Util.COMMUNICATION_PORT, Util.GROUP_NAME);
		queue.start();

		checkHeartBeat();

		while (true) {
			if (queue.size() > 0) {
				FlowMessage msg = queue.get(0);
				manageFlow(msg);
				queue.remove(msg);
			}
		}
	}

	@SuppressWarnings("unused")
	private final void checkHeartBeat() {
		// check if all the workers are up

		for (NodeDefinition node : flowDefinition) {
			node.healthy = false;
		}

		WorkData data = new WorkData();

		data.messageType = Util.MessageType.HEARTH_BEATH;
		Util.sendMessage(socket, group, this.getClass().getName(), "*", data);

		int cnt = flowDefinition.size();
		while (cnt > 0) {
			if (queue.size() > 0) {
				FlowMessage msg = queue.get(0);
				data = (WorkData) msg.data;
				queue.remove(msg);
				
				if (data.messageType == Util.MessageType.OK) {
					// worker is healthy
					NodeDefinition node = flowDefinition
							.findNodeByClass(msg.source);
					node.healthy = true;

					if (Util.DEBUG_LEVEL <= 0) {
						System.out.println(node.name + " is healthy");
					}

					cnt--;
				} else if (data.messageType == Util.MessageType.HEARTH_BEATH) {
					// ignore health check message for manager
				} else
					// push the other type of data to the end of the queue
					queue.add(msg);
			}
		}

		if (Util.DEBUG_LEVEL <= 1) {
			System.out.println("Health check completed");
		}
	}

	@SuppressWarnings("unused")
	public void manageFlow(FlowMessage msg) {
		WorkData data = (WorkData) msg.data;

		if (Util.DEBUG_LEVEL <= 0) {
			System.out
					.println("Manager received a message:" + data.messageType);
		}

		switch (data.messageType) {
		case BEGIN:
			// send to begin node
			((WorkData) data).messageType = Util.MessageType.NONE;
			Util.sendMessage(socket, group, this.getClass().getName(),
					flowDefinition.getStart().className, msg.data);
			break;
		case HEARTH_BEATH:
			break;
		case OK:
			break;
		case QUIT:
			queue.quit();
			break;
		default:
			NodeDefinition nextNode = flowDefinition.getNextNode(msg.source,
					data.messageType);
			if (nextNode != null) {
				((WorkData) data).messageType = Util.MessageType.NONE;
				Util.sendMessage(socket, group, this.getClass().getName(),
						nextNode.className, msg.data);
			}
		}
	}

	public void add(FlowMessage msg) {
		queue.add(msg);
	}
}
