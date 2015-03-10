package module.map.api;

import java.util.concurrent.ConcurrentHashMap;

import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.command.api.Command;
import module.item.api.IItem;
import module.map.Neighbor;
import module.map.Position;
import module.map.constants.CExit;

public interface IRoom {
	// interface for map unit in the game

	// position information
	void setPosition(int x, int y, int z);
	Position getPosition();
	
	// title, name of the room
	void setTitle(String title);
	String getTitle();
	void setDescription(String description);
	String getDescription();
	String displayRoom(); 
	String displayRoomExceptGroup(Group g);
	
	// exits & neighbor rooms & door
	ConcurrentHashMap<CExit.exit, Neighbor> getExits();
	void setExits(ConcurrentHashMap<CExit.exit, Neighbor> exits);
	void setSingleExit(CExit.exit way, Neighbor link);
	Neighbor getSingleExit(CExit.exit way);
	void removeSingleExit(CExit.exit way);
	
	// search & edit objects
	GroupList getGroupList();
	ICharacter searchCharByName(String groupName, String name);
	
	IItem searchItemByName(String name);
	
	// inform message to creatures in room
	void informRoom(String message);
	
	// special commands in the room
	Command roomCommand(String message);
}
