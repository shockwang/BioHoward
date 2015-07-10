package module.map;

import java.io.Serializable;

import module.map.api.IDoor;
import module.map.api.IRoom;

public class Neighbor implements Serializable{
	private IRoom room;
	private IDoor door;
	
	public Neighbor (IRoom room, IDoor door){
		this.room = room;
		this.door = door;
	}
	
	public Neighbor (IRoom room){
		this.room = room;
		this.door = null;
	}
	
	public void setRoom(IRoom room) {this.room = room;}
	
	public IRoom getRoom(){return room;}
	
	public void setDoor(IDoor door) {this.door = door;}
	
	public IDoor getDoor() {return this.door;}
}
