package module.map;

import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.SingleCharList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.ItemList;
import module.item.api.IItem;
import module.map.api.IRoom;
import module.map.constants.CExit;
import module.map.constants.CExit.exit;

public class BaseRoom implements IRoom{
	private Position pos = null;
	private String title = null;
	private String description = null;
	private ConcurrentHashMap<exit, Neighbor> exitMap = null;
	private CharList cList = null;
	private ItemList itemList = null;
	
	public BaseRoom(){
		exitMap = new ConcurrentHashMap<exit, Neighbor>();
		cList = new CharList();
		itemList = new ItemList();
	}
	
	@Override
	public void setPosition(Position pos) {
		this.pos = pos;
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
		outBuffer.append(this.itemList.displayInfo());
		outBuffer.append(this.cList.displayInfo());
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
	public IItem searchItemByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void informRoom(String message) {
		for (SingleCharList scl : this.cList.charList) {
			for (ICharacter c : scl.list) {
				CommandServer.informCharacter(c, message);
			}
		}
	}

	@Override
	public ICommand roomCommand(String message) {
		return null;
	}

	@Override
	public String displayRoomExceptCharacter(ICharacter c) {
		StringBuffer outBuffer = new StringBuffer();
		outBuffer.append(this.title + "\n");
		outBuffer.append(this.description + "\n");
		outBuffer.append(CExit.displayRoomExits(this));
		outBuffer.append(this.itemList.displayInfo());
		outBuffer.append(this.cList.displayInfoExceptChar(c));
		return outBuffer.toString();
	}

	@Override
	public void setItemList(ItemList list) {
		this.itemList = list;
	}

	@Override
	public ItemList getItemList() {
		return this.itemList;
	}

	@Override
	public boolean specialCommand(String msg) {
		// default do nothing
		return false;
	}

	@Override
	public CharList getCharList() {
		return this.cList;
	}

	@Override
	public void informRoomExceptCharacter(ICharacter c, String message) {
		for (SingleCharList scl : this.cList.charList) {
			for (ICharacter target : scl.list){
				if (target == c) continue;
				CommandServer.informCharacterNoChange(target, message);
			}
		}
	}
}
