package module.utility;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;

public class Battle {
	public static String deadMechanism(ICharacter target) {
		StringBuilder buffer = new StringBuilder();
		Group g = target.getMyGroup();
		target.getMyGroup().getAtRoom().informRoom(target.getChiName() + "�˦a���_�F!\n");

		if (g instanceof PlayerGroup) {
			target.getAttributeMap().get(attribute.HP).setCurrent(0);
			synchronized (g.getBattleTask().getTimeMap()) {
				g.getBattleTask().getTimeMap().get(target).setCurrent(0);
			}
		} 
		boolean battleOver = g.getBattleTask().checkBattleEnd();
		if (battleOver)
			buffer.append("�԰�����!\n");
		return buffer.toString();
	}
}
