package org.opendolphin.swingexample;

import net.miginfocom.swing.MigLayout;
import org.opendolphin.core.client.ClientDolphin;
import org.opendolphin.core.client.ClientPresentationModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ApplicationFrame {
	public static final String COLOR = "color";
	public static final String BLUE = "blue";
	public static final String YELLOW = "yellow";
	public static final String GREEN = "green";
	private final ClientPresentationModel pm1;

	public ApplicationFrame(final ClientDolphin clientDolphin) {
		pm1 = clientDolphin.presentationModel("pm1", Arrays.asList(COLOR));
	}

	public JFrame newComponent() {

		JFrame result = new JFrame("OpenDolphin Swing Example");

		MigLayout ml = new MigLayout(
			"insets 10, wrap",
			"[fill][]"
		);
		result.setLayout(ml);

		result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JTextField tf = new JTextField();
		JLabel label = new JLabel("label");

		JButton setPMbutton = new JButton("green -> PM");
		setPMbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pm1.getAt(COLOR).setValue(GREEN);
			}
		});


		JButton setTfTextbutton = new JButton("blue -> TF");
		setTfTextbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tf.setText(BLUE);
			}
		});

		SwingBinder.bind(COLOR).of(pm1).to("text").of(label);

		SwingBinder.bind("text").of(tf).to(COLOR).of(pm1);
		SwingBinder.bind(COLOR).of(pm1).to("text").of(tf);

		pm1.getAt(COLOR).setValue(YELLOW);

		result.add(tf);
		result.add(label);
		result.add(setPMbutton);
		result.add(setTfTextbutton);
		result.setSize(300, 100);
		result.setVisible(true);
		return result;
	}
}
