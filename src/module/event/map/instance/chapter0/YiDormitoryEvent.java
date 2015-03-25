package module.event.map.instance.chapter0;

import java.io.BufferedReader;
import java.io.IOException;

import module.character.Group;
import module.character.PlayerGroup;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

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
						buf.append("�o�O�}�Y�@��...�i�cder�N��! ...<ENTER>");
						g.getAtRoom().informRoom(buf.toString() + "\n");
						EventUtil.checkSkipEvent(in.readLine());
						buf.setLength(0);
						buf.append("�S�a���A�g�@�ӹC��, �u�O�ө��|�A��!!! ...<ENTER>");
						g.getAtRoom().informRoom(buf.toString() + "\n");
						EventUtil.checkSkipEvent(in.readLine());
						buf.setLength(0);
						buf.append("���U�ӴN�����}�l�o!");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					} catch (IOException e){
						// TODO: handle IOEXception
						// sadly the section is closed...
						e.printStackTrace();
					}
					String[] msg = {"l"};
					CommandServer.readCommand(pg, msg);
					mm.setState(MainMission.State.AFTER_OPENING);
				}
			}
			
		});
	}
}
