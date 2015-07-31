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
package de.norvos.persistence;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import de.norvos.utils.RandomUtils;

public class PersistentSetTest {

	PersistentSet set;

	@Before
	public void before() throws AccessDeniedException {
		set =
				new PersistentSet(Paths.get(System.getProperty("java.io.tmpdir")).resolve("NorvosTestJUnit")
						.resolve("test_" + RandomUtils.randomAlphanumerical(6)), 4);
	}

	@Test
	public void testAddContainsRemoveGetall() throws IOException {
		final byte[] one = new byte[] { 3, 25, 12 };
		final byte[] one_copy = new byte[] { 3, 25, 12 };
		final byte[] two = new byte[] { 4, 14, 54 };
		set.add(one);
		set.add(two);

		assertFalse(set.contains(new byte[] { 3, 16, 28 }));

		assertTrue(set.contains(one));
		assertTrue(set.contains(one_copy));
		assertTrue(set.contains(two));

		set.remove(one);
		assertFalse(set.contains(one));
		assertFalse(set.contains(one_copy));
		assertTrue(set.contains(two));

		set.add(one);
		set.remove(one_copy);

		assertFalse(set.contains(one));
		assertFalse(set.contains(one_copy));
		assertTrue(set.contains(two));

		for (final PersistentBytes bytes : set.getAll()) {
			assertTrue(set.contains(bytes.load()));
		}

	}

	@Test
	public void testPersistentBytes() throws IOException {
		final byte[] one = new byte[] { 3, 25, 12 };
		set.add(one);

		for (final PersistentBytes bytes : set.getAll()) {
			bytes.equals(bytes);
			assertArrayEquals(new byte[] { 3, 25, 12 }, bytes.load());
			bytes.replace(new byte[] { 3, 3, 3 });
			assertArrayEquals(new byte[] { 3, 3, 3 }, bytes.load());
		}
	}

}
