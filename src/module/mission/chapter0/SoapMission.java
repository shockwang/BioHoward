package module.mission.chapter0;

import module.mission.api.IMission;

public class SoapMission implements IMission{
	private String name = "ªÎ¨m¨Æ¥ó";
	
	@Override
	public String getMissionName() {
		return name;
	}

	@Override
	public void setState(Object state) {
		// do nothing
	}

	@Override
	public Object getState() {
		return null;
	}

	@Override
	public String displayState() {
		return "";
	}

}
