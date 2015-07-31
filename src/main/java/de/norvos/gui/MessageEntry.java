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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessageEntry extends JPanel {
	final static private int border = 2;

	private final boolean sent;

	public MessageEntry(final String message, final boolean sent) {
		super();
		this.sent = sent;

		setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
		final FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		final JLabel messageLabel = new JLabel(processText(message));
		messageLabel.setOpaque(false);

		add(messageLabel);
		setOpaque(false);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color baseColor;
		if (sent) {
			baseColor = Color.WHITE;
		} else {
			baseColor = Color.decode("0xFFCB00");
		}
		g2.setStroke(new BasicStroke(4));

		final int abstand = 1;

		g2.setColor(baseColor.darker());
		g2.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 5, 5);
		g.fillRect(3, 3, getWidth() - 7, getHeight() - 7);

		g2.setColor(baseColor);
		g2.drawRoundRect(3 + abstand, 3 + abstand, getWidth() - (7 + (2 * abstand)), getHeight() - (7 + (2 * abstand)),
				5, 5);

		g.fillRect(3 + abstand, 3 + abstand, getWidth() - (7 + (2 * abstand)), getHeight() - (7 + (2 * abstand)));
	}

	private static String processText(String text) {
		// TODO replace URLs with links
		text = "<html>" + text.replace("\n", "<br>");
		return text;
	}
}
