package module.item.api;

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
	
	attribute getAttribute();
	void setAttribute(attribute atr);
	
	status getStatus();
	void setStatus(status s);
}
