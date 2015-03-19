package module.mission;

import module.mission.api.IMission;

public class TestMission implements IMission{
	private String missionName = "龍族小說";
	
	public enum State{
		TALK_WITH_MING("小明請你幫他把書從小美那裏討回來。"),
		TALK_WITH_MEI("已經從小美那裏拿到書，回去找小明講話吧。"),
		DONE("");
		
		public String name;
		
		State(String name){
			this.name = name;
		}
	}
	private State state = State.TALK_WITH_MING;

	@Override
	public void setState(Object state) {
		this.state = (State) state;
	}

	@Override
	public Object getState() {
		return this.state;
	}

	@Override
	public String getMissionName() {
		return missionName;
	}

	@Override
	public String displayState() {
		StringBuffer buf = new StringBuffer();
		if (state != State.DONE){
			buf.append("\u2606" + missionName + "\n");
			buf.append("   " + state.name + "\n");
		}
		return buf.toString();
	}
}
