package org.opendolphin.swingexample;

import org.opendolphin.core.client.ClientDolphin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationFrame {
	private final ClientDolphin clientDolphin;

	public ApplicationFrame(final ClientDolphin clientDolphin) {
		//To change body of created methods use File | Settings | File Templates.
		this.clientDolphin = clientDolphin;
	}

	public JFrame newComponent() {
		JFrame result = new JFrame("Hello Swing");
		result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button = new JButton("Click Me");

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("button clicked");
			}
		};

		button.addActionListener(actionListener);

		result.add(button, BorderLayout.SOUTH);
		result.setSize(300, 100);
		result.setVisible(true);
		return result;
	}
}
