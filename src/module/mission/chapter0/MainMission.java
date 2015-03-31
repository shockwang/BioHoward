package module.mission.chapter0;

import module.mission.api.IMission;

public class MainMission implements IMission{
	private String missionName = "逃出清大";
	
	public enum State{
		BEFORE_OPENING("開頭劇情前"),
		AFTER_OPENING("開頭劇情後"),
		AFTER_FIRST_BATTLE("正在教學任務中"),
		START_SEARCHING("設法逃出宿舍，先去一樓大門看看吧。"),
		DONE("結束!");
		
		public String name;
		
		State(String name){
			this.name = name;
		}
	}
	private State state = State.BEFORE_OPENING;
	
	@Override
	public String getMissionName() {
		return missionName;
	}

	@Override
	public void setState(Object state) {
		this.state = (State) state;
	}

	@Override
	public Object getState() {
		return state;
	}

	@Override
	public String displayState() {
		StringBuffer buf = new StringBuffer();
		if (state != State.DONE){
			buf.append("\u2605" + missionName + "\n");
			buf.append("   " + state.name + "\n");
		}
		return buf.toString();
	}

}
