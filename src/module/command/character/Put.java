package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.utility.HelpUtil;
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
		synchronized (c.getAtRoom()) {
			if (command.length < 3)
				CommandServer.informCharacter(c, "你想把什麼物品裝進容器呢?\n");
			else {
				IContainer container = ItemUtil.checkIsContainer(c, c
						.getAtRoom().getItemList(), command[2]);
				if (container != null) {
					if (c.getInBattle()) {
						if (command[1].equals("all"))
							CommandServer.informCharacter(c, "你正在戰鬥中，沒有那麼多時間喔!\n");
						else {
							if (container.onPutContent(c, command[1]))
								return true;
						}
					} else {
						if (command[1].equals("all")){
							while (c.getInventory().itemList.size() > 0){
								IItem obj = c.getInventory().itemList.get(0).findItem(0);
								String target = Parse.getFirstWord(obj.getEngName());
								container.onPutContent(c, target);
							}
							CommandServer.informCharacter(c, "OK.\n");
						} else {
							container.onPutContent(c, command[1]);
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/put.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
