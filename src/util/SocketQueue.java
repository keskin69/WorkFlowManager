package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SocketQueue extends Thread {
	private Vector<FlowMessage> queue = null;
	private MulticastSocket socket = null;
	private InetAddress group = null;
	private String clientName = null;
	private boolean running = true;

	public SocketQueue(String clientName, int port, String groupName) {
		try {
			this.socket = new MulticastSocket(port);
			this.group = InetAddress.getByName(groupName);
			this.clientName = clientName;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queue = new Vector<FlowMessage>();
	}


	@SuppressWarnings("unused")
	public void run() {
		try {
			socket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatagramPacket packet;
		if (Util.DEBUG_LEVEL <= 0) {
			System.out.println("Listening for " + clientName);
		}

		while (running) {
			byte[] buf = new byte[65535];
			packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);

				ByteArrayInputStream b_in = new ByteArrayInputStream(
						packet.getData());
				ObjectInputStream o_in = new ObjectInputStream(b_in);
				FlowMessage msg = (FlowMessage) o_in.readObject();

				if (msg.target.equals(clientName) || msg.target.equals("*")) {
					if (Util.DEBUG_LEVEL <= 0) {
						System.out.println(clientName + ":Work received from "
								+ msg.source);
					}

					queue.add(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public void remove(FlowMessage msg) {
		queue.remove(msg);
	}

	public int size() {
		return queue.size();
	}

	public FlowMessage get(int index) {
		return queue.get(index);
	}

	public void quit() {
		running = false;
	}

	public void add(FlowMessage msg) {
		queue.add(msg);
	}
}
