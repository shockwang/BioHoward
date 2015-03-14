package module.mission;

import module.mission.api.IMission;

public class TestMission implements IMission{
	public enum State{
		TALK_WITH_MING,
		TALK_WITH_MEI,
		DONE
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
}
