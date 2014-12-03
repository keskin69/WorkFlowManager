package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import data.WorkData;

public class Util {
	public static final int COMMUNICATION_PORT = 6789;
	public static final String GROUP_NAME = "228.5.6.7";
	public static enum DebugLevel {DEBUG, INFO};
	public static final int DEBUG_LEVEL = 1;
	public static enum MessageType {
		NONE, BEGIN, YES, NO, HEARTH_BEATH, OK, QUIT
	};

	public static byte[] getBytes(Object obj) throws java.io.IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}

	public static void sendMessage(MulticastSocket socket, InetAddress group,
			String source, String target, WorkData data) {
		ByteArrayOutputStream b_out = new ByteArrayOutputStream();
		ObjectOutputStream o_out;

		try {
			FlowMessage msg = new FlowMessage(source, target, data);

			o_out = new ObjectOutputStream(b_out);
			o_out.writeObject(msg);
			byte[] b = b_out.toByteArray();
			DatagramPacket packet = new DatagramPacket(b, b.length, group,
					Util.COMMUNICATION_PORT);
			socket.send(packet);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
