package module.map.api;

import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.api.ICharacter;
import module.command.api.ICommand;
import module.item.ItemList;
import module.item.api.IItem;
import module.map.Neighbor;
import module.map.Position;
import module.map.constants.CExit;

public interface IRoom {
	// interface for map unit in the game

	// position information
	void setPosition(Position pos);
	Position getPosition();
	
	// title, name of the room
	void setTitle(String title);
	String getTitle();
	void setDescription(String description);
	String getDescription();
	String displayRoom(); 
	String displayRoomExceptCharacter(ICharacter c);
	
	// exits & neighbor rooms & door
	ConcurrentHashMap<CExit.exit, Neighbor> getExits();
	void setExits(ConcurrentHashMap<CExit.exit, Neighbor> exits);
	void setSingleExit(CExit.exit way, Neighbor link);
	Neighbor getSingleExit(CExit.exit way);
	void removeSingleExit(CExit.exit way);
	
	// search & edit objects
	CharList getCharList();
	
	IItem searchItemByName(String name);
	
	// inform message to creatures in room
	void informRoom(String message);
	void informRoomExceptCharacter(ICharacter c, String message);
	
	// special commands in the room
	ICommand roomCommand(String message);
	
	// item list
	void setItemList(ItemList list);
	ItemList getItemList();
	
	// special command when in this room
	boolean specialCommand(String msg); // true if special command match
}
