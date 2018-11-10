package module.character.constants;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.command.CommandServer;

public class CSpecialStatus {
	public static enum specialStatus {
		POISION("中毒"), BLIND("失明");

		private String chineseName;

		specialStatus(String name) {
			this.chineseName = name;
		}
	}

	private static String getName(specialStatus atr) {
		return atr.chineseName;
	}

	public static String displaySingleSpecialStatus(specialStatus atr, int value) {
		return String.format("%s: %d分|", getName(atr), value);
	}

	public static String displaySpecialStatus(ICharacter ch) {
		ConcurrentHashMap<specialStatus, Integer> map = ch.getSpecialStatusMap();

		String result = "|";
		if (map.get(specialStatus.POISION) != null)
			result += displaySingleSpecialStatus(specialStatus.POISION, map
					.get(specialStatus.POISION));
		if (map.get(specialStatus.BLIND) != null)
			result += displaySingleSpecialStatus(specialStatus.BLIND, map
					.get(specialStatus.BLIND));
		return result;
	}

	// update special status by hours
	public static void updateSpecialStatus(ICharacter ch, int timePass) {
		ConcurrentHashMap<specialStatus, Integer> map = ch.getSpecialStatusMap();

		int timeLeft;
		boolean doneFlag = false;
		// add the doneFlag to prevent the situation when an object is deleted
		// but the entry still tries to get the next object

		for (Entry<specialStatus, Integer> entry : map.entrySet()) {
			timeLeft = entry.getValue();
			timeLeft -= timePass;
			entry.setValue(timeLeft);

		}
		while (!doneFlag) {
			doneFlag = true;
			for (Entry<specialStatus, Integer> entry : map.entrySet()) {
				if (entry.getValue() <= 0){
					removeSpecialStatus(ch, entry.getKey());
					doneFlag = false;
					break;
				}
			}
		}
	}

	// remove a special status from a character
	public static void removeSpecialStatus(ICharacter ch, specialStatus ss) {
		ch.getSpecialStatusMap().remove(ss);
		ch.getAtRoom().informRoom(String.format("%s狀態從%s身上消失了。\n", 
				ss.chineseName, ch.getChiName()));
	}
	
	// set specialstatus and update
	public static void setSpecialStatus(ICharacter c, specialStatus ss, int time){
		String output;
		if (c.resistSpecialStatus(ss)){
			output = c.getChiName() + "看起來絲毫不受影響。";
		} else {
			output = c.getChiName() + ss.chineseName + "了!";
			c.setSpecialStatus(ss, time);
		}
		
		c.getAtRoom().informRoom(output + "\n");
		if (c instanceof PlayerCharacter){
			PlayerCharacter pg = (PlayerCharacter) c;
			String out = "status:" + pg.showGroupStatus();
			CommandServer.informCharacter(pg, out);
		}
	}
	
	public static double judgeHitRatioEffected(double hitRatio
			, Map<specialStatus, Integer> specialStatusMap){
		double result = hitRatio;
		
		if (specialStatusMap.get(specialStatus.BLIND) != null) result *= 0.3;
		
		return result;
	}
	
	public static double judgeDodgeRatioEffected(double dodgeRatio
			, Map<specialStatus, Integer> specialStatusMap){
		double result = dodgeRatio;
		
		if (specialStatusMap.get(specialStatus.BLIND) != null) result *= 0.3;
		
		return result;
	}
}
