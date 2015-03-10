package test.module.character;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import module.character.GroupList;
import module.character.api.ICharacter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupListTest {
	private GroupList testList;
	@Before
	public void setUp(){
		testList = new GroupList();
	}
	
	@After
	public void tearDown(){
		testList = null;
	}
	
	@Test
	public void createGroupTest(){
		ICharacter a = new CharacterStubName("啊嘶", "a");
		ICharacter b = new CharacterStubName("喔耶", "b");
		testList.createGroup(a);
		testList.createGroup(b);
		testList.displayInfo();
		
		System.out.println("search group test:");
		assertNotNull(testList.findChar("a's", "a", 0));
		assertNotNull(testList.findChar(1, "b", 0));
		assertNull(testList.findChar(2, "a", 0));
		testList.addGroupChar(0, new CharacterStubName("小明", "min"));
		testList.addGroupChar("b's", new CharacterStubName("小華", "hua"));
		testList.gList.get(0).displayInfo();
		testList.gList.get(1).displayInfo();
	}
}