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
							buf.append("說明：\"look\"指令能用來觀察你現在所處的房間狀態，包括在場的其他角色或者\n");
							buf.append("掉落在該場景的地上物品都能夠看到。亦可簡寫為\"l\"。詳細說明可輸入\n");
							buf.append("\"help look\"來查詢。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("請輸入\"look\"或\"l\"來觀察你現在所處的房間狀態。");
							g.getAtRoom().informRoom(buf.toString() + "\n");
							buf.setLength(0);
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look") && !input.equals("l")){
								buf.append("請輸入\"look\"或\"l\"來觀察你現在所處的房間狀態。");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
						} 
						String[] msg = {"look"};
						CommandServer.readCommand(pg, msg);
						EventUtil.informCheckReset(pg, buf, in);
						
						// talk to roommate
						buf.append("霍華看到室友一動不動的，下去叫他。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：在非戰鬥中能夠使用\"talk\"指令來和同一房間內的角色交談，格式為：\n");
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
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					String[] msg2 = {"talk", "roommate"};
					CommandServer.readCommand(pg, msg2);
				} else if (mm.getState() == MainMission.State.AFTER_FIRST_BATTLE){
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						buf.append("霍華：呼~終於幹掉了，但這學校是怎麼回事?");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("霍華看地上有鑰匙，準備撿起來。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"get\"指令可以讓你撿起掉落在地上的東西，指令格式為：\n");
							buf.append("\"<我方角色名稱> <get> <物品名稱>\"。與此相對的，\"drop\"指令\n");
							buf.append("可以讓你丟下身上的物品，指令格式為\"<我方角色名稱> <drop> <物品名稱>\"。\n");
							buf.append("若不指定我方角色，則自動選擇隊伍中的第一人進行動作。另外，在戰鬥中get/drop\n");
							buf.append("皆花費一回合的行動。詳細說明可輸入\"help get\"以及\"help drop\"來查詢。");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"get key\"來撿起宿舍鑰匙。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("get key")){
								g.getAtRoom().informRoom("請輸入\"get key\"來撿起宿舍鑰匙。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg = {"get", "key"};
						CommandServer.readCommand(pg, msg);
						buf.append("霍華：打開門出去看看好了。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"unlock\"指令可以讓你解開鎖住的門，前提是你身上帶著合適\n");
							buf.append("的鑰匙。相對的，\"lock\"指令則是可以讓你鎖住一扇門。指令格式為\n");
							buf.append("\"<我方角色名稱> <lock/unlock> <方位名稱>\"，其中方位名稱指定你想往\n");
							buf.append("哪個方向開門。若不指定我方角色名稱，則自動選擇隊伍中第一人進行動作。\n");
							buf.append("詳細說明可輸入\"help lock\"以及\"help unlock\"來查詢。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("說明：遊戲中的方位共有\"東/西/南/北/上/下\"六種，在指令上分別對應\n");
							buf.append("為\"east/west/south/north/up/down\"。輸入方位名稱時可以只輸入\n");
							buf.append("簡寫，分別為\"e/w/s/n/u/d\"。");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"unlock w\"來解開房間的門鎖。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("unlock w")){
								g.getAtRoom().informRoom("請輸入\"unlock w\"來解開房間的門鎖。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg2 = {"unlock", "w"};
						CommandServer.readCommand(pg, msg2);
						buf.append("霍華：打開門出去看看吧。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"open\"指令可以讓你打開一扇關著的門，前提是它沒有上鎖。\n");
							buf.append("相對的，\"close\"指令則可以讓你關上一扇開著的門。指令格式為\n");
							buf.append("\"<我方角色名稱> <open/close> <方位名稱>\"。若不指定我方角色\n");
							buf.append("，則自動選擇隊伍中第一人進行動做。詳細說明可輸入\"help open\"\n");
							buf.append("以及\"help close\"來查詢。");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"open w\"來打開房間的門。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("open w")){
								g.getAtRoom().informRoom("請輸入\"open w\"來打開房間的門。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg3 = {"open", "w"};
						CommandServer.readCommand(pg, msg3);
						buf.append("出門前看一下外面情況吧。");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"look\"指令除了觀察自己所在位置的資訊外，還可以用來觀察\n");
							buf.append("與其相連的位置情況。指令格式為\"<look> <方位名稱>\"。若該方位有\n");
							buf.append("房間相連，則你會看到該房間的狀況；若該方位有關著的門，則你會看到\n");
							buf.append("該扇門的敘述。");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"look w\"來觀察門外的情況。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look w")){
								g.getAtRoom().informRoom("請輸入\"look w\"來觀察門外的情況。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg4 = {"look", "w"};
						CommandServer.readCommand(pg, msg4);
						buf.append("霍華：喔喔喔好像有點危險，先把門關上休息一下吧。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("請輸入\"close w\"來關上宿舍房間的門。\n");
						String input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("close w")){
							g.getAtRoom().informRoom("請輸入\"close w\"來關上宿舍房間的門。\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg5 = {"close", "w"};
						CommandServer.readCommand(pg, msg5);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：隨著遊戲中的時間流逝，角色的體力會漸漸恢復。所以角色狀態\n");
							buf.append("低下時可以考慮找個安全的地方默默待上幾分鐘喔! 遊戲中的時間對應\n");
							buf.append("到現實中為\"2秒鐘現實時間 = 1分鐘遊戲時間\"。你可以輸入\"time\"\n");
							buf.append("指令來得知遊戲中的時間。");
							EventUtil.informCheckReset(pg, buf, in);
						}
						buf.append("霍華；好~的~休息夠了之後就出發探索吧!");
						EventUtil.informCheckReset(pg, buf, in);
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					mm.setState(MainMission.State.START_SEARCHING);
					pg.setInEvent(false);
				} else if (mm.getState() == MainMission.State.START_SEARCHING){
					// TODO: define actions after battle
					g.getAtRoom().informRoom("start searching~~~\n");
				}
			}
		});
	}
}
