package module.map;

import java.util.concurrent.ConcurrentHashMap;

import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.Command;
import module.item.api.IItem;
import module.map.api.IRoom;
import module.map.constants.CExit;
import module.map.constants.CExit.exit;

public class BaseRoom implements IRoom{
	private Position pos = null;
	private String title = null;
	private String description = null;
	private ConcurrentHashMap<exit, Neighbor> exitMap = null;
	private GroupList gList = null;
	
	public BaseRoom(){
		exitMap = new ConcurrentHashMap<exit, Neighbor>();
		gList = new GroupList();
	}
	
	@Override
	public void setPosition(int x, int y, int z) {
		this.pos = new Position(x, y, z);
	}

	@Override
	public Position getPosition() {
		return this.pos;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String displayRoom() {
		// define the show mechanism when a player "look" at a room
		StringBuffer outBuffer = new StringBuffer();
		outBuffer.append(this.title + "\n");
		outBuffer.append(this.description + "\n");
		outBuffer.append(CExit.displayRoomExits(this));
		outBuffer.append(this.gList.displayInfo());
		return outBuffer.toString();
	}

	@Override
	public ConcurrentHashMap<exit, Neighbor> getExits() {
		return this.exitMap;
	}

	@Override
	public void setExits(ConcurrentHashMap<exit, Neighbor> exits) {
		this.exitMap = exits;
	}

	@Override
	public void setSingleExit(exit way, Neighbor link) {
		this.exitMap.put(way, link);
	}

	@Override
	public Neighbor getSingleExit(exit way) {
		return this.exitMap.get(way);
	}

	@Override
	public void removeSingleExit(exit way) {
		this.exitMap.remove(way);
	}

	@Override
	public GroupList getGroupList() {
		return this.gList;
	}

	@Override
	public ICharacter searchCharByName(String groupName, String name) {
		return this.gList.findChar(groupName, name);
	}

	@Override
	public IItem searchItemByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void informRoom(String message) {
		for (Group g : this.gList.gList){
			CommandServer.informGroup(g, message);
		}
	}

	@Override
	public Command roomCommand(String message) {
		return null;
	}

	@Override
	public String displayRoomExceptGroup(Group g) {
		StringBuffer outBuffer = new StringBuffer();
		outBuffer.append(this.title + "\n");
		outBuffer.append(this.description + "\n");
		outBuffer.append(CExit.displayRoomExits(this));
		outBuffer.append(this.gList.displayInfoExceptGroup(g));
		return outBuffer.toString();
	}

}
