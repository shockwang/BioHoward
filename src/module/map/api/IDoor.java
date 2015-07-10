package module.map.api;

import java.io.Serializable;

import module.character.api.ICharacter;
import module.map.PositionDoor;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;

public interface IDoor extends Serializable {
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
	void setKeyName(String name);
	String getKeyName();
	boolean onLock(ICharacter c);
	boolean onUnlock(ICharacter c);
}
