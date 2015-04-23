package module.mission.chapter0;

import module.mission.api.IMission;

public class MainMission implements IMission{
	private String missionName = "�k�X�M�j";
	
	public enum State{
		BEFORE_OPENING("�}�Y�@���e", 0),
		AFTER_OPENING("�}�Y�@����", 1),
		AFTER_FIRST_BATTLE("���b�оǥ��Ȥ�", 2),
		START_SEARCHING("�]�k�k�X�J�١A���h�@�Ӥj���ݬݡC", 3),
		FOUND_DOORS_BLOCKED("�J�٤@�Ӫ��⮰�j�����Q�ʦ�F�A�h�J�ٺ޲z���ߧ��ݦ��S����\n���򦳥Ϊ��귽�C", 4),
		BEFORE_BREAK_MANAGE_DOOR("�M��A�X�Ψӥ��}�J�ٺ޲z���ߪ��W�������u��C", 5),
		AFTER_BREAK_MANAGE_DOOR("���i�J�ٺ޲z���ߴM��i�Ϊ��귽�C", 6),
		AFTER_FLEE_FROM_MANAGER("�N���������Q����F�A�n�p������O? �γ\�i�H�յۮ��������Q�o?", 7),
		AFTER_DEFEAT_MANAGER("����J���_�͡A�h�U�өж��M��i�Ϊ��u��C", 8),
		AFTER_FOUND_CUTTER("���o���ŤF�A�h�@�өж���@�ӥi�}�a���K���A���_��k�X�h�C", 9),
		AFTER_EXIT_DORMITORY("�n���e�������J�٤F�A���H�ϵ}�֪���V�e�i�~���קK�M�I�C", 10),
		FIGHT_WITH_SHADOW("�~�M�b�o�̹J�W�F�i�Ȫ��ĤH�A�ɥ��O�N�����ѧa!", 11),
		AFTER_DEFEATED("�N�سQ���ѤF�A���٤H�Ƥ��C", 12),
		DONE("����!", 100);
		
		public String name;
		public int index;
		
		State(String name, int index){
			this.name = name;
			this.index = index;
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
