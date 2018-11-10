package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.server.PlayerServer;
import module.utility.HelpUtil;
import module.utility.ItemUtil;
import module.utility.Parse;

public class Get implements ICommand {
	private String[] name;

	public Get() {
		name = new String[2];
		name[0] = "get";
		name[1] = "g";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		synchronized (c.getAtRoom()) {
			if (command.length == 1) {
				CommandServer.informCharacter(c, "稱具癬ぐ或狥﹁?\n");
				return false;
			}

			if (c.getInBattle()) {
				if (command[1].equals("all")) {
					CommandServer.informCharacter(c, "タ驹矮い礚猭Ω具癬珇\n");
					return false;
				}

				if (command.length == 3) {
					IContainer container = ItemUtil.checkIsContainer(c, c
							.getAtRoom().getItemList(), command[2]);
					if (container != null) {
						if (container.onGetContent(c, command[1]))
							return true;
					}
					return false;
				} else {
					IItem obj = c.getAtRoom().getItemList()
							.findItem(command[1]);
					if (obj != null) {
						if (pickUpSingleItem(c, obj))
							return true;
					} else
						CommandServer.informCharacter(c, "硂柑⊿Τ稱具狥﹁\n");
				}
			} else {
				if (command.length == 3) {
					IContainer container = ItemUtil.checkIsContainer(c, c
							.getAtRoom().getItemList(), command[2]);
					if (container != null) {
						if (command[1].equals("all")) {
							while (container.getItemList().itemList.size() > 0) {
								IItem obj = container.getItemList().itemList
										.get(0).findItem(0);
								String target = Parse.getFirstWord(obj
										.getEngName());
								if (!container.onGetContent(c, target))
									break;
							}
							CommandServer.informCharacter(c, "OK.\n");
						} else {
							container.onGetContent(c, command[1]);
						}
					}
				} else {
					IItem obj = null;
					if (command[1].equals("all")) {
						while (c.getAtRoom().getItemList().itemList.size() > 0) {
							obj = c.getAtRoom().getItemList().itemList.get(0)
									.findItem(0);
							if (!pickUpSingleItem(c, obj)) break;
						}
						CommandServer.informCharacter(c, "OK.\n");
						return false;
					}
					obj = c.getAtRoom().getItemList().findItem(command[1]);
					if (obj != null) {
						pickUpSingleItem(c, obj);
					} else
						CommandServer.informCharacter(c, "硂柑⊿Τ稱具狥﹁\n");
				}
			}
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/get.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	private boolean pickUpSingleItem(ICharacter c, IItem obj) {
		// shock add for container test
		if (obj instanceof IContainer) {
			IContainer container = (IContainer) obj;
			switch (container.getType()) {
			case FIXED_POSITION:
			case TREASURE_BOX:
				CommandServer.informCharacter(c, "硂甧竟琌ぃ繦種具癬\n");
				return false;
			default:
				// do nothing for now
			}
		}
		// shock add end
		c.getAtRoom().getItemList().removeItem(obj);
		c.getInventory().addItem(obj);
		c.getAtRoom().informRoom(
				c.getChiName() + "具癬" + obj.getChiName() + "\n");
		obj.setTTL(0);
		obj.setAtRoom(null);
		PlayerServer.getSystemTime().removeItem(obj);
		return true;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
