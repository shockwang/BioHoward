package module.map.api;

import module.character.api.ICharacter;
import module.item.api.IItem;
import module.map.PositionDoor;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;

public interface IDoor {
	// description
	void setDescription(String des);
	String getDescription();
	
	// identify the door
	void setDoorPosition(PositionDoor pd);
	PositionDoor getDoorPosition();
	
	// identify the door attribute
	void setDoorAttribute(doorAttribute da);
	doorAttribute getDoorAttribute();
	void setDoorStatus(doorStatus ds);
	doorStatus getDoorStatus();
	
	// lock/unlock the door
	void setKey(IItem key);
	IItem getKey();
	boolean onLock(ICharacter c);
	boolean onUnlock(ICharacter c);
}
