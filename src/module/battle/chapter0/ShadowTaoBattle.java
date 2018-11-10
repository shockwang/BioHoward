package module.battle.chapter0;

import module.battle.BattleTask;
import module.character.ICharacter;
import module.character.PlayerCharacter;
import module.command.CommandServer;
import module.map.api.IRoom;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class ShadowTaoBattle extends BattleTask{
	private IRoom here;
	
	public ShadowTaoBattle(ICharacter team1, ICharacter team2) {
		super(team1, team2);
		here = team1.getAtRoom();
	}
	
	@Override
	public void checkBattleEnd() {
		super.checkBattleEnd();
		PlayerCharacter pg = (PlayerCharacter) here.getGroupList().findGroup("enf");
		ICharacter taoG = here.getGroupList().findGroup("tao's");
		taoG.setInEvent(true);
		pg.setInEvent(true);
		EventUtil.executeEventMessage(pg, "after_tao_beat_shadow");
		
		// end the game for chapter 0
		((MainMission) PlayerServer.getMissionMap()
			.get(MainMission.class.toString())).setState(MainMission.State.DONE);
		String[] msg = {"quit"};
		CommandServer.readCommand(pg, msg);
		String[] msg2 = {"y"};
		CommandServer.readCommand(pg, msg2);
	}
}
