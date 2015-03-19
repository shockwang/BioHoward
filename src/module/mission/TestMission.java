package module.mission;

import module.mission.api.IMission;

public class TestMission implements IMission{
	private String missionName = "�s�ڤp��";
	
	public enum State{
		TALK_WITH_MING("�p���ЧA���L��ѱq�p�����ذQ�^�ӡC"),
		TALK_WITH_MEI("�w�g�q�p�����خ���ѡA�^�h��p�����ܧa�C"),
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
