package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.HelpUtil;

public class Inventory implements ICommand {
	private String[] name;

	public Inventory() {
		name = new String[2];
		name[0] = "inventory";
		name[1] = "i";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			StringBuffer buf = new StringBuffer();
			buf.append("�A���W��a�ۡG\n");
			if (g.getInventory().itemList.size() == 0)
				buf.append("�ŪŦp�]�C\n");
			else
				buf.append(g.getInventory().displayInfo());
			CommandServer.informGroup(g, buf.toString());
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/equipment.help");
		output += "\n";
		return output;
	}

}
