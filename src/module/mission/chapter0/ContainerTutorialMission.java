package module.mission.chapter0;

import module.mission.api.IMission;

public class ContainerTutorialMission implements IMission{
	private String name = "�e���ϥαо�";
	
	@Override
	public String getMissionName() {
		return name;
	}

	@Override
	public void setState(Object state) {
		// this mission doesn't need state, do nothing
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
