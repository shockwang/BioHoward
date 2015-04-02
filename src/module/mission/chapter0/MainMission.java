package module.mission.chapter0;

import module.mission.api.IMission;

public class MainMission implements IMission{
	private String missionName = "逃出清大";
	
	public enum State{
		BEFORE_OPENING("開頭劇情前"),
		AFTER_OPENING("開頭劇情後"),
		AFTER_FIRST_BATTLE("正在教學任務中"),
		START_SEARCHING("設法逃出宿舍，先去一樓大門看看。"),
		FOUND_DOORS_BLOCKED("宿舍一樓的兩扇大門都被封住了，去宿舍管理中心找找看有沒什麼\n什麼有用的資源。"),
		BEFORE_BREAK_MANAGE_DOOR("尋找適合用來打破宿舍管理中心門上玻璃的工具。"),
		AFTER_BREAK_MANAGE_DOOR("爬進宿舍管理中心尋找可用的資源。"),
		AFTER_FLEE_FROM_MANAGER("齋媽的攻擊十分凌厲，要如何應對呢? 或許可以試著拿滅火器噴她?"),
		AFTER_DEFEAT_MANAGER("拿到宿舍鑰匙，去各個房間尋找可用的工具。"),
		AFTER_FOUND_CUTTER("找到油壓剪了，去一樓房間找一個可破壞的鐵窗，剪斷後逃出去。"),
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
