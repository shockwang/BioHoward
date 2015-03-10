package test.module.character;

import module.character.Group;
import module.character.api.ICharacter;

import org.junit.Before;
import org.junit.Test;

public class GroupTest {
	private Group testGroup;
	private ICharacter leader;
	
	@Before
	public void setUp(){
		leader = new CharacterStubName("����", "Tao");
		testGroup = new Group(leader);
	}
	
	@Test
	public void addCharTest(){
		ICharacter min1 = new CharacterStubName("�p��", "min");
		testGroup.addChar(min1);
		testGroup.displayInfo();
		ICharacter hua1 = new CharacterStubName("�p��", "hua");
		testGroup.addChar(hua1);
		testGroup.displayInfo();
		ICharacter min2 = new CharacterStubName("�p��", "min");
		testGroup.addChar(min2);
		testGroup.displayInfo();
		
		// test removeChar
		System.out.println("removeChar test:");
		testGroup.removeChar(new CharacterStubName("�p��", "min"));
		testGroup.displayInfo();
		testGroup.removeChar(min1);
		testGroup.displayInfo();
		testGroup.removeChar(min2);
		testGroup.displayInfo();
		testGroup.removeChar(leader);
		testGroup.displayInfo();
		testGroup.removeChar(hua1);
		testGroup.displayInfo();
	}
}
