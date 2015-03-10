package module.utility;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;

public class Battle {
	public static String deadMechanism(ICharacter target) {
		StringBuilder buffer = new StringBuilder();
		Group g = target.getMyGroup();
		buffer.append(target.getChiName() + "倒地不起了!\n");

		if (g instanceof PlayerGroup) {
			target.getAttributeMap().get(attribute.HP).setCurrent(0);
			synchronized (g.getBattleTask().getTimeMap()) {
				g.getBattleTask().getTimeMap().get(target).setCurrent(0);
			}
		} else {
			// NPC group
			/*g.removeChar(target);
			if (g.list.isEmpty()) {
				g.getBattleTask().getMyGroups(target).gList.remove(g);
				g.setInBattle(false);
				g.getAtRoom().getGroupList().gList.remove(g);
				g.setAtRoom(null);
			}
			target.setMyGroup(null);
			target = null;*/
		}
		boolean battleOver = g.getBattleTask().checkBattleEnd();
		if (battleOver)
			buffer.append("戰鬥結束!\n");
		return buffer.toString();
	}
}
