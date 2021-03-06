package module.event.map.instance.chapter0;

import java.io.BufferedReader;

import module.battle.BattleTask;
import module.battle.chapter0.DormKeeperBattle;
import module.battle.chapter0.ShadowBattle;
import module.character.ICharacter;
import module.character.PlayerCharacter;
import module.character.constants.CConfig.config;
import module.character.instance.chapter0.DarkShadow;
import module.character.instance.chapter0.MadStudent;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.item.instance.chapter0.Soap;
import module.map.api.IRoom;
import module.mission.api.IMission;
import module.mission.chapter0.ContainerTutorialMission;
import module.mission.chapter0.FirstTimeSeeKeeperMission;
import module.mission.chapter0.MainMission;
import module.mission.chapter0.SoapMission;
import module.mission.chapter0.TwoDoorsMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;
import module.utility.MapUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("102,100,3", new AbstractEvent(){
			@Override
			public boolean isTriggered(ICharacter g){
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
			public void doEvent(ICharacter g) {
				PlayerCharacter pg = (PlayerCharacter) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (mm == null) {
					g.setInEvent(true);
					mm = new MainMission();
					PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
					mm.setState(MainMission.State.AFTER_OPENING);
					
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						EventUtil.executeEventMessage(pg, "after_wake_up");
						
						buf.append("霍華心想：張開眼睛看看房間吧.\n");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "look_tutorial");
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
						EventUtil.executeEventMessage(pg, "after_see_room");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "talk_tutorial");
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
						CommandServer.informCharacter(pg, "跳過劇情。\n");
					}
					String[] msg2 = {"talk", "roommate"};
					CommandServer.readCommand(pg, msg2);
				} else if (mm.getState() == MainMission.State.AFTER_FIRST_BATTLE){
					g.setInEvent(true);
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						EventUtil.executeEventMessage(pg, "after_beat_roommate");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "get_tutorial");
							g.getAtRoom().informRoom("請輸入\"get key\"來撿起宿舍鑰匙。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("get key")){
								g.getAtRoom().informRoom("請輸入\"get key\"來撿起宿舍鑰匙。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg = {"get", "key"};
						CommandServer.readCommand(pg, msg);
						EventUtil.executeEventMessage(pg, "after_get_key");
						
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
						buf.append("霍華：\"打開門出去看看吧。\"");
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
						buf.append("霍華：\"外面也可以看到奇怪的人，我還是先把門關上休息一下。\"");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)) {
							g.getAtRoom().informRoom("請輸入\"close w\"來關上宿舍房間的門。\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("close w")){
								g.getAtRoom().informRoom("請輸入\"close w\"來關上宿舍房間的門。\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
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
						buf.append("霍華發現宿舍的燈突然不亮了，所有電器用品也都開不起來。顯然不\n");
						buf.append("只是宿舍發生問題，或許整個新竹都出狀況了。\"看來不能一直待在\n");
						buf.append("這裡...\" 霍華默想著，\"稍作休息之後就出發去探索吧，首先要\n");
						buf.append("想辦法安全離開這個宿舍。\"");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("說明：\"mission\"或\"m\"指令能夠讓你查看自己目前身上有哪些任務\n");
							buf.append("以及接下來可以採取哪些行動。在遊戲進行時如果忘記自己接下來要做什麼，\n");
							buf.append("這會是一個很好用的指令喔!");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("請輸入\"mission\"或\"m\"來查看當前任務。\n");
							String input = IOUtil.readLineFromClientSocket(in);
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
							buf.append("\"<time>\"可以讓你查看遊戲中的時間。\n");
							buf.append("\"<mission/m>\"可以讓你查看目前身上有哪些任務，以及接下來的方向。");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("以上所有的指令皆可輸入\"<help> <指令名稱>\"來查詢詳細的\n");
							buf.append("說明，亦可以直接輸入\"help\"來查看可以使用的指令有哪些。\n");
							buf.append("遊戲中的指令說明暫時告一段落，接著就實際體驗文字遊戲帶給你\n");
							buf.append("的感受吧!");
							EventUtil.informCheckReset(pg, buf, in);
						}
					} catch (SkipEventException e){
						CommandServer.informCharacter(pg, "跳過劇情。\n");
					}
					mm.setState(MainMission.State.START_SEARCHING);
					pg.setInEvent(false);
				}
			}
		});
		
		EventUtil.mapEventMap.put("100,92,1", new AbstractEvent(){
			@Override
			public boolean isTriggered(ICharacter g){
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
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				PlayerCharacter pg = (PlayerCharacter) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("霍華來到宿舍的玄關，看見西邊有一道自動門。然而，不但由於斷電\n");
					buf.append("的緣故，導致自動門無法開啟，門的開啟方向也早已被硬物撐住，整\n");
					buf.append("個卡死了。");
					EventUtil.informCheckReset(pg, buf, in);
					buf.append("霍華看著在外面徘徊的學生，心中暗想：\"我能理解為什麼要把門堵\n");
					buf.append("起來了...如果從這裡把玻璃打破，外面那群傢伙絕對馬上包圍過來，\n");
					buf.append("想從這裡出去顯然太危險。\"");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.north == false){
						buf.append("霍華：\"去北邊的門看看有沒有機會。\"");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informCharacter(pg, "跳過劇情。\n");
				}
				tdm.south = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					EventUtil.executeEventMessage(pg, "after_two_door");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,103,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(ICharacter g){
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
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				PlayerCharacter pg = (PlayerCharacter) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("\"哇賽...\" 霍華瞪著西邊門的方向猛看。\"不但門扭曲變形，\n");
					buf.append("堆積的雜物也多到不像話，這我可沒辦法弄開啊...\"");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.south == false){
						buf.append("霍華：\"去南邊的門看看有沒有機會。\"");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informCharacter(pg, "跳過劇情。\n");
				}
				tdm.north = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					EventUtil.executeEventMessage(pg, "after_two_door");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
			
		});
		
		EventUtil.mapEventMap.put("101,92,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.FOUND_DOORS_BLOCKED)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(ICharacter g) {
				PlayerCharacter pg = (PlayerCharacter) g;
				
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
						buf.append("說明：當房間敘述或是門上的敘述出現\"用小括弧包起來的英文\"\n");
						buf.append("時，代表在這個特定的房間可以輸入此特定指令來發揮功效。在其他\n");
						buf.append("房間輸入則不會有反應。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("以這個例子來說，你可以在這個地方嘗試輸入\"crash\"與\"climb\"\n");
						buf.append("來發揮他們的效果。但在其他房間輸入則不會有反應。上述出現的提示\n");
						buf.append("訊息是輸入\"look s\"查看南方關著的門而得到的敘述，此外在任務\n");
						buf.append("提示內也有記錄可輸入的指令名稱。");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("霍華：只好去找找看有沒有什麼可用的工具了。\n");
						g.getAtRoom().informRoom(buf.toString());
					} catch (SkipEventException e){
						CommandServer.informCharacter(pg, "跳過劇情。\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("101,91,2", new AbstractEvent() {

			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					IMission m = PlayerServer.getMissionMap().get(ContainerTutorialMission.class.toString());
					if (m == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(ICharacter g) {
				PlayerCharacter pg = (PlayerCharacter) g;
				
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
						buf.append("\"<use> <物品名稱>\"或\"<use> <物品名稱> <目標名稱>\"。\n");
						buf.append("物品的使用效果能夠輸入\"<look> <物品名稱>\"來觀看到。");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("請輸入\"use juice\"來喝果汁：\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("use juice")){
							g.getAtRoom().informRoom("請輸入\"use juice\"來喝果汁：\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg4 = {"use", "juice"};
						CommandServer.readCommand(pg, msg4);
						EventUtil.executeEventMessage(pg, "use_item_tutorial");
					} catch (SkipEventException e){
						CommandServer.informCharacter(pg, "跳過劇情。\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("100,91,1", new AbstractEvent() {
			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					FirstTimeSeeKeeperMission ftskm = (FirstTimeSeeKeeperMission) PlayerServer
							.getMissionMap().get(FirstTimeSeeKeeperMission.class.toString());
					if (ftskm == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				PlayerServer.getMissionMap().put(FirstTimeSeeKeeperMission.class.toString(), 
						new FirstTimeSeeKeeperMission());
				EventUtil.executeEventMessage((PlayerCharacter) g, "first time see keeper");
				ICharacter gg = g.getAtRoom().getGroupList().findGroup("keeper");
				new DormKeeperBattle(gg, g);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,101,3", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_DEFEAT_MANAGER)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerCharacter) g, "find_hydrualic_cut");
				((MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString())).setState(
								MainMission.State.AFTER_FOUND_CUTTER);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("104,110,1", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_EXIT_DORMITORY)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerCharacter) g, "see_shadow");
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				mm.setState(MainMission.State.FIGHT_WITH_SHADOW);
				
				// create shadow group
				ICharacter enemyG = new ICharacter(new DarkShadow());
				enemyG.setIsRespawn(false);
				MapUtil.initializeCharacterAtMap(enemyG, g.getAtRoom());
				new ShadowBattle(enemyG, g);
				g.setInEvent(false);
			}
			
		});
		
		EventUtil.mapEventMap.put("103,103,1", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(ICharacter g){
				if (super.isTriggered(g)){
					if (PlayerServer.getMissionMap().get(SoapMission.class.toString()) == null)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(ICharacter g) {
				g.setInEvent(true);
				PlayerServer.getMissionMap().put(SoapMission.class.toString(), 
						new SoapMission());
				IRoom here = g.getAtRoom();
				here.getItemList().addItem(new Soap());
				EventUtil.executeEventMessage((PlayerCharacter) g, "soap_event");
				ICharacter enemyG = new ICharacter(new MadStudent());
				enemyG.setIsRespawn(false);
				MapUtil.initializeCharacterAtMap(enemyG, here);
				new BattleTask(enemyG, g);
				g.setInEvent(false);
			}
			
		});
	}
}
