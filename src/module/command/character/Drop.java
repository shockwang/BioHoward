package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IItem;
import module.server.PlayerServer;
import module.utility.HelpUtil;

public class Drop implements ICommand {
	private String[] name;

	public Drop() {
		name = new String[2];
		name[0] = "drop";
		name[1] = "dr";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		synchronized (c.getAtRoom()) {
			if (command.length == 1) {
				CommandServer.informCharacter(c, "�A�Q��U����O?\n");
				return false;
			}

			if (c.getInBattle()) {
				if (command[1].equals("all")) {
					CommandServer.informCharacter(c, "�A���b�԰����A�L�k�@����U�h�Ӫ��~�C\n");
					return false;
				}

				IItem obj = c.getInventory().findItem(command[1]);
				if (obj != null) {
					dropSingleItem(c, obj);
					return true;
				} else
					CommandServer.informCharacter(c, "�A���W�S���Q�᪺�F��C\n");
			} else {
				IItem obj = null;
				if (command[1].equals("all")) {
					while (c.getInventory().itemList.size() > 0) {
						obj = c.getInventory().itemList.get(0).findItem(0);
						dropSingleItem(c, obj);
					}
					CommandServer.informCharacter(c, "OK.\n");
					return false;
				}

				obj = c.getInventory().findItem(command[1]);
				if (obj != null) {
					dropSingleItem(c, obj);
				} else
					CommandServer.informCharacter(c, "�A���W�S���Q�᪺�F��C\n");
			}
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/drop.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		
		return output;
	}

	private void dropSingleItem(ICharacter c, IItem obj) {
		c.getInventory().removeItem(obj);
		c.getAtRoom().getItemList().addItem(obj);
		c.getAtRoom().informRoom(
				c.getChiName() + "��U�F" + obj.getChiName() + "�C\n");
		obj.setTTL(0);
		obj.setAtRoom(c.getAtRoom());
		PlayerServer.getSystemTime().addItem(obj);
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}
}
