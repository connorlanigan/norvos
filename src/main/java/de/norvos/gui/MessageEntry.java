package de.norvos.gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.border.BevelBorder;

public class MessageEntry extends JPanel {
	final static private int border = 2;
	
	private boolean sent;
	
	public MessageEntry(String message, boolean sent){
		super();
		this.sent = sent;
		
		setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		
		JLabel messageLabel = new JLabel(processText(message));
		messageLabel.setOpaque(false);
		
		add(messageLabel);
		setOpaque(false);
	}
	
	private String processText(String text){
		//TODO replace URLs with links
		text = "<html>"+text.replace("\n", "<br>");
		return text;
	}
	
	
	@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		Color baseColor;
		if(sent){
			baseColor = Color.WHITE;
		}else{
			baseColor = Color.decode("0xFFCB00");
		}
		g2.setStroke(new BasicStroke(4));
		
		int abstand = 1;
		
		g2.setColor(baseColor.darker());
		g2.drawRoundRect(3, 3, getWidth()-7, getHeight()-7, 5, 5);
		g.fillRect(3, 3, getWidth()-7, getHeight()-7);
		
		
		g2.setColor(baseColor);
		g2.drawRoundRect(3+abstand, 3+abstand, getWidth()-(7+(2*abstand)), getHeight()-(7+(2*abstand)), 5, 5);
		
		g.fillRect(3+abstand, 3+abstand, getWidth()-(7+(2*abstand)), getHeight()-(7+(2*abstand)));
	}
}
