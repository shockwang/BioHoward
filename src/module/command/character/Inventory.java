package module.command.character;

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
		synchronized (c.getAtRoom()) {
			StringBuffer buf = new StringBuffer();
			buf.append("你身上攜帶著：\n");
			if (c.getInventory().itemList.size() == 0)
				buf.append("空空如也。\n");
			else
				buf.append(c.getInventory().displayInfo());
			CommandServer.informCharacter(c, buf.toString());
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/equipment.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
