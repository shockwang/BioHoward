package module.item.api;

import module.character.api.ICharacter;
import module.item.ItemList;
import module.map.constants.CDoorAttribute;

public interface IContainer extends IItem{
	enum Type {
		MOVEABLE,
		FIXED_POSITION,
		TREASURE_BOX
	}
	
	void displayContent(ICharacter c);
	boolean onGetContent(ICharacter c, String target); // true if success
	boolean onPutContent(ICharacter c, String target); // true if success
	
	ItemList getItemList();
	
	void setType(Type t);
	Type getType();
	
	// container status & attribute, use from door setup
	void setAttribute(CDoorAttribute.doorAttribute attr);
	CDoorAttribute.doorAttribute getAttribute();
	
	void setStatus(CDoorAttribute.doorStatus status);
	CDoorAttribute.doorStatus getStatus();
	
	boolean onLock(ICharacter c);
	boolean onUnlock(ICharacter c);
}
