package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.HelpUtil;
import module.utility.ItemUtil;
import module.utility.MoveUtil;

public class Open implements ICommand {
	private String[] name;

	public Open() {
		name = new String[2];
		name[0] = "open";
		name[1] = "op";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		if (command.length == 1) {
			CommandServer.informCharacter(c, "�A�Q���}����?\n");
			return false;
		}
		
		// exit case
		exit target = MoveUtil.getWay(command[1]);
		if (target != null){
			try {
				IDoor targetDoor = c.getAtRoom().getExits().get(target).getDoor();
				synchronized (targetDoor) {
					if (targetDoor.getDoorStatus() == doorStatus.OPENED)
						CommandServer.informCharacter(c, "�o�Ӥ�V�������N�O�}�۪��F�C\n");
					else if (targetDoor.getDoorStatus() == doorStatus.LOCKED)
						CommandServer.informCharacter(c, "�o�����W��F��C\n");
					else {
						if (targetDoor.getDoorAttribute() == doorAttribute.BROKEN){
							CommandServer.informCharacter(c, "�o�����a���F�A�����}�C\n");
							return false;
						}
						targetDoor.setDoorStatus(doorStatus.OPENED);
						c.getAtRoom().informRoom(c.getChiName() + "���}�F"
								+ target.chineseName + "�誺���C\n");
						if (c.getInBattle())
							return true;
					}
				}
			} catch (NullPointerException e){
				CommandServer.informCharacter(c, "�o�Ӥ�V�S������C\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(c, c.getAtRoom().getItemList(), command[1]);
		if (container != null){
			if (container.onOpen(c)) return true;
		}

		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/open.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
