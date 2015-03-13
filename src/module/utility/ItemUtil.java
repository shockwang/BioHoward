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
	}
	
	private static String showSingleEquip(IEquipment.EquipType type, 
			ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap){
		IEquipment equip = equipMap.get(type);
		if (equip != null) return String.format("%s/%s\n", equip.getChiName(), equip.getEngName());
		else return "無\n";
	}
}
