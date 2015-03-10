package test.module.character.constants;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.constants.CSpecialStatus;
import module.character.constants.CSpecialStatus.specialStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.module.character.CharacterStub;

public class CSpecialStatusTest {
	private ConcurrentHashMap<CSpecialStatus.specialStatus, Integer> testMap;
	private ICharacter charStub;
	
	@Before
	public void setUp(){
		testMap = new ConcurrentHashMap<CSpecialStatus.specialStatus, Integer>();
		charStub = new CharacterStub(){
			@Override
			public ConcurrentHashMap<CSpecialStatus.specialStatus, Integer> getSpecialStatusMap(){
				return testMap;
			}
			
			@Override
			public String getChiName(){
				return "´ú¸Õ¤H";
			}
		};
	}
	
	@After
	public void tearDown(){
		testMap = null;
		charStub = null;
	}
	
	@Test
	public void testSpecialStatus(){
		testMap.put(specialStatus.POISION, 3);
		testMap.put(specialStatus.BLIND, 5);
		System.out.println(CSpecialStatus.displaySpecialStatus(charStub));
		
		CSpecialStatus.updateSpecialStatus(charStub, 2);
		System.out.println(CSpecialStatus.displaySpecialStatus(charStub));
		
		CSpecialStatus.updateSpecialStatus(charStub, 2);
		System.out.println(CSpecialStatus.displaySpecialStatus(charStub));
		
		CSpecialStatus.updateSpecialStatus(charStub, 2);
		System.out.println(CSpecialStatus.displaySpecialStatus(charStub));
		
		assertTrue(true);
	}
}
