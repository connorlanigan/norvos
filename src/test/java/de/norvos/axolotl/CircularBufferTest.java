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
package de.norvos.axolotl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.norvos.utils.CircularBuffer;

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
