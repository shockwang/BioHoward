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
				CommandServer.informCharacter(c, "�A�Q�˳Ƥ���O?\n");
				return false;
			}

			IItem obj = c.getInventory().findItem(command[1]);
			if (obj == null)
				CommandServer.informCharacter(c, "�A���W�èS�����˪F��C\n");
			else if (!(obj instanceof IEquipment))
				CommandServer.informCharacter(c, "���ä��O�˳Ƴ�!\n");
			else {
				IEquipment equip = (IEquipment) obj;
				if (c.getInBattle()) {
					IEquipment oldEquip = c.getEquipment().get(
							equip.getEquipType());
					if (oldEquip != null) {
						CommandServer.informCharacter(c, 
								"�A���W�ӳ���w�g��W�˳ơA�b�԰������˽Х����U�¸˳ơC\n");
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
