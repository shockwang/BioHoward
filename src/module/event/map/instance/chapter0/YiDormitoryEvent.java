package module.event.map.instance.chapter0;

import java.io.BufferedReader;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("101,100,3", new AbstractEvent(){
			@Override
			public void doEvent(Group g) {
				CommandServer.informGroup(g, "�Ať��@�n�z����~\n");
			}
		});
		
		EventUtil.mapEventMap.put("102,100,3", new AbstractEvent(){

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (mm == null) {
					mm = new MainMission();
					PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						buf.append("�}�Y�@���ԭz ...");
						EventUtil.informCheckReset(g, buf, in);
						
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					mm.setState(MainMission.State.AFTER_OPENING);
					
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						buf.append("�N�ءG��c...�n�_�Ǫ��ڡC");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�N�ؤ߷Q�G�i�}�����ݬݩж��a.\n");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�п�J\"look\"���[��A�{�b�ҳB���ж����A�C");
							g.getAtRoom().informRoom(buf.toString() + "\n");
							buf.setLength(0);
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look")){
								buf.append("�п�J\"look\"���[��A�{�b�ҳB���ж����A�C");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
							String[] msg = {"look"};
							CommandServer.readCommand(pg, msg);
							buf.append("\"look\"���O��Ψ��[��A�{�b�ҳB���ж����A�A�]�A�b������L����Ϊ�\n");
							buf.append("�����b�ӳ������a�W���~������ݨ�C");
							
						} else {
							String[] msg = {"look"};
							CommandServer.readCommand(pg, msg);
						}
						EventUtil.informCheckReset(pg, buf, in);
						
						// talk to roommate
						buf.append("�N�جݨ�Ǥͤ@�ʤ��ʪ��A�U�h�s�L�C");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�b�D�԰�������ϥ�\"talk\"���O�өM�P�@�ж����������͡A�榡���G\n");
							buf.append("\"<talk> <����W��>\"�άO\"<talk> <����W��> <����W��>\"\n");
							buf.append("�Ǥͥثe�ëD�s�b�h�H����A�]���ϥ�\"talk roommate\"�ӻP�L��͡C\n");
							buf.append("�i��J\"help talk\"�Ө��o�惡���O���Բӻ����C");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"talk roommate\"�ӻP�Ǥͥ�͡C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("talk roommate")){
								buf.append("�п�J\"talk roommate\"�ӻP�Ǥͥ�͡C");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
							String[] msg = {"talk", "roommate"};
							CommandServer.readCommand(pg, msg);
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
				}
			}
		});
	}
}
