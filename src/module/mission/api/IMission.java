package module.mission.api;

import java.io.Serializable;

public interface IMission extends Serializable{
	// provide a base mission interface
	String getMissionName();
	
	void setState(Object state);
	Object getState();
	
	// provide display state view
	String displayState();
}
