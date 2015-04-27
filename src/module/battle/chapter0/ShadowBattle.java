package module.battle.chapter0;

import module.battle.BattleTask;
import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.instance.main.Dawdw;
import module.character.instance.main.Tao;
import module.command.CommandServer;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.BattleUtil;
import module.utility.EventUtil;
import module.utility.ItemUtil;
import module.utility.MapUtil;

public class ShadowBattle extends BattleTask{
	private PlayerGroup ggg;
	
	public ShadowBattle(Group team1, Group team2) {
		super(team1, team2);
	}
	
	@Override
	protected boolean checkGroupListDown(GroupList list) {
		boolean allDown = true;
		boolean groupDown;
		boolean isDown = false;
		while (!isDown) {
			isDown = true;
			for (Group g : list.gList) {
				groupDown = true;
				for (CharList cList : g.list) {
					for (ICharacter c : cList.charList) {
						if (!c.isDown()) {
							allDown = false;
							groupDown = false;
						}
					}
				}
				if (groupDown) {
					if (g instanceof PlayerGroup) {
						CommandServer.informGroup(g, "你的隊伍全滅了...\n");
						ggg = (PlayerGroup) g;
						return true;
					} else {
						ItemUtil.createLootingItem(g);
						// group inventory drop to the ground
						ItemUtil.dropAllItemOnDefeat(g);
						// do group down event if there's any
						GroupList oppositeGroups = getEnemyGroups(g);
						PlayerGroup pg = null;
						for (Group px : oppositeGroups.gList){
							if (px instanceof PlayerGroup){
								pg = (PlayerGroup) px;
								break;
							}
						}
						if (pg != null){
							this.isBlocked = true;
							g.list.get(0).charList.get(0).doEventWhenGroupDown(pg);
							this.isBlocked = false;
						}
					}
					
					// a group is down, remove data from this battle
					removeGroupFromTimeMap(g);
					g.getAtRoom().getGroupList().gList.remove(g);
					g.setAtRoom(null);
					g.setInBattle(false);
					g.setBattleTask(null);
					list.gList.remove(g);
					isDown = false;
					break;
				}
			}
		}
		return allDown;
	}
	
	@Override
	public void checkBattleEnd() {
		super.checkBattleEnd();
		ggg.setInEvent(true);
		((MainMission) PlayerServer.getMissionMap().get(
				MainMission.class.toString())).setState(MainMission.State.AFTER_DEFEATED);
		EventUtil.executeEventMessage(ggg, "after_defeated_by_shadow");
		Group taoG = new Group(new Tao());
		taoG.addChar(new Dawdw());
		MapUtil.initializeGroupAtMap(taoG, ggg.getAtRoom());
		
		Group shadowG = ggg.getAtRoom().getGroupList().findGroup("shadow");
		BattleUtil.attackMechanism(taoG.findAliveChar("tao"), shadowG.findAliveChar("shadow"));
		new ShadowTaoBattle(taoG, shadowG);
		ggg.setInEvent(false);
		ggg.setTalking(true);
	}
}
