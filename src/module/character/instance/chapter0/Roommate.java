package module.character.instance.chapter0;

import module.battle.BattleTask;
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
		String desc = "霍華的室友。平時會和霍華一起打CS，一起講垃圾話等等。但他現\n";
		desc += "在看起來很不正常，臉上發狂的表情以及布滿血絲的雙眼都讓你感覺到他的\n";
		desc += "危險性。在你還來不及確認發生了什麼事之前，他已經迅速地向你撲過來。";
		this.setDesc(desc);
		this.addAttribute(attribute.HP, 50);
		
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		EventUtil.executeEventMessage(g, "talk_to_roommate");
		if (g.getConfigData().get(config.TUTORIAL_ON)){
			EventUtil.executeEventMessage(g, "battle_tutorial");
			// TODO: add modify config method & explanation
			// buf.append("此項設定的預設值為\"非即時戰鬥\"，你可以透過blabla修改它。");
			new FirstTutorialBattle(this.getMyGroup(), g);
		}
		else {
			new BattleTask(this.getMyGroup(), g);
			g.setInEvent(false);
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
		pg.setInBattle(false);
		pg.setBattleTask(null);
		EventUtil.doRoomEvent(pg);
	}
}
