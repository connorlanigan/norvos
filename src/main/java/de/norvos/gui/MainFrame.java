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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (final Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(() -> {
			try {
				final MainFrame frame = new MainFrame();
				frame.setVisible(true);
				frame.t.display();
				frame.setLocationRelativeTo(null);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	private final JPanel contentPane;
	int i = 0;

	private final TestDialog t;

	/**
	 * Create the frame.
	 */

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		getRootPane().setBackground(Color.WHITE);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);

		final MessageList list = new MessageList();
		getContentPane().add(list);

		t = new TestDialog();
		t.register(list, "messageSent");
		t.register(list, "messageReceived");

		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");

		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");

		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");

		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");

		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");
		list.notify("messageSent", "Hallo!");
		list.notify("messageReceived", "Hallo Du!");

	}
}
