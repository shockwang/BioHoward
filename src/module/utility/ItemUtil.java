package module.utility;

import module.item.api.IEquipment;

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
}
