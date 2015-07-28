package de.norvos.gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;

public class TestDialog extends JDialog implements Observable{

	NotificatorMap map =  new NotificatorMap();
	
	
	
	public void display(){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Create the dialog.
	 */
	public TestDialog() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Emulate messages here");
		setAlwaysOnTop(true);
		setBounds(100, 100, 329, 148);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		final JTextArea inputText = new JTextArea();
		getContentPane().add(inputText);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JButton btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				map.notify("messageReceived", inputText.getText());
				inputText.setText("");
			}
		});
		panel.add(btnReceive);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				map.notify("messageSent", inputText.getText());
				inputText.setText("");
			}
		});
		panel.add(btnSend);

	}

	@Override
	public void register(Notifiable n, String event) {
		map.register(event, n);
	}

	@Override
	public void unregister(Notifiable n) {
		map.unregister(n);
	}
}
