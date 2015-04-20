package module.command.character;

import module.character.Group;
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
		name[1] = "o";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		if (command.length == 2) {
			CommandServer.informGroup(g, "�A�Q��" + c.getChiName() + "���}����?\n");
			return false;
		}
		
		// exit case
		exit target = MoveUtil.getWay(command[2]);
		if (target != null){
			try {
				IDoor targetDoor = g.getAtRoom().getExits().get(target).getDoor();
				synchronized (targetDoor) {
					if (targetDoor.getDoorStatus() == doorStatus.OPENED)
						CommandServer.informGroup(g, "�o�Ӥ�V�������N�O�}�۪��F�C\n");
					else if (targetDoor.getDoorStatus() == doorStatus.LOCKED)
						CommandServer.informGroup(g, "�o�����W��F��C\n");
					else {
						if (targetDoor.getDoorAttribute() == doorAttribute.BROKEN){
							CommandServer.informGroup(g, "�o�����a���F�A�����}�C\n");
							return false;
						}
						targetDoor.setDoorStatus(doorStatus.OPENED);
						g.getAtRoom().informRoom(c.getChiName() + "���}�F"
								+ target.chineseName + "�誺���C\n");
						if (g.getInBattle())
							return true;
					}
				}
			} catch (NullPointerException e){
				CommandServer.informGroup(g, "�o�Ӥ�V�S������C\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(g, g.getAtRoom().getItemList(), command[2]);
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

}
