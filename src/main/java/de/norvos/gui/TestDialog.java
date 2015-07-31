/*******************************************************************************
 * Copyright (C) 2015 Connor Lanigan (email: dev@connorlanigan.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.norvos.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

public class TestDialog extends JDialog implements Observable {

	NotificatorMap map = new NotificatorMap();

	/**
	 * Create the dialog.
	 */
	public TestDialog() {
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Emulate messages here");
		setAlwaysOnTop(true);
		setBounds(100, 100, 329, 148);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		final JTextArea inputText = new JTextArea();
		getContentPane().add(inputText);

		final JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JButton btnReceive = new JButton("Receive");
		btnReceive.addActionListener(arg0 -> {
			map.notify("messageReceived", inputText.getText());
			inputText.setText("");
		});
		panel.add(btnReceive);

		final JButton btnSend = new JButton("Send");
		btnSend.addActionListener(arg0 -> {
			map.notify("messageSent", inputText.getText());
			inputText.setText("");
		});
		panel.add(btnSend);

	}

	public void display() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void register(final Notifiable n, final String event) {
		map.register(event, n);
	}

	@Override
	public void unregister(final Notifiable n) {
		map.unregister(n);
	}
}
