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

public class MessageList extends JPanel implements Notifiable {
	private JPanel mainList;
	private JScrollPane scrollPane;

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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JPanel wholeWidthPanel = new JPanel();
				JPanel flowLeftPanel = new JPanel();

				wholeWidthPanel.setOpaque(false);
				flowLeftPanel.setOpaque(false);

				flowLeftPanel.setLayout(new BoxLayout(flowLeftPanel, BoxLayout.X_AXIS));
				wholeWidthPanel.setLayout(new BorderLayout());

				if (event.equals("messageSent")) {
					MessageEntry entry = new MessageEntry((String) notificationData, true);
					flowLeftPanel.add(entry);
					validate();
				} else if (event.equals("messageReceived")) {
					MessageEntry entry = new MessageEntry((String) notificationData, false);
					flowLeftPanel.add(entry);
					validate();
				} else {
					System.out.println("Unknown event: " + event);
				}
				flowLeftPanel.setMaximumSize(
						new Dimension(flowLeftPanel.getMaximumSize().width, flowLeftPanel.getPreferredSize().height));

				wholeWidthPanel.add(flowLeftPanel);

				wholeWidthPanel.setMaximumSize(new Dimension(wholeWidthPanel.getMaximumSize().width,
						wholeWidthPanel.getPreferredSize().height));

				mainList.add(wholeWidthPanel);
				mainList.revalidate();

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						JScrollBar vertical = scrollPane.getVerticalScrollBar();
						vertical.setValue(vertical.getMaximum());
					}
				});

			}
		});
	}

}
