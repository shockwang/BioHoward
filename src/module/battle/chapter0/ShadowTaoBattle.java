package module.battle.chapter0;

import module.battle.BattleTask;
import module.character.Group;
import module.character.PlayerGroup;
import module.map.api.IRoom;

public class ShadowTaoBattle extends BattleTask{
	private IRoom here;
	
	public ShadowTaoBattle(Group team1, Group team2) {
		super(team1, team2);
		here = team1.getAtRoom();
	}
	
	@Override
	public void checkBattleEnd() {
		super.checkBattleEnd();
		PlayerGroup pg = (PlayerGroup) here.getGroupList().findGroup("enf");
		Group taoG = here.getGroupList().findGroup("tao");
		taoG.setInEvent(true);
		// TODO: add story here
	}
}
