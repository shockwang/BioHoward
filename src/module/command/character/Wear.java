package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IEquipment;
import module.item.api.IItem;
import module.utility.HelpUtil;

public class Wear implements ICommand {
	private String[] name;

	public Wear() {
		name = new String[2];
		name[0] = "wear";
		name[1] = "wea";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		synchronized (c.getAtRoom()) {
			if (command.length == 1) {
				CommandServer.informCharacter(c, "你想裝備什麼呢?\n");
				return false;
			}

			IItem obj = c.getInventory().findItem(command[1]);
			if (obj == null)
				CommandServer.informCharacter(c, "你身上並沒有那樣東西。\n");
			else if (!(obj instanceof IEquipment))
				CommandServer.informCharacter(c, "那並不是裝備喔!\n");
			else {
				IEquipment equip = (IEquipment) obj;
				if (c.getInBattle()) {
					IEquipment oldEquip = c.getEquipment().get(
							equip.getEquipType());
					if (oldEquip != null) {
						CommandServer.informCharacter(c, 
								"你身上該部位已經穿上裝備，在戰鬥中換裝請先卸下舊裝備。\n");
						return false;
					}
					equip.onWear(c);
					return true;
				} else
					equip.onWear(c);
			}
			return false;
		}
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/wear.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
