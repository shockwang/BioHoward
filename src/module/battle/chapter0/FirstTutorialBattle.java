package module.battle.chapter0;

import java.io.BufferedReader;

import module.battle.BattleTask;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.utility.EventUtil;
import module.utility.IOUtil;

public class FirstTutorialBattle extends BattleTask{
	private int playerMoveCount;
	private boolean isBlocked = false;
	
	public FirstTutorialBattle(Group team1, Group team2) {
		super(team1, team2);
		playerMoveCount = 0;
	}
	
	@Override
	public void run() {
		if (isBlocked) return;
		synchronized (this) {
			ready = updateTime();
			updatePlayerStatus();
		}
		updatePlayerStatus(team1List.gList);
		updatePlayerStatus(team2List.gList);
		try {
			for (ICharacter c : ready) {
				if (c.getMyGroup() instanceof PlayerGroup) {
					isBlocked = true;
					playerMoveCount++;
					boolean skip = doEvent((PlayerGroup) c.getMyGroup());
					isBlocked = false;
					if (skip) continue;
					
					if (((PlayerGroup) c.getMyGroup()).getConfigData().get(
							config.REALTIMEBATTLE)) {
						// real time battle

					} else {
						// blocks when a character in player's group is ready
						try {
							synchronized (this) {
								wait();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				c.battleAction(getEnemyGroups(c));
			}
		} catch (NullPointerException e) {
			return; // no one is ready, return
		}
	}
	
	private boolean doEvent(PlayerGroup g){
		StringBuffer buf = new StringBuffer();
		BufferedReader in = g.getInFromClient();
		String input;
		switch (playerMoveCount){
		case 1: 
			buf.append("�N�ؤ߷Q�G����A���W������i�H�Ϊ��F���?");
			EventUtil.informReset(g, buf, in);
			buf.append("�����G��J\"inventory\"��\"i\"�i�d�ݲ{�b���W�a�����~�����ǡC\n");
			buf.append("�п�J\"inventory\"�Ӭd�ݨ��W�����~�C");
			g.getAtRoom().informRoom(buf.toString() + "\n");
			buf.setLength(0);
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("inventory")){
				g.getAtRoom().informRoom("�п�J\"inventory\"�Ӭd�ݨ��W�����~�C\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg = {"inventory"};
			CommandServer.readCommand(g, msg);
			buf.append("�N�ب������ҥ������i�ΡA�ǳƸ˳ơC");
			EventUtil.informReset(g, buf, in);
			buf.append("�����G�A�i�H�ϥ�\"wear\"���O���������W�Y��˳ơC���O�榡��\n");
			buf.append("\"<����^��W�r> <wear> <�˳ƦW��>\"�C��i�ϥ�\"remove\"\n");
			buf.append("���O�Ӳ�U�˳ơC�����\"<����^��W�r> <remove> <�˳ƦW��>\"�C\n");
			buf.append("�Y�̫e�����[�W����W�١A�h�۰ʫ��w������Ĥ@�Ө���i��ʧ@�C\n");
			buf.append("�b�԰���wear/remove�Ү��Ө���@�^�X���ɶ��C�Բӻ����i��J\n");
			buf.append("\"help wear\"�H��\"help remove\"�Ӭd�ߡC");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("�п�J\"wear book\"�N�����ҥ��˳ư_�ӡC\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("wear book")){
				g.getAtRoom().informRoom("�п�J\"wear book\"�N�����ҥ��˳ư_�ӡC\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg2 = {"wear", "book"};
			CommandServer.readCommand(g, msg2);
			return true;
		case 2:
			buf.append("�N�ءG�n��~�ǳƶ}��!");
			EventUtil.informReset(g, buf, in);
			buf.append("�����G�A�i�H��J\"equipment\"��\"eq\"�Ӭd�ݶ�����⪺�˳�\n");
			buf.append("���p�A���O�榡��\"<����^��W�r> <equipment>\"�C�Y�e������J\n");
			buf.append("����W�١A�h�۰ʫ��w����Ĥ@�쨤��@���ާ@��H�C\n");
			buf.append("�Բӻ����i��J\"help equipment\"�Ӭd�ߡC");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("�п�J\"equipment\"�Ӭd���N�ت��˳ơC\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("equipment")){
				g.getAtRoom().informRoom("�п�J\"equipment\"�Ӭd���N�ت��˳ơC\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg3 = {"equipment"};
			CommandServer.readCommand(g, msg3);
			buf.append("�����G���q�������O��\"attack\"��\"at\"�A���O�榡��\n");
			buf.append("\"<�ڤ訤��W��> <attack> <�Ĥ訤��W��>\"�C�Y�����w�ڤ�\n");
			buf.append("����W�١A�h�۰ʫ��w������Ĥ@�쨤��Ӱʧ@�C�Բӻ����i��J\n");
			buf.append("\"help attack\"�Ӭd�ߡC");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("�п�J\"at roommate\"�ӧ����Ǥ͡C\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("at roommate")){
				g.getAtRoom().informRoom("�п�J\"at roommate\"�ӧ����Ǥ͡C\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg4 = {"at", "roommate"};
			CommandServer.readCommand(g, msg4);
			g.setInEvent(false);
			return true;
		}
		return false;
	}
}
