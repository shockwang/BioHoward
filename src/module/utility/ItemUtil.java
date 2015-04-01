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
	
	public static String getEquipmentType(IEquipment equip){
		switch (equip.getEquipType()){
		case WEAPON:
			return "武器";
		case SHIELD:
			return "盾牌";
		case HELMET:
			return "頭盔";
		case ARMOR:
			return "護甲";
		case GLOVES:
			return "手套";
		case BOOTS:
			return "鞋子";
		case ACCESSORY:
			return "飾品";
		default:
			return null;
		}
	}

	public static String showPlayerEquip(ICharacter c) {
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c
				.getEquipment();

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
			ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap) {
		IEquipment equip = equipMap.get(type);
		if (equip != null)
			return String.format("%s/%s\n", equip.getChiName(),
					equip.getEngName());
		else
			return "無\n";
	}

	public static String showLookEquip(ICharacter c) {
		ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = c
				.getEquipment();

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
			buf.append("|飾品|   "
					+ showSingleEquip(EquipType.ACCESSORY, equipMap));

		return buf.toString();
	}

	public static void dropAllItemOnDefeat(Group g) {
		if (g.getInventory().itemList.size() == 0)
			return;

		synchronized (g.getAtRoom().getItemList()) {
			StringBuffer buf = new StringBuffer();
			buf.append(g.getChiName() + "掉落了物品：\n");
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
