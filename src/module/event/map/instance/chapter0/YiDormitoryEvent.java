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
						buf.append("開頭劇情敘述 ...");
						EventUtil.informCheckReset(g, buf, in);
						
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					mm.setState(MainMission.State.AFTER_OPENING);
					
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						buf.append("霍華：嗚呃...好奇怪的夢。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("霍華心想：張開眼睛看看房間吧.\n");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("請輸入\"look\"來觀察你現在所處的房間狀態。");
							g.getAtRoom().informRoom(buf.toString() + "\n");
							buf.setLength(0);
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look")){
								buf.append("請輸入\"look\"來觀察你現在所處的房間狀態。");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
							String[] msg = {"look"};
							CommandServer.readCommand(pg, msg);
							buf.append("\"look\"指令能用來觀察你現在所處的房間狀態，包括在場的其他角色或者\n");
							buf.append("掉落在該場景的地上物品都能夠看到。");
							
						} else {
							String[] msg = {"look"};
							CommandServer.readCommand(pg, msg);
						}
						EventUtil.informCheckReset(pg, buf, in);
						
						// talk to roommate
						buf.append("霍華看到室友一動不動的，下去叫他。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("在非戰鬥中能夠使用\"talk\"指令來和同一房間內的角色交談，格式為：\n");
							buf.append("\"<talk> <角色名稱>\"或是\"<talk> <隊伍名稱> <角色名稱>\"\n");
							buf.append("室友目前並非存在多人隊伍內，因此使用\"talk roommate\"來與他交談。\n");
							buf.append("可輸入\"help talk\"來取得對此指令的詳細說明。");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"talk roommate\"來與室友交談。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("talk roommate")){
								buf.append("請輸入\"talk roommate\"來與室友交談。");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
							String[] msg = {"talk", "roommate"};
							CommandServer.readCommand(pg, msg);
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
				}
			}
		});
	}
}
