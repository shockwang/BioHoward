package test.module.item;

import java.util.concurrent.ConcurrentHashMap;

import module.character.AbstractCharacter;
import module.character.GroupList;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.BaseEquipment;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;

import org.junit.Before;
import org.junit.Test;

public class EquipmentTest {
	private IEquipment testArmor = null;
	
	private class CharEquipTest extends AbstractCharacter{
		public CharEquipTest(String chiName, String engName) {
			super(chiName, engName);
		}

		private String desc;
		
		@Override
		public void setDesc(String description) {
			this.desc = description;
		}

		@Override
		public String getDesc() {
			return desc;
		}

		@Override
		public boolean battleAction(GroupList enemyGroup) {
			return true;
		}
		
	}
	
	@Before
	public void setUp(){
		testArmor = new BaseEquipment("����", "armor", EquipType.ARMOR);
		ConcurrentHashMap<attribute, Integer> attrMap = new ConcurrentHashMap<attribute, Integer>();
		attrMap.put(attribute.HP, 50);
		attrMap.put(attribute.MP, -30);
		attrMap.put(attribute.SP, 100);
		testArmor.setAttribute(attrMap);
		
		ConcurrentHashMap<status, Integer> statMap = new ConcurrentHashMap<status, Integer>();
		statMap.put(status.WEAPON_ATTACK, 10);
		statMap.put(status.SPELL_ATTACK, 5);
		testArmor.setStatus(statMap);
		
		testArmor.setDescription("�O��޳�諸����!");
		testArmor.setPrice(12345);
	}
	
	@Test
	public void equipDisplayTest(){
		System.out.println(testArmor.display());
	}
}
