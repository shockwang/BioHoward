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
					if (((PlayerGroup) c.getMyGroup()).getConfigData().get(config.TUTORIAL_ON)){
						isBlocked = true;
						playerMoveCount++;
						boolean skip = doEvent((PlayerGroup) c.getMyGroup());
						isBlocked = false;
						if (skip) continue;
					}
					
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
			g.getAtRoom().informRoom("<ENTER>\n");
			IOUtil.clearBufferedReader(in);
			buf.append("霍華心想：嗚嗚嗚，身上有什麼可以用的東西嗎?");
			EventUtil.informReset(g, buf, in);
			buf.append("說明：輸入\"inventory\"或\"i\"可查看現在身上帶的物品有哪些。\n");
			buf.append("請輸入\"inventory\"或\"i\"來查看身上的物品。");
			g.getAtRoom().informRoom(buf.toString() + "\n");
			buf.setLength(0);
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("inventory") && !input.equals("i")){
				g.getAtRoom().informRoom("請輸入\"inventory\"或\"i\"來查看身上的物品。\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg = {"inventory"};
			CommandServer.readCommand(g, msg);
			buf.append("霍華咬著牙：\"普物課本又厚又重，就拿這個來應戰吧。\"");
			EventUtil.informReset(g, buf, in);
			buf.append("說明：你可以使用\"wear\"指令來讓角色穿上某件裝備。指令格式為\n");
			buf.append("\"<角色英文名字> <wear> <裝備名稱>\"。亦可使用\"remove\"\n");
			buf.append("指令來脫下裝備。格式為\"<角色英文名字> <remove> <裝備名稱>\"。\n");
			buf.append("若最前面不加上角色名稱，則自動指定為隊伍中第一個角色進行動作。\n");
			buf.append("在戰鬥中wear/remove皆消耗角色一回合的時間。詳細說明可輸入\n");
			buf.append("\"help wear\"以及\"help remove\"來查詢。");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("請輸入\"wear book\"將普物課本裝備起來。\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("wear book")){
				g.getAtRoom().informRoom("請輸入\"wear book\"將普物課本裝備起來。\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg2 = {"wear", "book"};
			CommandServer.readCommand(g, msg2);
			return true;
		case 2:
			g.getAtRoom().informRoom("<ENTER>\n");
			IOUtil.clearBufferedReader(in);
			buf.append("霍華：\"沒辦法了，只能先設法把他打暈，不然連我自己都性命難保!\"");
			EventUtil.informReset(g, buf, in);
			buf.append("說明：你可以輸入\"equipment\"或\"eq\"來查看隊伍中角色的裝備\n");
			buf.append("情況，指令格式為\"<角色英文名字> <equipment>\"。若前面不輸入\n");
			buf.append("角色名稱，則自動指定隊伍中第一位角色作為操作對象。\n");
			buf.append("詳細說明可輸入\"help equipment\"來查詢。");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("請輸入\"equipment\"或\"eq\"來查看霍華的裝備。\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("equipment") && !input.equals("eq")){
				g.getAtRoom().informRoom("請輸入\"equipment\"或\"eq\"來查看霍華的裝備。\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg3 = {"equipment"};
			CommandServer.readCommand(g, msg3);
			buf.append("說明：普通攻擊指令為\"attack\"或\"at\"，指令格式為\n");
			buf.append("\"<我方角色名稱> <attack> <敵方角色名稱>\"。若不指定我方\n");
			buf.append("角色名稱，則自動指定隊伍中的第一位角色來動作。另外，在戰鬥中\n");
			buf.append("若不指定特定敵方目標，則自動指定敵方隊伍中第一位角色進行攻擊\n");
			buf.append("。詳細說明可輸入\"help attack\"來查詢。");
			EventUtil.informReset(g, buf, in);
			g.getAtRoom().informRoom("請輸入\"at roommate\"或\"at\"來攻擊室友。\n");
			input = IOUtil.readLineFromClientSocket(in);
			while (!input.equals("at roommate") && !input.equals("at")){
				g.getAtRoom().informRoom("請輸入\"at roommate\"或\"at\"來攻擊室友。\n");
				input = IOUtil.readLineFromClientSocket(in);
			}
			String[] msg4 = {"at", "roommate"};
			CommandServer.readCommand(g, msg4);
			g.setInEvent(false);
			return true;
		case 5:
			buf.append("提示：重複輸入同樣的指令很煩嗎? 你可以利用\"!\"指令代表你\n");
			buf.append("上一次輸入的指令喔! 你可以試著使用\"!\"來代替\"at\"或\n");
			buf.append("\"at roommate\"。\n");
			g.getAtRoom().informRoom(buf.toString());
		}
		return false;
	}
	
	
}
