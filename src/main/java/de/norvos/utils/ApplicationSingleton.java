package de.norvos.utils;

import java.net.InetAddress;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationSingleton {
	private static boolean alreadyLocked = false;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationSingleton.class);
	private static boolean portLockingFailed;

	synchronized public static void checkAndLock() {
		if (!alreadyLocked) {
			final Thread t = new Thread(() -> {
				try {
					final InetAddress localhost = InetAddress.getByName("127.0.0.1");

					@SuppressWarnings("resource") // this resource leak is
					final
					// intended: the socket
					// should only close when
					// the application closes.
					ServerSocket socket = new ServerSocket(Constants.SINGLETON_LOCK_PORT, 1, localhost);
					while (true) {
						socket.accept().close();
						LOGGER.info(
								"Someone has connected to the locking port (port {}). The connection has thus been reset.",
								Constants.SINGLETON_LOCK_PORT);
					}
				} catch (final Exception e) {
					portLockingFailed = true;
				}
			} , "Port Locking Thread");
			t.setDaemon(true);
			t.start();

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
				LOGGER.info(
						"Waiting for the port locking to happen has failed. It is not safe starting the application now.");
				Errors.stopApplication();
				throw new UnreachableCodeException();
			}

			if (portLockingFailed) {
				LOGGER.info("Attempted to start already running application.");
				Errors.stopApplication();
				throw new UnreachableCodeException();

			} else {
				LOGGER.debug("Successfully locked on port {}.", Constants.SINGLETON_LOCK_PORT);
				alreadyLocked = true;
			}
		}
	}

	private ApplicationSingleton() {
	}
}
