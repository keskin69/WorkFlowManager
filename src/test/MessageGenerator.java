package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import manager.FlowDefinition;
import manager.FlowManager;
import util.SocketQueue;
import util.Util;
import data.Characteristic;

public class MessageGenerator extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8608542369883001111L;
	protected SocketQueue queue = null;
	private MulticastSocket socket = null;
	private static InetAddress group = null;
	private static String managerName = null;
	private static String defFile = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MessageGenerator();
		defFile = args[0];
	}

	public MessageGenerator() {
		// init gui
		super("Message Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pnlButtons = new JPanel();
		getContentPane().add(pnlButtons, BorderLayout.PAGE_END);

		JButton btnInit = new JButton("Initiate");
		pnlButtons.add(btnInit, BorderLayout.CENTER);
		btnInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initiate(defFile);
			}
		});

		JButton btnSave = new JButton("Send");
		pnlButtons.add(btnSave, BorderLayout.CENTER);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		JButton btnQuit = new JButton("Quit");
		pnlButtons.add(btnQuit, BorderLayout.CENTER);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// drop a test data to flow diagram
				MyData data = new MyData();
				data.messageType = Util.MessageType.QUIT;
				Util.sendMessage(socket, group, "", "*", data);
			}
		});

		try {
			group = InetAddress.getByName(Util.GROUP_NAME);
			socket = new MulticastSocket(Util.COMMUNICATION_PORT);
			group = InetAddress.getByName(Util.GROUP_NAME);
			socket.joinGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pack();
		setVisible(true);
	}

	private void initiate(String defFile) {
		FlowDefinition flowDefinition = new FlowDefinition(defFile);
		managerName = flowDefinition.manager;

		// activate flow manager
		FlowManager manager;
		try {
			manager = (FlowManager) Class.forName(managerName).newInstance();
			manager.init(flowDefinition);
			manager.start();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send() {
		// drop a test data to flow diagram
		MyData data = new MyData();
		Characteristic att = new Characteristic();
		att.name = "Log";
		att.value = "";
		att.type = "String";
		data.put(att);
		data.messageType = Util.MessageType.BEGIN;
		Util.sendMessage(socket, group, "", managerName, data);
	}
}
