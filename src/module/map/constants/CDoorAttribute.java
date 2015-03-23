package module.map.constants;

public class CDoorAttribute {
	public static enum doorAttribute{
		LOCKABLE, 
		UNLOCKABLE,
		BROKEN; // broken door, can not modify status
	}
	
	public static enum doorStatus{
		OPENED, 
		CLOSED, 
		LOCKED, 
		
	}
	
	public static doorAttribute parseDoorAttributeFromString(String name){
		if (name.equals("lockable")) return doorAttribute.LOCKABLE;
		else if (name.equals("unlockable")) return doorAttribute.UNLOCKABLE;
		else if (name.equals("broken")) return doorAttribute.BROKEN;
		else return null;
	}
	
	public static doorStatus parseDoorStatusFromString(String name){
		if (name.equals("opened")) return doorStatus.OPENED;
		else if (name.equals("closed")) return doorStatus.CLOSED;
		else if (name.equals("locked")) return doorStatus.LOCKED;
		else return null;
	}
}
