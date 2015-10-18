package de.norvos.eventbus.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.eventbus.Event;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.EventBusListener;
import de.norvos.eventbus.events.MessageSentEvent;
import de.norvos.gui.components.MessageList;

public class MessageSentHandler implements EventBusListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSentHandler.class);

	public MessageSentHandler() {
		EventBus.register(MessageSentEvent.class, this);
	}

	@Override
	public void update(final Event event) {
		if (!(event instanceof MessageSentEvent)) {
			LOGGER.warn("Received a {} event for the MessageSentHandler.", event.getClass());
			return;
		}
		final MessageSentEvent messageSentEvent = (MessageSentEvent) event;

		MessageList.getActiveInstance().update(messageSentEvent);
	}
}
