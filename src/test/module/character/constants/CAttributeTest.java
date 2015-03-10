package test.module.character.constants;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.module.character.CharacterStub;

public class CAttributeTest {
	private ConcurrentHashMap<CAttribute.attribute, IntPair> testMap;
	private ICharacter charStub;
	
	@Before
	public void setUp(){
		testMap = new ConcurrentHashMap<CAttribute.attribute, IntPair>();
		charStub = new CharacterStub(){
			@Override
			public ConcurrentHashMap<CAttribute.attribute, IntPair> getAttributeMap(){
				return testMap;
			}
			
		};
	}
	
	@After
	public void tearDown(){
		testMap = null;
		charStub = null;
	}
	
	@Test
	public void testCAttribute(){
		testMap.put(CAttribute.attribute.HP, new IntPair(20, 30));
		testMap.put(CAttribute.attribute.MP, new IntPair(10, 20));
		testMap.put(CAttribute.attribute.SP, new IntPair(5, 6));
		System.out.println(CAttribute.displayAttribute(charStub));
		testMap.clear();
		
		testMap.put(CAttribute.attribute.HP, new IntPair(15, 17));
		testMap.put(CAttribute.attribute.SP, new IntPair(6, 10));
		System.out.println(CAttribute.displayAttribute(charStub));
		
		
		assertTrue(true);
	}
}
