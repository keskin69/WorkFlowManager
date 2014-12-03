package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import manager.NodeDefinition;


import util.FlowMessage;
import util.SocketQueue;
import util.Util;
import data.WorkData;

public abstract class ANode extends Thread implements INode {
	protected SocketQueue queue = null;
	private MulticastSocket socket = null;
	private static InetAddress group = null;
	private static String managerName = null;
	protected NodeDefinition node = null;
	public WorkData data = null;


	@SuppressWarnings("unused")
	public void run() {
		if (Util.DEBUG_LEVEL <= 0) {
			System.out.println("Node " + this.getClass().getName() + " is up.");
		}

		queue = new SocketQueue(this.getClass().getName(),
				Util.COMMUNICATION_PORT, Util.GROUP_NAME);
		queue.start();
		boolean running = true;

		while (running) {
			if (queue.size() > 0) {
				FlowMessage msg = queue.get(0);
				WorkData d = (WorkData) msg.data;

				if (Util.DEBUG_LEVEL <= 0) {
					System.out.println(this.getClass().getName()
							+ " received a message:" + d.messageType);
				}

				switch (d.messageType) {
				case QUIT:
					queue.quit();
					running = false;
					//System.out.println("Quiting" + node.name);
					quit();
					break;
				case HEARTH_BEATH:
					managerName = msg.source;
					d.messageType = Util.MessageType.OK;
					sendResponse(d);
					queue.remove(msg);
					break;
				default:
					work(msg.data);
					queue.remove(msg);
				}
			}
		}

		try {
			socket.leaveGroup(group);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void quit() {
	}

	public void sendResponse(WorkData data) {
		// return the processed data to manager
		System.out.println(this.getClass().getName());
		Util.sendMessage(socket, group, this.getClass().getName(), managerName,
				data);
	}

	public void init(NodeDefinition node) {
		this.node = node;
		
		try {
			group = InetAddress.getByName(Util.GROUP_NAME);
			socket = new MulticastSocket(Util.COMMUNICATION_PORT);
			group = InetAddress.getByName(Util.GROUP_NAME);
			socket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void work(WorkData data);

}
