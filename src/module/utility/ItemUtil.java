package module.utility;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;

public class ItemUtil {
	public static String wearMsg(IEquipment equip){
		switch (equip.getEquipType()){
		case WEAPON:
			return String.format("舉起%s當作武器。", equip.getChiName());
		case SHIELD:
			return String.format("舉起%s當作盾牌。", equip.getChiName());
		case HELMET:
			return String.format("將%s戴在頭上。", equip.getChiName());
		case ARMOR:
			return String.format("將%s穿在身上。", equip.getChiName());
		case GLOVES:
			return String.format("將%s戴在手上。", equip.getChiName());
		case BOOTS:
			return String.format("將%s穿在腳上。", equip.getChiName());
		case ACCESSORY:
			return String.format("把%s穿戴在身上。", equip.getChiName());
		default: 
			return null;
		}
	}
	
	public static String showPlayerEquip(ICharacter c){
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c.getEquipment();
		
		StringBuffer buf = new StringBuffer();
		buf.append("|頭部|   ");
		buf.append(showSingleEquip(EquipType.HELMET, equipMap));
		buf.append("|身體|   ");
		buf.append(showSingleEquip(EquipType.ARMOR, equipMap));
		buf.append("|雙手|   ");
		buf.append(showSingleEquip(EquipType.GLOVES, equipMap));
		buf.append("|武器|   ");
		buf.append(showSingleEquip(EquipType.WEAPON, equipMap));
		buf.append("|盾牌|   ");
		buf.append(showSingleEquip(EquipType.SHIELD, equipMap));
		buf.append("|雙腳|   ");
		buf.append(showSingleEquip(EquipType.BOOTS, equipMap));
		buf.append("|飾品|   ");
		buf.append(showSingleEquip(EquipType.ACCESSORY, equipMap));
		return buf.toString();
	}
	
	private static String showSingleEquip(IEquipment.EquipType type, 
			ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap){
		IEquipment equip = equipMap.get(type);
		if (equip != null) return String.format("%s/%s\n", equip.getChiName(), equip.getEngName());
		else return "無\n";
	}
	
	public static String showLookEquip(ICharacter c){
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c.getEquipment();
		
		StringBuffer buf = new StringBuffer("");
		if (equipMap.containsKey(EquipType.HELMET)) 
			buf.append("|頭部|   " + showSingleEquip(EquipType.HELMET, equipMap));
		if (equipMap.containsKey(EquipType.ARMOR)) 
			buf.append("|身體|   " + showSingleEquip(EquipType.ARMOR, equipMap));
		if (equipMap.containsKey(EquipType.GLOVES)) 
			buf.append("|雙手|   " + showSingleEquip(EquipType.GLOVES, equipMap));
		if (equipMap.containsKey(EquipType.WEAPON)) 
			buf.append("|武器|   " + showSingleEquip(EquipType.WEAPON, equipMap));
		if (equipMap.containsKey(EquipType.SHIELD)) 
			buf.append("|盾牌|   " + showSingleEquip(EquipType.SHIELD, equipMap));
		if (equipMap.containsKey(EquipType.BOOTS)) 
			buf.append("|雙腳|   " + showSingleEquip(EquipType.BOOTS, equipMap));
		if (equipMap.containsKey(EquipType.ACCESSORY)) 
			buf.append("|飾品|   " + showSingleEquip(EquipType.ACCESSORY, equipMap));
		
		return buf.toString();
	}
}
