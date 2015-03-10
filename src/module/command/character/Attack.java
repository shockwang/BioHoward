package module.command.character;

import module.battle.BattleTask;
import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.command.CommandServer;
import module.command.api.Command;
import module.utility.Battle;
import module.utility.Parse;

public class Attack implements Command {
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
		// TODO Auto-generated method stub
		// start parse from command[2]
		Group g = c.getMyGroup();

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
							// add the group to enemyList
							attackMechanism(c, target);
							g.getBattleTask().addBattleGroup(
									enemyList.gList.get(0), target.getMyGroup());
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
						if (!g.getInBattle()) {
							// attack mechanism
							attackMechanism(c, target);
							new BattleTask(g, target.getMyGroup());
							return false;
						}
					}
				}
			}
		}

		if (target != null) {
			// attack mechanism
			attackMechanism(c, target);
			return true;
		} else {
			CommandServer.informGroup(g, "�A�n��������H���b�o��.\n");
			return false;
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	private void attackMechanism(ICharacter src, ICharacter target) {
		String out = String.format("%s�V%s���X�@��, ", src.getChiName(),
				target.getChiName());

		if (target.getAttributeMap().get(attribute.HP) != null) {
			out += String.format("��L�y��%d�I�ˮ`!\n", 10);
			int current = target.getAttributeMap().get(attribute.HP)
					.getCurrent();
			target.getAttributeMap().get(attribute.HP).setCurrent(current - 10);
			if (target.isDown())
				out += Battle.deadMechanism(target);
		} else {
			out += String.format("����%s�ݦ����@���_�@��!\n", src.getChiName(),
					target.getChiName(), target.getChiName());
		}
		src.getMyGroup().getAtRoom().informRoom(out);
	}
}
