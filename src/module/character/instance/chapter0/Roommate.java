package module.character.instance.chapter0;

import java.io.BufferedReader;

import module.battle.chapter0.FirstTutorialBattle;
import module.character.BaseHumanCharacter;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CConfig.config;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class Roommate extends BaseHumanCharacter{
	
	public Roommate(){
		this("室友", "enf's roommate");
	}
	
	public Roommate(String chiName, String engName) {
		super(chiName, engName);
		// TODO: modify description
		this.setDesc("就是室友");
		this.addAttribute(attribute.HP, 50);
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		StringBuffer buf = new StringBuffer();
		BufferedReader in = g.getInFromClient();
		buf.append("霍華叫室友第一次沒反應。");
		EventUtil.informReset(g, buf, in);
		buf.append("霍華叫室友第二次沒反應。");
		EventUtil.informReset(g, buf, in);
		buf.append("霍華拍室友，室友吼吼吼吼~~");
		g.getAtRoom().informRoom(buf.toString() + "\n");
		buf.setLength(0);
		if (g.getConfigData().get(config.TUTORIAL_ON)){
			buf.append("戰鬥說明：\n");
			buf.append("遊戲中的戰鬥採兩方陣營的時間條戰鬥方式。進入戰鬥後，中間狀態\n");
			buf.append("列的左邊會出現該角色在戰鬥中的時間條，當時間條增加至100%時\n");
			buf.append("便可行動。在除此之外的時間對該角色下指令則會得到\"該角色尚未\n");
			buf.append("準備好\"的回應。");
			EventUtil.informReset(g, buf, in);
			buf.append("此外，遊戲中戰鬥模式可選擇\"即時戰鬥/非即時戰鬥\"兩種。在即時戰鬥\n");
			buf.append("中，即便我方角色的時間條增加至100%，其他人的行動依舊不會停止，會繼續\n");
			buf.append("增加時間條。必須等到玩家對該角色進行操作後，該角色的時間條才會重新開始\n");
			buf.append("計算。也就是說，打字速度不夠快的人可能會因此被敵人多打好幾下喔...請慎重\n");
			buf.append("選擇。");
			EventUtil.informReset(g, buf, in);
			buf.append("反之，非即時戰鬥則是在我方角色時間條增至100%時，同一場戰鬥內的角色皆會\n");
			buf.append("停下動作，等待該角色行動完畢之後再繼續增加各自的時間條。然而需要注意：即便\n");
			buf.append("同一場戰鬥中的時間條暫停了，遊戲內的時間依舊在運行，未加入該場戰鬥的角色也\n");
			buf.append("能夠自由行動。");
			EventUtil.informReset(g, buf, in);
			// TODO: add modify config method & explanation
			buf.append("此項設定的預設值為\"非即時戰鬥\"，你可以透過blabla修改它。");
			EventUtil.informReset(g, buf, in);
			new FirstTutorialBattle(this.getMyGroup(), g);
		}
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
	
	@Override
	public void doEventWhenGroupDown(PlayerGroup pg){
		MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
		mm.setState(MainMission.State.AFTER_FIRST_BATTLE);
		EventUtil.doRoomEvent(pg);
	}
}
