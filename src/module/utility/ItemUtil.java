package module.utility;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.ItemList;
import module.item.SingleItemList;
import module.item.api.IContainer;
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

		boolean hasEquip = false;
		
		StringBuffer buf = new StringBuffer("");
		if (equipMap.containsKey(EquipType.HELMET)) {
			buf.append("|頭部|   " + showSingleEquip(EquipType.HELMET, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.ARMOR)) {
			buf.append("|身體|   " + showSingleEquip(EquipType.ARMOR, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.GLOVES)) {
			buf.append("|雙手|   " + showSingleEquip(EquipType.GLOVES, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.WEAPON)) {
			buf.append("|武器|   " + showSingleEquip(EquipType.WEAPON, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.SHIELD)) {
			buf.append("|盾牌|   " + showSingleEquip(EquipType.SHIELD, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.BOOTS)) {
			buf.append("|雙腳|   " + showSingleEquip(EquipType.BOOTS, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.ACCESSORY)) {
			buf.append("|飾品|   "
					+ showSingleEquip(EquipType.ACCESSORY, equipMap));
			hasEquip = true;
		}
		
		if (!hasEquip) {
			buf.append("空無一物.\n");
		}

		return buf.toString();
	}

	public static void dropAllItemOnDefeat(ICharacter c) {
		// add all equipments to inventory
		for (Entry<EquipType, IEquipment> entry : c.getEquipment().entrySet()){
			IItem equip = entry.getValue();
			c.getInventory().addItem(equip);
		}
		c.getEquipment().clear();
		
		if (c.getInventory().itemList.size() == 0)
			return;

		synchronized (c.getAtRoom().getItemList()) {
			StringBuffer buf = new StringBuffer();
			buf.append(c.getChiName() + "掉落了物品：\n");
			for (SingleItemList sil : c.getInventory().itemList)
				buf.append(sil.displayInfo());
			
			IItem obj = null;
			while (c.getInventory().itemList.size() > 0){
				obj = c.getInventory().itemList.get(0).findItem(0);
				c.getInventory().removeItem(obj);
				c.getAtRoom().getItemList().addItem(obj);
			}
			c.getAtRoom().informRoomExceptCharacter(c, buf.toString());
		}
	}
	
	public static void createLootingItem(ICharacter c){
		c.looting();
	}
	
	public static IContainer checkIsContainer(ICharacter c, ItemList list, String target){
		IItem objContainer = list.findItem(target);
		if (objContainer != null) {
			if (objContainer instanceof IContainer){
				return (IContainer) objContainer;
			} else
				CommandServer.informCharacter(c, "那樣東西並不是容器喔!\n");
		} else
			CommandServer.informCharacter(c, "這裡沒有你指定的容器。\n");
		return null;
	}
	
	public static String showContainerStatus(IContainer c){
		switch (c.getStatus()){
		case CLOSED: case LOCKED:
			return "關";
		case OPENED: 
			return "開";
		}
		return null;
	}
	
	public static IEquipment findEquipByName(ICharacter c, String name) {
		for (Entry<EquipType, IEquipment> entry : c.getEquipment().entrySet()){
			if (Search.searchName(entry.getValue().getEngName(), name))
				return entry.getValue();
		}

		return null;
	}
}
