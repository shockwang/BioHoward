package module.mission.chapter0;

import module.mission.api.IMission;

public class TwoDoorsMission implements IMission{
	private String name = "檢查宿舍大門";
	
	public enum State{
		DISCOVERED_ONE_DOOR,
		DONE;
	}
	public State state = State.DISCOVERED_ONE_DOOR;
	public boolean south = false;
	public boolean north = false;
	
	@Override
	public String getMissionName() {
		return name;
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
			buf.append("\u2606" + name + "\n");
			buf.append("   再去宿舍");
			if (south) buf.append("北");
			else if (north) buf.append("南");
			buf.append("側的大門看看。\n");
		}
		return buf.toString();
	}

}
