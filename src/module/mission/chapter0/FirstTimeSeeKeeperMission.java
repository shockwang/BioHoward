package module.mission.chapter0;

import module.mission.api.IMission;

public class FirstTimeSeeKeeperMission implements IMission{
	
	@Override
	public String getMissionName() {
		return "第一次遇到齋媽";
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
