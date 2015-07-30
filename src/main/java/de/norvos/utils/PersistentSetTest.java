package de.norvos.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import de.norvos.utils.PersistentSet.PersistentBytes;

public class PersistentSetTest {

	PersistentSet set;
	
	@Before
	public void before() throws AccessDeniedException{
		set = new PersistentSet(Paths.get(System.getProperty("java.io.tmpdir")).resolve("NorvosTestJUnit").resolve("test_"+RandomUtils.randomAlphanumerical(6)), 4);
	}
	

	@Test
	public void testAddContainsRemoveGetall() throws IOException {
		byte[] one = new byte[]{3, 25, 12};
		byte[] one_copy = new byte[]{3, 25, 12};
		byte[] two = new byte[]{4, 14, 54};
		set.add(one);
		set.add(two);		

		assertFalse(set.contains(new byte[]{3, 16, 28}));
		
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
		
		for(PersistentBytes bytes: set.getAll()){
			assertTrue(set.contains(bytes.load()));
		}
		
	}
	
	@Test
	public void testPersistentBytes() throws IOException{
		byte[] one = new byte[]{3, 25, 12};
		set.add(one);
		
		for(PersistentBytes bytes : set.getAll()){
			bytes.equals(bytes);
			assertArrayEquals(new byte[]{3, 25, 12}, bytes.load());
			bytes.replace(new byte[]{3, 3, 3});
			assertArrayEquals(new byte[]{3,3,3}, bytes.load());
		}
	}

}
