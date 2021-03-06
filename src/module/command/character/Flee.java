package module.command.character;

import module.character.ICharacter;
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
		ICharacter g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			if (!g.getInBattle()) {
				CommandServer.informCharacter(g, "你並沒有在戰鬥中，為何要逃跑?\n");
				return false;
			} else {
				// TODO: implement flee mechanism
				int prob = PlayerServer.getRandom().nextInt(10);
				if (prob <= 6) {
					if (CExit.getAccessibleExitsRoom(g.getAtRoom()) == null) {
						g.getAtRoom().informRoom(
								String.format(
										"%s成功找到一個空隙準備逃離戰鬥，但卻發現房間沒有出口，無法逃脫!\n",
										c.getChiName()));
						return true;
					}
					g.getAtRoom().informRoom(
							c.getChiName() + "腳底抹油，一溜菸的成功逃出了戰場。\n");
					g.getBattleTask().removeBattleGroup(g);
					NpcActionUtil.randomMove(g);
				} else {
					g.getAtRoom().informRoom(c.getChiName() + "嘗試逃跑，但不幸失敗了!\n");
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
