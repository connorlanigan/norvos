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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * A circular buffer is a circular, wrapped list. As soon as the buffer is
 * filled, every write access will delete the oldest item. <br>
 * Entries are identified by their ID, which is supplied when adding them to the
 * buffer.
 *
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 *
 * @param <T>
 *            Type of carried data
 */
public class CircularBuffer<T> {

	final private ArrayList<Entry<Integer, T>> array;
	private int pointer = 0;
	private int size = 0;

	/**
	 * Constructs a new circular buffer of the given size.
	 *
	 * @param size
	 *            the number of elements this buffer can contain.
	 */
	public CircularBuffer(final int size) {
		array = new ArrayList<Entry<Integer, T>>(size);
		this.size = size;
	}

	/**
	 * Adds an element to this buffer. If the buffer is full, the oldest element
	 * in it will be removed. The element can later be retrieved by the ID that
	 * is given here.
	 *
	 * @param id
	 *            numberical ID of the element
	 * @param obj
	 *            the element to store
	 */
	public void add(final int id, final T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}

		final Entry<Integer, T> entry = new AbstractMap.SimpleImmutableEntry<>(id, obj);

		if (array.size() > pointer) {
			array.set(pointer, entry);
		} else {
			array.add(entry);
		}
		pointer = (pointer + 1) % size;

	}

	/**
	 * Retrieves an element from the buffer. If multiple elements have the same
	 * ID, the oldest one will be returned.
	 *
	 * @param id
	 *            numerical ID, chosen at store-time.
	 * @return the stored element
	 */
	public T get(final int id) {
		for (int i = 0; i < array.size(); i++) {
			final Entry<Integer, T> obj = array.get((pointer + i) % array.size());
			if (obj.getKey().equals(id)) {
				return obj.getValue();
			}
		}
		return null;
	}

	/**
	 * Retrieves a list of all elements currently in the buffer. This list will
	 * be ordered from oldest to newest. The returned list is a copy of the
	 * internal state and thus can freely be modified.
	 *
	 * @return List containing all current elements
	 */
	public List<T> getAll() {
		final List<T> newList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			final Entry<Integer, T> obj = array.get((pointer + i) % array.size());
			newList.add(obj.getValue());
		}

		return newList;
	}

	/**
	 * Removes an element from the buffer. The freed space will not be reused
	 * before the time the element would have been deleted automatically. If
	 * multiple elements have the same ID, the oldest one will be removed.
	 *
	 * @param id
	 *            numerical ID of the element to delete
	 */
	public void remove(final int id) {
		int currentPointer = pointer;
		for (int i = 0; i < array.size(); i++) {
			currentPointer = (pointer + i) % array.size();
			final Entry<Integer, T> obj = array.get(currentPointer);
			if (obj.getKey().equals(id)) {
				array.remove(currentPointer);
			}
		}
	}
}
