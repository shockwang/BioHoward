package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.utility.ItemUtil;
import module.utility.Parse;

public class Put implements ICommand {
	private String[] name;

	public Put() {
		name = new String[2];
		name[0] = "put";
		name[1] = "p";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			if (command.length < 4)
				CommandServer.informGroup(g, "�A�Q�⤰�򪫫~�˶i�e���O?\n");
			else {
				IContainer container = ItemUtil.checkIsContainer(g, g
						.getAtRoom().getItemList(), command[3]);
				if (container != null) {
					if (g.getInBattle()) {
						if (command[2].equals("all"))
							CommandServer.informGroup(g, "�A���b�԰����A�S������h�ɶ���!\n");
						else {
							if (container.onPutContent(c, command[2]))
								return true;
						}
					} else {
						if (command[2].equals("all")){
							while (g.getInventory().itemList.size() > 0){
								IItem obj = g.getInventory().itemList.get(0).findItem(0);
								String target = Parse.getFirstWord(obj.getEngName());
								container.onPutContent(c, target);
							}
							CommandServer.informGroup(g, "OK.\n");
						} else {
							container.onPutContent(c, command[2]);
						}
					}
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

}
