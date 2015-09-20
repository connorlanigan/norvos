package de.norvos.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * This Control loads the ResourceBundle as UTF-8. <br>
 * <br>
 * <i>Note: lines differing from the default implementation are marked with
 * "// Connor Lanigan:"</i>
 *
 * @author Connor Lanigan
 */
public class UTF8Control extends Control {
	@Override
	public ResourceBundle newBundle(final String baseName, final Locale locale, final String format,
			final ClassLoader loader, final boolean reload)
					throws IllegalAccessException, InstantiationException, IOException {
		final String bundleName = toBundleName(baseName, locale);
		ResourceBundle bundle = null;
		if (format.equals("java.class")) {
			try {
				@SuppressWarnings("unchecked")
				final Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader
						.loadClass(bundleName);

				// If the class isn't a ResourceBundle subclass, throw a
				// ClassCastException.
				if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
					bundle = bundleClass.newInstance();
				} else {
					throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
				}
			} catch (final ClassNotFoundException e) {
			}
		} else if (format.equals("java.properties")) {
			// Connor Lanigan: The check in toResourceName0 is skipped, as that
			// method is marked protected and the check is not needed in this
			// application. Thus, toResourceName is used instead.
			final String resourceName = toResourceName(bundleName, "properties");
			if (resourceName == null) {
				return bundle;
			}
			final ClassLoader classLoader = loader;
			final boolean reloadFlag = reload;
			InputStream stream = null;
			try {
				stream = AccessController.doPrivileged((PrivilegedExceptionAction<InputStream>) () -> {
					InputStream is = null;
					if (reloadFlag) {
						final URL url = classLoader.getResource(resourceName);
						if (url != null) {
							final URLConnection connection = url.openConnection();
							if (connection != null) {
								// Disable caches to get fresh data for
								// reloading.
								connection.setUseCaches(false);
								is = connection.getInputStream();
							}
						}
					} else {
						is = classLoader.getResourceAsStream(resourceName);
					}
					return is;
				});
			} catch (final PrivilegedActionException e) {
				throw (IOException) e.getException();
			}
			if (stream != null) {
				try {
					// Connor Lanigan: This line is changed to read the resource
					// bundle as UTF-8.
					bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
				} finally {
					stream.close();
				}
			}
		} else {
			throw new IllegalArgumentException("unknown format: " + format);
		}
		return bundle;
	}
}