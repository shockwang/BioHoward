package module.command.character;

import java.util.Map.Entry;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IEquipment;
import module.utility.HelpUtil;
import module.utility.Search;

public class Remove implements ICommand {
	private String[] name;

	public Remove() {
		name = new String[2];
		name[0] = "remove";
		name[1] = "rem";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		synchronized (c.getAtRoom()) {
			if (command.length == 1) {
				CommandServer.informCharacter(c, "你想卸下什麼呢?\n");
				return false;
			}

			for (Entry<IEquipment.EquipType, IEquipment> entry : c.getEquipment().entrySet()) {
				if (Search.searchName(entry.getValue().getEngName(), command[1])) {
					entry.getValue().onRemove(c);
					if (c.getInBattle())
						return true;
					else
						return false;
				}
			}
			CommandServer.informCharacter(c, "你並沒有穿著這件裝備。\n");

			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/remove.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
