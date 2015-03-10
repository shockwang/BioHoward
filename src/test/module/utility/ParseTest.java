package test.module.utility;

import static org.junit.Assert.*;
import module.utility.Parse;

import org.junit.Test;

public class ParseTest {
	@Test
	public void mergeStringTest(){
		String data1 = "status oh ya oh ya";
		String[] temp = data1.split(" ");
		assertTrue(Parse.mergeString(temp, 2, ' ').equals("ya oh ya"));
		assertTrue(Parse.mergeString(temp, 1, 3, ' ').equals("oh ya oh"));
		assertTrue(Parse.mergeString(temp, 5, ' ') == null);
	}
}
