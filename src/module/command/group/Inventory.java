package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;

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
			buf.append("你身上攜帶著：\n");
			if (g.getInventory().itemList.size() == 0)
				buf.append("空空如也。\n");
			else
				buf.append(g.getInventory().displayInfo());
			CommandServer.informGroup(g, buf.toString());
			return false;
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
