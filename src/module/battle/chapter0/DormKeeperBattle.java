package module.battle.chapter0;

import module.battle.BattleTask;
import module.character.ICharacter;
import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CConfig.config;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class DormKeeperBattle extends BattleTask{

	public DormKeeperBattle(ICharacter team1, ICharacter team2) {
		super(team1, team2);
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
				if (c.getMyGroup() instanceof PlayerCharacter) {
					// add event code here
					MainMission mm = (MainMission) PlayerServer
							.getMissionMap().get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_BREAK_MANAGE_DOOR){
						if (c.getCurrentAttribute(attribute.HP) < 40){
							this.isBlocked = true;
							PlayerCharacter pg = (PlayerCharacter) c.getMyGroup();
							pg.setInEvent(true);
							EventUtil.executeEventMessage(pg, "keeper_attack_hard");
							if (pg.getConfigData().get(config.TUTORIAL_ON))
								EventUtil.executeEventMessage(pg, "flee_tutorial");
							mm.setState(MainMission.State.AFTER_FLEE_FROM_MANAGER);
							pg.setInEvent(false);
							this.isBlocked = false;
						}
					}
					// event code end
					if (((PlayerCharacter) c.getMyGroup()).getConfigData().get(
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

}
