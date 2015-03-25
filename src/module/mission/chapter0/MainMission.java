package module.mission.chapter0;

import module.mission.api.IMission;

public class MainMission implements IMission{
	private String missionName = "�k�X�M�j";
	
	public enum State{
		BEFORE_OPENING("�}�Y�@���e"),
		AFTER_OPENING("�}�Y�@����"),
		DONE("����!");
		
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
