package module.mission.chapter0;

import module.mission.api.IMission;

public class MainMission implements IMission{
	private String missionName = "�k�X�M�j";
	
	public enum State{
		BEFORE_OPENING("�}�Y�@���e"),
		AFTER_OPENING("�}�Y�@����"),
		AFTER_FIRST_BATTLE("���b�оǥ��Ȥ�"),
		START_SEARCHING("�]�k�k�X�J�١A���h�@�Ӥj���ݬݡC"),
		FOUND_DOORS_BLOCKED("�J�٤@�Ӫ��⮰�j�����Q�ʦ�F�A�h�J�ٺ޲z���ߧ��ݦ��S����\n���򦳥Ϊ��귽�C"),
		BEFORE_BREAK_MANAGE_DOOR("�M��A�X�Ψӥ��}�J�ٺ޲z���ߪ��W�������u��C"),
		AFTER_BREAK_MANAGE_DOOR("���i�J�ٺ޲z���ߴM��i�Ϊ��귽�C"),
		AFTER_FLEE_FROM_MANAGER("�N���������Q����F�A�n�p������O? �γ\�i�H�յۮ��������Q�o?"),
		AFTER_DEFEAT_MANAGER("����J���_�͡A�h�U�өж��M��i�Ϊ��u��C"),
		AFTER_FOUND_CUTTER("���o���ŤF�A�h�@�өж���@�ӥi�}�a���K���A���_��k�X�h�C"),
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
