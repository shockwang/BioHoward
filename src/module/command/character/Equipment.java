package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
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
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			StringBuffer buf = new StringBuffer();
			buf.append(c.getChiName() + "ªº¸Ë³Æ¡G\n");
			buf.append(ItemUtil.showPlayerEquip(c));
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
