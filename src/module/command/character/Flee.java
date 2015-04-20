package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.constants.CExit;
import module.server.PlayerServer;
import module.utility.HelpUtil;
import module.utility.NpcActionUtil;

public class Flee implements ICommand {
	private String[] name;

	public Flee() {
		name = new String[2];
		name[0] = "flee";
		name[1] = "fl";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			if (!g.getInBattle()) {
				CommandServer.informGroup(g, "�A�èS���b�԰����A����n�k�]?\n");
				return false;
			} else {
				// TODO: implement flee mechanism
				int prob = PlayerServer.getRandom().nextInt(10);
				if (prob <= 6) {
					if (CExit.getAccessibleExitsRoom(g.getAtRoom()) == null) {
						g.getAtRoom().informRoom(
								String.format(
										"%s���\���@�ӪŻطǳưk���԰��A���o�o�{�ж��S���X�f�A�L�k�k��!\n",
										c.getChiName()));
						return true;
					}
					g.getAtRoom().informRoom(
							c.getChiName() + "�}���٪o�A�@�ȵҪ����\�k�X�F�Գ��C\n");
					g.getBattleTask().removeBattleGroup(g);
					NpcActionUtil.randomMove(g);
				} else {
					g.getAtRoom().informRoom(c.getChiName() + "���հk�]�A���������ѤF!\n");
				}
			}
			return true;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/flee.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

}
