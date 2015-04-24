package module.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import module.battle.BattleTask;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus;
import module.character.constants.CStatus;
import module.character.constants.CStatus.status;
import module.command.CommandServer;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;
import module.item.api.IWeapon;
import module.server.PlayerServer;

public class BattleUtil {
	public static void deadMechanism(ICharacter target) {
		Group g = target.getMyGroup();
		target.getMyGroup().getAtRoom().informRoom(target.getChiName() + "�˦a���_�F!\n");

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
			CommandServer.informGroup(g, "�A������b���O�A���@�U�a�C\n");
		} else if (target.getMyGroup().getTalking() || target.getMyGroup().getInEvent()){
			CommandServer.informGroup(g, "�A���w������b���O�A���@�U�a�C\n");
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
				startNewBattle(gS, gT);
			}
		}
	}
	
	public static void attackMechanism(ICharacter src, ICharacter target){
		StringBuffer buf = new StringBuffer();
		IWeapon weapon = (IWeapon) src.getEquipment().get(EquipType.WEAPON);
		if (weapon != null){
			buf.append(String.format("%s���ʤ⤤��%s�V%s��%s���h�A", 
					src.getChiName(), weapon.getChiName(), target.getChiName(), 
					target.getRandomBodyPart()));
		} else {
			buf.append(String.format("%s%s%s��%s�A", 
					src.getChiName(), src.getBareHandAttackMessage(), target.getChiName(), 
					target.getRandomBodyPart()));
		}
		
		// calculate dodge probability
		double hitRatio = 0.9; // TODO: define hitRatio
		if (weapon != null) hitRatio = weapon.getHitRatio();
		hitRatio = CSpecialStatus.judgeHitRatioEffected(hitRatio, src.getSpecialStatusMap());
		
		double speedDelta = (double) (src.getStatus(status.SPEED) - target.getStatus(status.SPEED))
				/ (double) target.getStatus(status.SPEED);
		
		// judge the dodge ratio
		speedDelta = CSpecialStatus.judgeDodgeRatioEffected(speedDelta, target.getSpecialStatusMap());
		
		double dodgeRatio = 1 - hitRatio + speedDelta;
		if (dodgeRatio > 0.95) dodgeRatio = 0.95;
		if (dodgeRatio < 0) dodgeRatio = 0;
		
		if (PlayerServer.getRandom().nextDouble() > dodgeRatio){
			// hit
			if (target.getAttributeMap().get(attribute.HP) != null) {
				int damage = attackDamage(src, target);
				if (damage > 0){
					buf.append(String.format("��L�y��%d�I�ˮ`!\n", damage));
					
					int current = target.getAttributeMap().get(attribute.HP)
							.getCurrent();
					target.getAttributeMap().get(attribute.HP).setCurrent(current - damage);
					
				} else {
					// no damage
					buf.append("���o�ث¤O���L���@����b���̡C\n");
				}
			} else {
				// no hp
				buf.append("���ݰ_�ӹ�L���@���_�@�ΡC\n");
			}
			src.getMyGroup().getAtRoom().informRoom(buf.toString());
			
			// weapon effect
			if (weapon != null){
				weapon.onAttack(src, target);
				if (target.isDown()) {
					BattleUtil.deadMechanism(target);
				}
			}
			
			if (target.isDown()) {
				BattleUtil.deadMechanism(target);
			}
			
			if (target.getMyGroup() instanceof PlayerGroup) {
				target.getMyGroup().getBattleTask()
						.updatePlayerStatus((PlayerGroup) target.getMyGroup());
			}
			return;
		} else {
			// dodged
			buf.append("\n��" + target.getChiName() + "�ܧ֪��⨭�l������@�{�A���L�F����!\n");
		}
		src.getMyGroup().getAtRoom().informRoom(buf.toString());
	}
	
	private static int attackDamage(ICharacter src, ICharacter target){
		
		int equipAtk = 0;
		for (Entry<EquipType, IEquipment> entry : src.getEquipment().entrySet()){
			try {
				equipAtk += entry.getValue().getStatus()
						.get(CStatus.status.WEAPON_ATTACK);
			} catch (NullPointerException e){
				// do nothing
			}
		}
		int atk = src.getStatus(status.STRENGTH) / 2 + equipAtk;
		//TODO: add weapon practice point
		// TODO: add critical hit mechanism
		
		int defense = target.getStatus(status.CONSTITUTION) + getCharPhysicalEquipDefense(target);
		int delta = atk - defense;
		if (delta < 0) delta = 0;
		
		// add random effect
		double rate = 100.0 + PlayerServer.getRandom().nextGaussian() * 5.0;
		double result = (double) delta * (rate / 100.0);
		return (int) result;
	}
	
	private static int getCharPhysicalEquipDefense(ICharacter target){
		int defenseSum = 0;
		
		for (Entry<EquipType, IEquipment> entry : target.getEquipment().entrySet()){
			try {
				defenseSum += entry.getValue().getStatus().get(CStatus.status.ARMOR);
			} catch (NullPointerException e){
				// do nothing
			}
		}
		return defenseSum;
	}
	
	public static void startNewBattle(Group src, Group target){
		Class<BattleTask> specialTaskClass = null;
		
		if (src.getSpecialBattle() != null)
			specialTaskClass = src.getSpecialBattle();
		else if (target.getSpecialBattle() != null)
			specialTaskClass = src.getSpecialBattle();
		
		if (specialTaskClass != null){
			Class[] paramClass = new Class[] {Group.class, Group.class};
			Object[] paramArray = new Object[] {src, target};
			try {
				Constructor c = specialTaskClass.getConstructor(paramClass);
				c.newInstance(paramArray);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			new BattleTask(src, target);
	}
	
	public static String showStatusInBattle(ICharacter c){
		if (c.getAttributeMap().containsKey(attribute.HP)){
			int current, max;
			current = c.getCurrentAttribute(attribute.HP);
			max = c.getMaxAttribute(attribute.HP);
			double rate = (double) current / (double) max;
			
			String output = c.getChiName();
			if (rate > 0.8)
				output += "�ݰ_�ӨS������ˡC";
			else if (rate > 0.6)
				output += "���W���F�X�B�ˡA���L�٤�ê�ơC";
			else if (rate > 0.4)
				output += "�I�l���áA���A���Ӧn�C";
			else if (rate > 0.2)
				output += "�y��a�աA�n���H�ɳ��|�ˤU!";
			else
				output += "���b���`��t�ä�A�V�O�M�D�@������!";
			return output;
		}
		return null;
	}
}
