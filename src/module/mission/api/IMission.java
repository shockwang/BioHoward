package module.mission.api;

public interface IMission {
	// provide a base mission interface
	String getMissionName();
	
	void setState(Object state);
	Object getState();
	
	// provide display state view
	String displayState();
}
