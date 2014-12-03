package node;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.Characteristic;

public abstract class NodeGUI extends ANode {
	protected boolean dataPending = false;
	private JFrame frame = null;
	private JPanel pnlEntry = null;
	

	public void initGUI() {
		if (frame == null) {

			JLabel lbl = null;
			JTextField txtField = null;
			JTextArea txtArea = null;

			frame = new JFrame(this.node.title);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			GridLayout gridLayout = new GridLayout(0, 2);
			gridLayout.setHgap(5);
			gridLayout.setVgap(5);

			JPanel pnlInfo = new JPanel();
			frame.getContentPane().add(pnlInfo, BorderLayout.PAGE_START);
			pnlInfo.setLayout(gridLayout);

			Characteristic attData = null;
			for (String key : data.keySet()) {
				attData = data.get(key);

				if (checkIfDisplayOnly(attData.name)) {

					lbl = new JLabel(key + ":");
					pnlInfo.add(lbl);

					if (attData.type.equals("String")) {
						txtField = new JTextField(attData.value);
						txtField.setEnabled(false);
						pnlInfo.add(txtField);
					} else if (attData.type.equals("TextArea")) {
						txtArea = new JTextArea(attData.value);
						txtArea.setEnabled(false);
						pnlInfo.add(txtArea);
					}
				}
			}

			pnlEntry = new JPanel();
			frame.getContentPane().add(pnlEntry, BorderLayout.CENTER);
			pnlEntry.setLayout(gridLayout);

			JPanel pnlButtons = new JPanel();
			frame.getContentPane().add(pnlButtons, BorderLayout.PAGE_END);
			JButton btnSave = new JButton("Kaydet");
			pnlButtons.add(btnSave, BorderLayout.CENTER);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (dataPending) {
						save();
						frame.setVisible(false);
					}
				}
			});

			for (Characteristic att : node.attributes) {
				if (!att.displayOnly) {
					lbl = new JLabel(att.name + ":");
					pnlEntry.add(lbl);

					if (att.type.equals("String")) {
						txtField = new JTextField(att.value);
						txtField.setName(att.name);
						pnlEntry.add(txtField);
					} else if (att.type.equals("TextArea")) {
						txtArea = new JTextArea(att.value);
						txtArea.setName(att.name);
						pnlEntry.add(txtArea);
					}
				}
			}

			frame.pack();
		}

		frame.setVisible(true);

	}

	private boolean checkIfDisplayOnly(String fldName) {
		for (Characteristic att : node.attributes) {
			if (att.name.equals(fldName)) {
				return att.displayOnly;
			}
		}

		return true;
	}

	public void quit() {
		if (frame != null) {
			frame.setVisible(false);
		}
	}

	public void save() {
		for (Characteristic att : node.attributes) {
			for (Component comp : pnlEntry.getComponents()) {
				if (comp instanceof JTextField) {
					if (att.name.equals(comp.getName())) {
						att.value = ((JTextField) comp).getText();

						if (att.mandatory && att.value.equals("")) {
							JOptionPane.showMessageDialog(frame, att.name
									+ " alanõ zorunlu olarak doldurulmadõr.",
									"Zorunlu Alan Kontrolu",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						data.put(att);

					}
				}
			}
		}

		dataPending = false;
		sendResponse(data);
	}
}
