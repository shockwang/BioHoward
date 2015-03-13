package module.utility;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;

public class ItemUtil {
	public static String wearMsg(IEquipment equip){
		switch (equip.getEquipType()){
		case WEAPON:
			return String.format("�|�_%s��@�Z���C", equip.getChiName());
		case SHIELD:
			return String.format("�|�_%s��@�޵P�C", equip.getChiName());
		case HELMET:
			return String.format("�N%s���b�Y�W�C", equip.getChiName());
		case ARMOR:
			return String.format("�N%s��b���W�C", equip.getChiName());
		case GLOVES:
			return String.format("�N%s���b��W�C", equip.getChiName());
		case BOOTS:
			return String.format("�N%s��b�}�W�C", equip.getChiName());
		case ACCESSORY:
			return String.format("��%s�����b���W�C", equip.getChiName());
		default: 
			return null;
		}
	}
	
	public static String showPlayerEquip(ICharacter c){
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c.getEquipment();
		
		StringBuffer buf = new StringBuffer();
		buf.append("|�Y��|   ");
		buf.append(showSingleEquip(EquipType.HELMET, equipMap));
	}
	
	private static String showSingleEquip(IEquipment.EquipType type, 
			ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap){
		IEquipment equip = equipMap.get(type);
		if (equip != null) return String.format("%s/%s\n", equip.getChiName(), equip.getEngName());
		else return "�L\n";
	}
}
