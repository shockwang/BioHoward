package module.map.constants;

public class CDoorAttribute {
	public static enum doorAttribute{
		LOCKABLE, 
		UNLOCKABLE;
	}
	
	public static enum doorStatus{
		OPENED, 
		CLOSED, 
		LOCKED, 
		BROKEN; // broken door?
	}
	
	public static doorAttribute parseDoorAttributeFromString(String name){
		if (name.equals("lockable")) return doorAttribute.LOCKABLE;
		else if (name.equals("unlockable")) return doorAttribute.UNLOCKABLE;
		else return null;
	}
	
	public static doorStatus parseDoorStatusFromString(String name){
		if (name.equals("opened")) return doorStatus.OPENED;
		else if (name.equals("closed")) return doorStatus.CLOSED;
		else if (name.equals("locked")) return doorStatus.LOCKED;
		else if (name.equals("broken")) return doorStatus.BROKEN;
		else return null;
	}
}
