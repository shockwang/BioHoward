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
}
