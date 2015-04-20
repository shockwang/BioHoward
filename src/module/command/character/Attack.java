package module.command.character;

import module.battle.BattleTask;
import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.BattleUtil;
import module.utility.HelpUtil;
import module.utility.Parse;

public class Attack implements ICommand {
	private String[] name;

	public Attack() {
		name = new String[2];
		name[0] = "attack";
		name[1] = "at";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			ICharacter target = null;
			if (g.getInBattle()) {
				GroupList enemyList = g.getBattleTask().getEnemyGroups(c);
				if (command.length == 2) {
					target = enemyList.findFirstAliveChar();
				} else {
					String ttt = Parse.mergeString(command, 2, ' ');
					target = g.getAtRoom().getGroupList()
							.findCharExceptGroup(g, ttt);
					if (target != null) {
						if (target.getMyGroup() == g) {
							CommandServer.informGroup(g, "�A���o�򬽤߷Q�����ۤv���P��?\n");
							return false;
						} else {
							if (!(target.getMyGroup().getInBattle())) {
								if (target.getMyGroup().getTalking()){
									CommandServer.informGroup(g, "�H�a���b���ܡA���@�U�a�C\n");
									return false;
								}
								// add the group to enemyList
								g.getBattleTask().addBattleGroup(
										enemyList.gList.get(0),
										target.getMyGroup());
								//attackMechanism(c, target);
								BattleUtil.attackMechanism(c, target);
								return true;
							}
						}
					} else
						target = enemyList.findAliveChar(command[2]);
				}
			} else {
				if (command.length == 2) {
					CommandServer.informGroup(g,
							String.format("�A�Q��%s�����֩O?\n", c.getChiName()));
					return false;
				} else {
					String ttt = Parse.mergeString(command, 2, ' ');
					target = g.getAtRoom().getGroupList()
							.findCharExceptGroup(g, ttt);
					if (target != null) {
						if (target.getMyGroup() == g) {
							CommandServer.informGroup(g, "�A���o�򬽤߷Q�����ۤv���P��?\n");
							return false;
						} else {
							if (!target.getMyGroup().getInBattle()) {
								if (target.getMyGroup().getTalking()){
									CommandServer.informGroup(g, "�H�a���b���ܡA���@�U�a�C\n");
									return false;
								}
								// attack mechanism
								new BattleTask(g, target.getMyGroup());
								//attackMechanism(c, target);
								BattleUtil.attackMechanism(c, target);
								return false;
							} else {
								target.getMyGroup()
										.getBattleTask()
										.addBattleOppositeGroup(
												target.getMyGroup(), g);
							}
						}
					}
				}
			}

			if (target != null) {
				// attack mechanism
				//attackMechanism(c, target);
				BattleUtil.attackMechanism(c, target);
				return true;
			} else {
				CommandServer.informGroup(g, "�A�n��������H���b�o��.\n");
				return false;
			}
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/attack.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		
		return output;
	}
}
