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
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import de.norvos.observers.Notifiable;

@SuppressWarnings("serial")
public class MessageList extends JPanel implements Notifiable {
	private final JPanel mainList;
	private final JScrollPane scrollPane;

	public MessageList() {
		setLayout(new BorderLayout());

		mainList = new JPanel();
		mainList.setLayout(new BoxLayout(mainList, BoxLayout.Y_AXIS));

		mainList.setOpaque(false);
		mainList.setBackground(Color.WHITE);

		scrollPane = new JScrollPane(mainList);
		scrollPane.setOpaque(false);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(scrollPane);
		setOpaque(false);
	}

	@Override
	public void notify(final String event, final Object notificationData) {
		EventQueue.invokeLater(() -> {
			final JPanel wholeWidthPanel = new JPanel();
			final JPanel flowLeftPanel = new JPanel();

			wholeWidthPanel.setOpaque(false);
			flowLeftPanel.setOpaque(false);

			flowLeftPanel.setLayout(new BoxLayout(flowLeftPanel, BoxLayout.X_AXIS));
			wholeWidthPanel.setLayout(new BorderLayout());

			if (event.equals("messageSent")) {
				final MessageEntry entry1 = new MessageEntry((String) notificationData, true);
				flowLeftPanel.add(entry1);
				validate();
			} else if (event.equals("messageReceived")) {
				final MessageEntry entry2 = new MessageEntry((String) notificationData, false);
				flowLeftPanel.add(entry2);
				validate();
			} else {
				System.out.println("Unknown event: " + event);
			}
			flowLeftPanel.setMaximumSize(new Dimension(flowLeftPanel.getMaximumSize().width, flowLeftPanel
					.getPreferredSize().height));

			wholeWidthPanel.add(flowLeftPanel);

			wholeWidthPanel.setMaximumSize(new Dimension(wholeWidthPanel.getMaximumSize().width, wholeWidthPanel
					.getPreferredSize().height));

			mainList.add(wholeWidthPanel);
			mainList.revalidate();

			EventQueue.invokeLater(() -> {
				final JScrollBar vertical = scrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
			});

		});
	}

}
