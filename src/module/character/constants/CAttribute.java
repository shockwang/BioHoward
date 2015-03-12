package module.character.constants;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.api.IntPair;

public class CAttribute {
	public static enum attribute {
		HP("生命"), MP("魔力"), SP("內力");

		private String chineseName;

		attribute(String inputName) {
			this.chineseName = inputName;
		}
		
		public String getName(){
			return this.chineseName;
		}
	}

	private static String getName(attribute atr) {
		return atr.chineseName;
	}

	public static String displaySingleAttribute(attribute atr,
			IntPair value) {
		return String.format("%s: %d/%d |", getName(atr), value.getCurrent(), value.getMax());
	}

	public static String displayAttribute(ICharacter ch) {
		ConcurrentHashMap<attribute, IntPair> map = ch.getAttributeMap();
		
		String result = "|";
		if (map.get(attribute.HP) != null)
			result += displaySingleAttribute(attribute.HP, map.get(attribute.HP));
		if (map.get(attribute.MP) != null)
			result += displaySingleAttribute(attribute.MP, map.get(attribute.MP));
		if (map.get(attribute.SP) != null)
			result += displaySingleAttribute(attribute.SP, map.get(attribute.SP));
		return result;
	}
	
	public static void fullRecover(ICharacter c){
		for (Entry<attribute, IntPair> entry : c.getAttributeMap().entrySet()){
			IntPair pair = entry.getValue();
			pair.setCurrent(pair.getMax());
		}
	}
}
