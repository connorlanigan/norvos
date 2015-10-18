package de.norvos.persistence;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttachmentStore {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentStore.class);

	public static File getAttachment(final long attachmentId) {
		if (attachmentId == 0) {
			return null;
		}
		return null; // TODO
	}
}
