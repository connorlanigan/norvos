package de.norvos.axolotl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.norvos.axolotl.CircularBuffer;

public class CircularBufferTest {
	CircularBuffer<String> buffer;

	@Before
	public void setUp() throws Exception {
		buffer = new CircularBuffer<>(3);
	}

	@Test
	public void addGet() {
		assertTrue(buffer.getAll().isEmpty());
		buffer.add(1, "One");
		buffer.add(2, "Two");

		assertEquals("One", buffer.get(1));
		assertEquals("Two", buffer.get(2));
		assertEquals(null, buffer.get(3));

		buffer.add(3, "Three");
		buffer.add(4, "Four");

		assertEquals(null, buffer.get(1));
		assertEquals("Two", buffer.get(2));
		assertEquals("Three", buffer.get(3));
		assertEquals("Four", buffer.get(4));

	}

	@Test(expected = NullPointerException.class)
	public void addNull() {
		buffer.add(1, null);
	}

	@Test
	public void remove() {
		buffer.add(1, "One");
		buffer.remove(1);
		assertEquals(null, buffer.get(1));
	}

}
