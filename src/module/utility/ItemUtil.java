package module.utility;

import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.Group;
import module.character.api.ICharacter;
import module.item.SingleItemList;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;
import module.item.api.IItem;

public class ItemUtil {
	public static String wearMsg(IEquipment equip) {
		switch (equip.getEquipType()) {
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
	
	public static String getEquipmentType(IEquipment equip){
		switch (equip.getEquipType()){
		case WEAPON:
			return "�Z��";
		case SHIELD:
			return "�޵P";
		case HELMET:
			return "�Y��";
		case ARMOR:
			return "�@��";
		case GLOVES:
			return "��M";
		case BOOTS:
			return "�c�l";
		case ACCESSORY:
			return "���~";
		default:
			return null;
		}
	}

	public static String showPlayerEquip(ICharacter c) {
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c
				.getEquipment();

		StringBuffer buf = new StringBuffer();
		buf.append("|�Y��|   ");
		buf.append(showSingleEquip(EquipType.HELMET, equipMap));
		buf.append("|����|   ");
		buf.append(showSingleEquip(EquipType.ARMOR, equipMap));
		buf.append("|����|   ");
		buf.append(showSingleEquip(EquipType.GLOVES, equipMap));
		buf.append("|�Z��|   ");
		buf.append(showSingleEquip(EquipType.WEAPON, equipMap));
		buf.append("|�޵P|   ");
		buf.append(showSingleEquip(EquipType.SHIELD, equipMap));
		buf.append("|���}|   ");
		buf.append(showSingleEquip(EquipType.BOOTS, equipMap));
		buf.append("|���~|   ");
		buf.append(showSingleEquip(EquipType.ACCESSORY, equipMap));
		return buf.toString();
	}

	private static String showSingleEquip(IEquipment.EquipType type,
			ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap) {
		IEquipment equip = equipMap.get(type);
		if (equip != null)
			return String.format("%s/%s\n", equip.getChiName(),
					equip.getEngName());
		else
			return "�L\n";
	}

	public static String showLookEquip(ICharacter c) {
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c
				.getEquipment();

		StringBuffer buf = new StringBuffer("");
		if (equipMap.containsKey(EquipType.HELMET))
			buf.append("|�Y��|   " + showSingleEquip(EquipType.HELMET, equipMap));
		if (equipMap.containsKey(EquipType.ARMOR))
			buf.append("|����|   " + showSingleEquip(EquipType.ARMOR, equipMap));
		if (equipMap.containsKey(EquipType.GLOVES))
			buf.append("|����|   " + showSingleEquip(EquipType.GLOVES, equipMap));
		if (equipMap.containsKey(EquipType.WEAPON))
			buf.append("|�Z��|   " + showSingleEquip(EquipType.WEAPON, equipMap));
		if (equipMap.containsKey(EquipType.SHIELD))
			buf.append("|�޵P|   " + showSingleEquip(EquipType.SHIELD, equipMap));
		if (equipMap.containsKey(EquipType.BOOTS))
			buf.append("|���}|   " + showSingleEquip(EquipType.BOOTS, equipMap));
		if (equipMap.containsKey(EquipType.ACCESSORY))
			buf.append("|���~|   "
					+ showSingleEquip(EquipType.ACCESSORY, equipMap));

		return buf.toString();
	}

	public static void dropAllItemOnDefeat(Group g) {
		if (g.getInventory().itemList.size() == 0)
			return;

		synchronized (g.getAtRoom().getItemList()) {
			StringBuffer buf = new StringBuffer();
			buf.append(g.getChiName() + "�����F���~�G\n");
			for (SingleItemList sil : g.getInventory().itemList)
				buf.append(sil.displayInfo());
			
			IItem obj = null;
			while (g.getInventory().itemList.size() > 0){
				obj = g.getInventory().itemList.get(0).findItem(0);
				g.getInventory().removeItem(obj);
				g.getAtRoom().getItemList().addItem(obj);
			}
			g.getAtRoom().informRoomExceptGroup(g, buf.toString());
		}
	}
	
	public static void createLootingItem(Group g){
		for (CharList cList : g.list){
			for (ICharacter c : cList.charList)
				c.looting();
		}
	}
}
