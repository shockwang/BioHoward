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

		boolean hasEquip = false;
		
		StringBuffer buf = new StringBuffer("");
		if (equipMap.containsKey(EquipType.HELMET)) {
			buf.append("|�Y��|   " + showSingleEquip(EquipType.HELMET, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.ARMOR)) {
			buf.append("|����|   " + showSingleEquip(EquipType.ARMOR, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.GLOVES)) {
			buf.append("|����|   " + showSingleEquip(EquipType.GLOVES, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.WEAPON)) {
			buf.append("|�Z��|   " + showSingleEquip(EquipType.WEAPON, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.SHIELD)) {
			buf.append("|�޵P|   " + showSingleEquip(EquipType.SHIELD, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.BOOTS)) {
			buf.append("|���}|   " + showSingleEquip(EquipType.BOOTS, equipMap));
			hasEquip = true;
		}
		if (equipMap.containsKey(EquipType.ACCESSORY)) {
			buf.append("|���~|   "
					+ showSingleEquip(EquipType.ACCESSORY, equipMap));
			hasEquip = true;
		}
		
		if (!hasEquip) {
			buf.append("�ŵL�@��.\n");
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
			buf.append(c.getChiName() + "�����F���~�G\n");
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
				CommandServer.informCharacter(c, "���˪F��ä��O�e����!\n");
		} else
			CommandServer.informCharacter(c, "�o�̨S���A���w���e���C\n");
		return null;
	}
	
	public static String showContainerStatus(IContainer c){
		switch (c.getStatus()){
		case CLOSED: case LOCKED:
			return "��";
		case OPENED: 
			return "�}";
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
