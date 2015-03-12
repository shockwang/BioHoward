package module.item.api;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;

public interface IEquipment extends IItem{
	enum EquipType {
		WEAPON,
		SHIELD,
		HELMET,
		ARMOR,
		GLOVES,
		BOOTS,
		ACCESSORY
	}
	
	void setEquipType(EquipType type);
	EquipType getEquipType();
	
	boolean onWear(ICharacter c);
	boolean onRemove(ICharacter c);
	
	ConcurrentHashMap<attribute, Integer> getAttribute();
	void setAttribute(ConcurrentHashMap<attribute, Integer> map);
	
	ConcurrentHashMap<status, Integer> getStatus();
	void setStatus(ConcurrentHashMap<status, Integer> map);
}
