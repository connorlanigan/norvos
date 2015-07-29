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
package de.norvos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 * A Set of byte-arrays that is backed by files on disk.
 * 
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>} *
 */
public class PersistentSet {

	/**
	 * The directory in which to operate.
	 */
	private Path directory;

	/**
	 * Determines the length of the filenames.
	 */
	private int sizeExponent;

	/**
	 * Initializes the PersistentSet in a given directory.
	 * 
	 * @param directory
	 *            the directory in which to create this Set.
	 * @param nameLength
	 *            the length of the filenames to use. This limits the maximum
	 *            amount of entries. The total amount of entries is
	 *            36^nameLength.
	 * @throws AccessDeniedException
	 *             if the directory can not be accessed.
	 */
	public PersistentSet(Path directory, int nameLength) throws AccessDeniedException {
		this.directory = directory;
		directory.toFile().mkdirs();
		if (!directory.toFile().exists()) {
			throw new AccessDeniedException("The directory [" + directory + "] could not be created.");
		}
	}

	/**
	 * Returns if this byte-array already exists in this set.
	 * 
	 * @param bytes
	 *            the byte-array to check
	 * @return true if and only if this byte-array is contained in the set
	 */
	public boolean contains(byte[] bytes) {
		for (File file : directory.toFile().listFiles()) {
			try {
				try (FileInputStream inputStream = new FileInputStream(file)) {
					if (checkEqual(bytes, inputStream)) {
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		return false;
	}

	/**
	 * Checks if the InputStream returns exactly the expected byte-array.
	 * 
	 * @param bytes
	 *            the expected byte-array.
	 * @param inputStream
	 *            the stream to check.
	 * @return true if and only if the stream returned all bytes of the array.
	 * @throws IOException
	 *             if a read error occurs
	 */
	private boolean checkEqual(byte[] bytes, InputStream inputStream) throws IOException {
		int singleByte;
		int i = 0;
		while ((singleByte = inputStream.read()) != -1) {
			if (bytes[i] != singleByte) {
				return false;
			}
			i++;
		}
		return true;
	}

	/**
	 * Adds a byte-array to this set. When the method returns, the array has
	 * been written to disk completely. No file is created if this method fails.
	 * 
	 * @param bytes
	 *            the byte-array to store.
	 * @throws IOException
	 *             if the array could not be stored. No file has been created in
	 *             this case.
	 */
	public void add(byte[] bytes) throws IOException {
		File storeFile;
		do {
			storeFile = directory.resolve(RandomUtils.randomAlphanumerical(sizeExponent)).toFile();
		} while (storeFile.exists());

		File tempFile = File.createTempFile(directory.getFileName().toString(), "persistentSetFile");
		try (FileOutputStream outStream = new FileOutputStream(tempFile)) {
			outStream.write(bytes);
			Files.move(tempFile.toPath(), storeFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * Removes a byte-array from this set.
	 * 
	 * @param bytes
	 *            the byte-array to remove.
	 */
	public void remove(byte[] bytes) {
		for (File file : directory.toFile().listFiles()) {
			try {
				try (FileInputStream inputStream = new FileInputStream(file)) {
					if (checkEqual(bytes, inputStream)) {
						Files.delete(file.toPath());
						return;
					}
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Returns a set of all byte-arrays currently contained in the set. If one
	 * of the elements is removed from the set, this list may get corrupted.
	 * 
	 * @return Set of all byte-arrays (in unloaded form)
	 */
	public Set<PersistentBytes> getAll() {
		File[] files = directory.toFile().listFiles();
		Set<PersistentBytes> list = new HashSet<PersistentBytes>(files.length);
		for (File file : files) {
			list.add(new PersistentBytes(file));
		}
		return list;
	}

	/**
	 * A class that represents a not-yet-loaded byte-array backed by a file.
	 * This helps save memory. The byte-array is only loaded when the
	 * load()-method is called, but it will not be cached.
	 * 
	 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
	 *
	 */
	public class PersistentBytes {
		private File file;

		/**
		 * Creates a new PersistentBytes-object from the given file.
		 * 
		 * @param file
		 */
		public PersistentBytes(File file) {
			this.file = file;
		}

		/**
		 * Loads the byte-array into memory and returns it. The content is not
		 * cached, this means that on every call this method will return the
		 * current content of the backing file.
		 * 
		 * @return the represented byte-array.
		 * @throws IOException
		 *             if the bytes could not be read
		 */
		public byte[] load() throws IOException {
			byte[] bFile = new byte[(int) file.length()];
			try (FileInputStream fileInputStream = new FileInputStream(file)) {
				fileInputStream.read(bFile);
				fileInputStream.close();
				return bFile;
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof PersistentBytes) {
				PersistentBytes other = (PersistentBytes) o;
				if (other.file.equals(this.file)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Replaces the byte-array on disk with the given byte-array.
		 * 
		 * @param bytes
		 *            the new bytes to store.
		 * @throws IOException
		 *             if the file could not be written.
		 */
		public void replace(byte[] bytes) throws IOException {
			File tempFile = File.createTempFile(file.toString(), "replaceTemp");
			try (FileOutputStream outStream = new FileOutputStream(tempFile)) {
				outStream.write(bytes);
				Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
}
