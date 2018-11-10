package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.HelpUtil;
import module.utility.ItemUtil;

public class Equipment implements ICommand {
	private String[] name;

	public Equipment() {
		name = new String[2];
		name[0] = "equipment";
		name[1] = "eq";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		synchronized (c.getAtRoom()) {
			StringBuffer buf = new StringBuffer();
			buf.append("你的裝備：\n");
			buf.append(ItemUtil.showPlayerEquip(c));
			CommandServer.informCharacter(c, buf.toString());
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/equipment.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
