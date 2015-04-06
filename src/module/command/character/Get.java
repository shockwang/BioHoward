package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.server.PlayerServer;
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
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			if (command.length == 2) {
				CommandServer.informGroup(g, "稱琵" + c.getChiName()
						+ "具癬ぐ或狥﹁?\n");
				return false;
			}

			if (g.getInBattle()) {
				if (command[2].equals("all")) {
					CommandServer.informGroup(g, "タ驹矮い礚猭Ω具癬珇\n");
					return false;
				}

				if (command.length == 4) {
					IContainer container = ItemUtil.checkIsContainer(g, g
							.getAtRoom().getItemList(), command[3]);
					if (container != null) {
						if (container.onGetContent(c, command[2]))
							return true;
					}
					return false;
				} else {
					IItem obj = g.getAtRoom().getItemList()
							.findItem(command[2]);
					if (obj != null) {
						if (pickUpSingleItem(c, g, obj))
							return true;
					} else
						CommandServer.informGroup(g, "硂柑⊿Τ稱具狥﹁\n");
				}
			} else {
				if (command.length == 4) {
					IContainer container = ItemUtil.checkIsContainer(g, g
							.getAtRoom().getItemList(), command[3]);
					if (container != null) {
						if (command[2].equals("all")) {
							while (container.getItemList().itemList.size() > 0) {
								IItem obj = container.getItemList().itemList
										.get(0).findItem(0);
								String target = Parse.getFirstWord(obj
										.getEngName());
								if (!container.onGetContent(c, target))
									break;
							}
							CommandServer.informGroup(g, "OK.\n");
						} else {
							container.onGetContent(c, command[2]);
						}
					}
				} else {
					IItem obj = null;
					if (command[2].equals("all")) {
						while (g.getAtRoom().getItemList().itemList.size() > 0) {
							obj = g.getAtRoom().getItemList().itemList.get(0)
									.findItem(0);
							if (!pickUpSingleItem(c, g, obj)) break;
						}
						CommandServer.informGroup(g, "OK.\n");
						return false;
					}
					obj = g.getAtRoom().getItemList().findItem(command[2]);
					if (obj != null) {
						pickUpSingleItem(c, g, obj);
					} else
						CommandServer.informGroup(g, "硂柑⊿Τ稱具狥﹁\n");
				}
			}
			return false;
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean pickUpSingleItem(ICharacter c, Group g, IItem obj) {
		// shock add for container test
		if (obj instanceof IContainer) {
			IContainer container = (IContainer) obj;
			switch (container.getType()) {
			case FIXED_POSITION:
			case TREASURE_BOX:
				CommandServer.informGroup(g, "硂甧竟琌ぃ繦種具癬\n");
				return false;
			default:
				// do nothing for now
			}
		}
		// shock add end
		g.getAtRoom().getItemList().removeItem(obj);
		g.getInventory().addItem(obj);
		g.getAtRoom().informRoom(
				c.getChiName() + "具癬" + obj.getChiName() + "\n");
		obj.setTTL(0);
		obj.setAtRoom(null);
		PlayerServer.getSystemTime().removeItem(obj);
		return true;
	}

}
