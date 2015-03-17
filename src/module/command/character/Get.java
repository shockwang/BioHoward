package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IItem;

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

		if (command.length == 2) {
			CommandServer.informGroup(g, "�A�Q��" + c.getChiName() + "�߰_����F��?\n");
			return false;
		}

		synchronized (g.getAtRoom().getItemList()) {
			if (g.getInBattle()) {
				if (command[2].equals("all")) {
					CommandServer.informGroup(g, "�A���b�԰����A�L�k�@���߰_�h�Ӫ��~�C\n");
					return false;
				}

				IItem obj = g.getAtRoom().getItemList().findItem(command[2]);
				if (obj != null) {
					pickUpSingleItem(c, g, obj);
					return true;
				} else
					CommandServer.informGroup(g, "�o�̨S���A�Q�ߪ��F��C\n");
			} else {
				synchronized (g.getInventory()) {
					IItem obj = null;
					if (command[2].equals("all")) {
						while (g.getAtRoom().getItemList().itemList.size() > 0) {
							obj = g.getAtRoom().getItemList().itemList.get(0)
									.findItem(0);
							pickUpSingleItem(c, g, obj);
						}
						CommandServer.informGroup(g, "OK.\n");
						return false;
					}
					obj = g.getAtRoom().getItemList().findItem(command[2]);
					if (obj != null) {
						pickUpSingleItem(c, g, obj);
					} else
						CommandServer.informGroup(g, "�o�̨S���A�Q�ߪ��F��C\n");
				}
			}
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	private void pickUpSingleItem(ICharacter c, Group g, IItem obj) {
		g.getAtRoom().getItemList().removeItem(obj);
		g.getInventory().addItem(obj);
		g.getAtRoom().informRoom(
				c.getChiName() + "�߰_�F" + obj.getChiName() + "�C\n");
	}

}
