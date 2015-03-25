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
				CommandServer.informGroup(g, "你聽到一聲哇哈哈~\n");
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
						buf.append("這是開頭劇情...可惡der霍華! ...<ENTER>");
						g.getAtRoom().informRoom(buf.toString() + "\n");
						EventUtil.checkSkipEvent(in.readLine());
						buf.setLength(0);
						buf.append("特地為你寫一個遊戲, 真是太抬舉你啦!!! ...<ENTER>");
						g.getAtRoom().informRoom(buf.toString() + "\n");
						EventUtil.checkSkipEvent(in.readLine());
						buf.setLength(0);
						buf.append("接下來就正式開始囉!");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
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
