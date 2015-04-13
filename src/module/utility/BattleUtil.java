package module.utility;

import module.battle.BattleTask;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.command.CommandServer;

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
	
	public static boolean checkIfAbleToStartBattle(ICharacter src, ICharacter target){
		Group g = src.getMyGroup();
		
		if (g.getTalking() || g.getInEvent()){
			CommandServer.informGroup(g, "你的隊伍正在忙呢，等一下吧。\n");
		} else if (target.getMyGroup().getTalking() || target.getMyGroup().getInEvent()){
			CommandServer.informGroup(g, "你指定的隊伍正在忙呢，等一下吧。\n");
		} else 
			return true;
		return false;
	}
	
	public static void handleBattleTaskBehavior(ICharacter src, ICharacter target){
		if (src.getMyGroup() == target.getMyGroup()) return;
		
		Group gS = src.getMyGroup();
		Group gT = target.getMyGroup();
		if (gS.getInBattle()){
			if (gT.getInBattle()){
				if (gS == gT) return; // already in the same battle
				else {
					// TODO: define the battle merge mechanism
				}
			} else {
				// target group is not in battle
				gS.getBattleTask().addBattleOppositeGroup(gS, gT);
			}
		} else {
			if (gT.getInBattle()){
				// src join target's battle
				gT.getBattleTask().addBattleOppositeGroup(gT, gS);
			} else {
				// both src & target group not in battle
				new BattleTask(gS, gT);
			}
		}
	}
}
