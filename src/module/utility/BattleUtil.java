package module.utility;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;

public class BattleUtil {
	public static void deadMechanism(ICharacter target) {
		Group g = target.getMyGroup();
		target.getMyGroup().getAtRoom().informRoom(target.getChiName() + "倒地不起了!\n");

		if (g instanceof PlayerGroup) {
			target.getAttributeMap().get(attribute.HP).setCurrent(0);
			synchronized (g.getBattleTask().getTimeMap()) {
				g.getBattleTask().getTimeMap().get(target).setCurrent(0);
			}
		} 
		g.getBattleTask().checkBattleEnd();
	}
	
	public static boolean characterAttributeChange(ICharacter target, attribute attr, int value){
		try {
			int max = target.getMaxAttribute(attr);
			int current = target.getCurrentAttribute(attr);
			
			if (current + value > max) current = max;
			else current += value;
			
			target.setCurrentAttribute(attr, current);
			return true;
		} catch (NullPointerException e){
			// TODO: define null case?
			e.printStackTrace();
		}
		return false;
	}
}
