package module.event.map.instance.chapter0;

import java.io.BufferedReader;

import module.battle.chapter0.DormKeeperBattle;
import module.battle.chapter0.ShadowBattle;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.mission.api.IMission;
import module.mission.chapter0.ContainerTutorialMission;
import module.mission.chapter0.FirstTimeSeeKeeperMission;
import module.mission.chapter0.MainMission;
import module.mission.chapter0.TwoDoorsMission;
import module.server.PlayerServer;
import module.utility.BattleUtil;
import module.utility.EventUtil;
import module.utility.IOUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("102,100,3", new AbstractEvent(){
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
					if (mm == null ||
							mm.getState() == MainMission.State.AFTER_OPENING ||
							mm.getState() == MainMission.State.AFTER_FIRST_BATTLE)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (mm == null) {
					g.setInEvent(true);
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
					g.setInEvent(true);
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
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"mission\"或\"m\"指令能夠讓你查看自己目前身上有哪些任務\n");
							buf.append("以及接下來可以採取哪些行動。在遊戲進行時如果忘記自己接下來要做什麼，\n");
							buf.append("這會是一個很好用的指令喔!");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"mission\"或\"m\"來查看當前任務。\n");
							input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("mission") && !input.equals("m")){
								g.getAtRoom().informRoom("請輸入\"mission\"或\"m\"來查看當前任務。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
							mm.setState(MainMission.State.START_SEARCHING);
							String[] msg7 = {"mission"};
							CommandServer.readCommand(pg, msg7);
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("說明：隊伍移動時直接輸入方位名稱即可，方位就如同之前提過的那樣，\n");
							buf.append("共有\"東/西/南/北/上/下\"六種可能的情況，對應指令分別為\"east/\n");
							buf.append("west/south/north/up/down\"，或簡寫為\"e/w/s/n/u/d\"。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("說明：當你輸入\"look\"觀察所在房間的資訊時，房間敘述下面會顯示\n");
							buf.append("出口資訊，用中括號包起來。值得注意的是，出口資訊只會顯示當下可以\n");
							buf.append("直接行走的方向，其他沒顯示的方向可能只是門關起來了，不一定就是\n");
							buf.append("沒有出口喔! 因此平常就可以輸入不同的方位來探索看看有沒有自己還\n");
							buf.append("沒發現的通道!");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("說明：以上是進行本遊戲會用到的基本指令。現在再整理一下：\n");
							buf.append("\"<look/l>\"可以用來觀察所在房間的資訊。\n");
							buf.append("\"<look/l> <方位代稱>\"可以用來觀察某方向的資訊。\n");
							buf.append("\"<talk/ta> <角色名稱>\"可以讓你與某人對話。\n");
							buf.append("\"<attack/at> <角色名稱>\"可以讓你攻擊某位角色並進入戰鬥。\n");
							buf.append("\"<inventory/i>\"可以讓你查看身上的物品。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("\"<equipment/eq>\"可以讓你查看隊伍中角色的裝備情況。\n");
							buf.append("\"<get/g> <物品名稱>\"可以讓你撿起地上的某件特定物品。\n");
							buf.append("\"<drop/dr> <物品名稱>\"可以讓你丟下身上的某件特定物品。\n");
							buf.append("\"<wear/wea> <裝備名稱>\"可以讓角色穿上某件裝備。\n");
							buf.append("\"<open/o> <方位名稱>\"可以讓角色開啟某方向的門。\n");
							buf.append("\"<close/cl> <方位名稱>\"可以讓角色關閉某方向的門。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("\"<unlock/un> <方位名稱>\"可以讓角色解開某方向的門鎖。\n");
							buf.append("\"<lock/loc> <方位名稱>\"可以讓角色鎖上某方向的門。\n");
							buf.append("\"<e/w/s/n/u/d>\"可以讓隊伍往\"東/西/南/北/上/下\"移動。\n");
							buf.append("\"<time/t>\"可以讓你查看遊戲中的時間。\n");
							buf.append("\"<mission/m>\"可以讓你查看目前身上有哪些任務，以及接下來的方向。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("以上所有的指令皆可輸入\"<help> <指令名稱>\"來查詢詳細的\n");
							buf.append("說明，亦可以直接輸入\"help\"來查看可以使用的指令有哪些。\n");
							buf.append("遊戲中的指令說明暫時告一段落，接著就實際體驗文字遊戲帶給你\n");
							buf.append("的感受吧!");
							EventUtil.informCheckReset(pg, buf, in);
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					mm.setState(MainMission.State.START_SEARCHING);
					pg.setInEvent(false);
				}
			}
		});
		
		EventUtil.mapEventMap.put("100,92,1", new AbstractEvent(){
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
							.get(TwoDoorsMission.class.toString());
					if (tdm == null ||
							tdm.south == false)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerGroup pg = (PlayerGroup) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("這裡被雜物堆滿了，要出去顯然不可能。");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.north == false){
						buf.append("去北邊的門看看有沒有機會。");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informGroup(pg, "跳過劇情。\n");
				}
				tdm.south = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					buf.append("去宿舍管理中心看看吧!");
					g.getAtRoom().informRoom(buf.toString() + "\n");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,103,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
							.get(TwoDoorsMission.class.toString());
					if (tdm == null ||
							tdm.north == false)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerGroup pg = (PlayerGroup) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("這裡自動門壞了，打不開。");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.south == false){
						buf.append("去南邊的門看看有沒有機會。");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informGroup(pg, "跳過劇情。\n");
				}
				tdm.north = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					buf.append("去宿舍管理中心看看吧!");
					g.getAtRoom().informRoom(buf.toString() + "\n");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
			
		});
		
		EventUtil.mapEventMap.put("101,92,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.FOUND_DOORS_BLOCKED)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
				if (mm.getState() == MainMission.State.FOUND_DOORS_BLOCKED){
					g.setInEvent(true);
					mm.setState(MainMission.State.BEFORE_BREAK_MANAGE_DOOR);
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						buf.append("霍華：南邊就是宿舍管理室了，不過看來也是鎖著的。\n");
						buf.append("嗯~門上有玻璃，找個沉重、堅硬一點的東西應該可以破壞吧!");
						EventUtil.informCheckReset(pg, buf, in);
						String[] msg = {"look", "s"};
						CommandServer.readCommand(pg, msg);
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("霍華：只好去找找看有沒有什麼可用的工具了。\n");
						g.getAtRoom().informRoom(buf.toString());
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("101,91,2", new AbstractEvent() {

			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					IMission m = PlayerServer.getMissionMap().get(ContainerTutorialMission.class.toString());
					if (m == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				if (pg.getConfigData().get(config.TUTORIAL_ON)){
					g.setInEvent(true);
					PlayerServer.getMissionMap().put(ContainerTutorialMission.class.toString(), 
							new ContainerTutorialMission());
					try {
						StringBuffer buf = new StringBuffer();
						BufferedReader in = pg.getInFromClient();
						buf.append("霍華：是宿舍的冰箱耶，雖然平常沒什麼在用，但現在這種誇張的時刻\n");
						buf.append("，還是看一下裡面有沒有什麼可以用的東西吧。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("說明：遊戲中的\"容器\"也是物品的一種，差別在於它能夠用來盛裝\n");
						buf.append("其他物品。容器在顯示時會順便顯示它的狀態為\"開/關\"。你一樣\n");
						buf.append("可以用物品指令存取它，但同時也能夠用\"open/close/lock/unlock\"\n");
						buf.append("來對它進行操作。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("現在試著輸入\"open refrigerator\"來打開冰箱：\n");
						String input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("open refrigerator")){
							g.getAtRoom().informRoom("現在試著輸入\"open refrigerator\"來打開冰箱：\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg = {"open", "refrigerator"};
						CommandServer.readCommand(pg, msg);
						buf.append("霍華：來看看裡面有沒有什麼可用的東西吧!\n");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("說明：你可以使用\"look\"指令來查看一個容器中的物品，前提是這個容器\n");
						buf.append("是開著的。指令格式為\"<look> <in> <容器名稱>\"。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("請輸入\"look in refrigerator\"來查看冰箱內有哪些物品：\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("look in refrigerator")){
							g.getAtRoom().informRoom("請輸入\"look in refrigerator\"來查看冰箱內有哪些物品：\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg2 = {"look", "in", "refrigerator"};
						CommandServer.readCommand(pg, msg2);
						buf.append("霍華：正好口有點渴了，這杯果汁看起來...算了，我賭它沒壞!");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("說明：你可以使用\"get\"指令將物品從容器中拿出來，指令格式為\n");
						buf.append("\"<get> <物品名稱> <容器名稱>\"，另外也能夠使用\"put\"指令\n");
						buf.append("將身上的物品放入容器內。指令格式為\"<put> <物品名稱> <容器名稱>\"。\n");
						buf.append("詳細說明可以輸入\"help get\"以及\"help put\"來查詢。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("請輸入\"get juice refrigerator\"來從冰箱內拿出果汁：\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("get juice refrigerator")){
							g.getAtRoom().informRoom("請輸入\"get juice refrigerator\"來從冰箱內拿出果汁：\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg3 = {"get", "juice", "refrigerator"};
						CommandServer.readCommand(pg, msg3);
						buf.append("說明：你可以輸入\"use\"指令來使用某些可使用的物品，指令格式為\n");
						buf.append("\"<use> <物品名稱>\"或\"<use> <物品名稱> <目標名稱>\"。物品的使用\n");
						buf.append("效果能夠輸入\"<look> <物品名稱>\"來觀看到。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("請輸入\"use juice\"來喝果汁：\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("use juice")){
							g.getAtRoom().informRoom("請輸入\"use juice\"來喝果汁：\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg4 = {"use", "juice"};
						CommandServer.readCommand(pg, msg4);
						buf.append("說明：使用物品時，若不指定使用對象，則參照各個物品的屬性來判斷，沒有\n");
						buf.append("一定的行為。若指定使用對象，則一定會作用在該對象身上，即便該對象是敵\n");
						buf.append("人。指定對象的使用指令格式為\"<use> <物品名稱> <對象名稱>\"。若對\n");
						buf.append("象為我方隊伍內的角色，則需輸入\"<team> <角色名稱>\"。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("舉例來說，果汁在不指定使用者的情況下會直接被該角色使用，但你同樣可以\n");
						buf.append("輸入\"use juice team enf\"來指定霍華喝這杯果汁。基本上，回復類的\n");
						buf.append("物品都能夠不指定使用者而直接回復該角色的狀態。相對的，會造成傷害的物品\n");
						buf.append("則不會在不指定使用者的情況下直接傷害使用者。另外，在戰鬥中使用物品會\n");
						buf.append("消耗角色一回合的時間。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("說明到此結束，冰箱中還有一塊蛋糕，你可以考慮一起帶上它!");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "跳過劇情。\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("100,91,1", new AbstractEvent() {
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					FirstTimeSeeKeeperMission ftskm = (FirstTimeSeeKeeperMission) PlayerServer
							.getMissionMap().get(FirstTimeSeeKeeperMission.class.toString());
					if (ftskm == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerServer.getMissionMap().put(FirstTimeSeeKeeperMission.class.toString(), 
						new FirstTimeSeeKeeperMission());
				EventUtil.executeEventMessage((PlayerGroup) g, "first time see keeper");
				Group gg = g.getAtRoom().getGroupList().findGroup("keeper");
				new DormKeeperBattle(gg, g);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,101,3", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_DEFEAT_MANAGER)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerGroup) g, "find_hydrualic_cut");
				((MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString())).setState(
								MainMission.State.AFTER_FOUND_CUTTER);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("104,110,1", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_EXIT_DORMITORY)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerGroup) g, "see_shadow");
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				mm.setState(MainMission.State.FIGHT_WITH_SHADOW);
				Group enemyG = g.getAtRoom().getGroupList().findGroup("shadow");
				new ShadowBattle(enemyG, g);
				g.setInEvent(false);
			}
			
		});
	}
}
