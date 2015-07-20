package de.norvos.axolotl;

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
	public CircularBuffer(int size) {
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
	public void add(int id, T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}

		Entry<Integer, T> entry = new AbstractMap.SimpleImmutableEntry<>(id, obj);

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
	public T get(int id) {
		for (int i = 0; i < array.size(); i++) {
			Entry<Integer, T> obj = array.get((pointer + i) % array.size());
			if (obj.getKey().equals(id)) {
				return obj.getValue();
			}
		}
		return null;
	}

	/**
	 * Removes an element from the buffer. The freed space will not be reused
	 * before the time the element would have been deleted automatically. If
	 * multiple elements have the same ID, the oldest one will be removed.
	 * 
	 * @param id
	 *            numerical ID of the element to delete
	 */
	public void remove(int id) {
		int currentPointer = pointer;
		for (int i = 0; i < array.size(); i++) {
			currentPointer = (pointer + i) % array.size();
			Entry<Integer, T> obj = array.get(currentPointer);
			if (obj.getKey().equals(id)) {
				array.remove(currentPointer);
			}
		}
	}

	/**
	 * Retrieves a list of all elements currently in the buffer. This list will
	 * be ordered from oldest to newest. The returned list is a copy of the
	 * internal state and thus can freely be modified.
	 * 
	 * @return List containing all current elements
	 */
	public List<T> getAll() {
		List<T> newList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Entry<Integer, T> obj = array.get((pointer + i) % array.size());
			newList.add(obj.getValue());
		}

		return newList;
	}
}
