package module.character.constants;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;

public class CStatus {
	public static enum status{
		// those are owned by characters
		STRENGTH("�O�q"),
		CONSTITUTION("���"),
		SPEED("�ӱ�"),
				
		// those are provided by equipments
		WEAPON_ATTACK("���z�ˮ`"),
		SPELL_ATTACK("�N�Ӷˮ`"),
		ARMOR("�@��");
		
		private String chineseName;
		status (String name){
			this.chineseName = name;
		}
		
		public String getName(){
			return this.chineseName;
		}
	}
	
	private static String getName(status atr) {
		return atr.chineseName;
	}
	
	public static String displaySingleStatus(status atr,
			int value) {
		return String.format("%s: %d|", getName(atr), value);
	}
	
	// display status
	public static String displayStatus(ICharacter ch) {
		ConcurrentHashMap<status, Integer> map = ch.getStatusMap();
		
		String result = "|";
		
		result += displaySingleStatus(status.WEAPON_ATTACK, map.get(status.WEAPON_ATTACK).intValue());
		result += displaySingleStatus(status.SPELL_ATTACK, map.get(status.SPELL_ATTACK).intValue());
		result += displaySingleStatus(status.SPEED, map.get(status.SPEED).intValue());
		return result;
	}
}
