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
				CommandServer.informCharacter(c, "你想丟下什麼呢?\n");
				return false;
			}

			if (c.getInBattle()) {
				if (command[1].equals("all")) {
					CommandServer.informCharacter(c, "你正在戰鬥中，無法一次丟下多個物品。\n");
					return false;
				}

				IItem obj = c.getInventory().findItem(command[1]);
				if (obj != null) {
					dropSingleItem(c, obj);
					return true;
				} else
					CommandServer.informCharacter(c, "你身上沒有想丟的東西。\n");
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
					CommandServer.informCharacter(c, "你身上沒有想丟的東西。\n");
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
				c.getChiName() + "丟下了" + obj.getChiName() + "。\n");
		obj.setTTL(0);
		obj.setAtRoom(c.getAtRoom());
		PlayerServer.getSystemTime().addItem(obj);
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}
}
