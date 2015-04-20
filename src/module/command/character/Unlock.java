package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.HelpUtil;
import module.utility.ItemUtil;
import module.utility.MoveUtil;

public class Unlock implements ICommand {
	private String[] name;

	public Unlock() {
		name = new String[2];
		name[0] = "unlock";
		name[1] = "un";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();

		if (command.length == 2) {
			CommandServer.informGroup(g, "�A�Q�⤰��F�����?\n");
			return false;
		}

		// exit case
		exit direction = MoveUtil.getWay(command[2]);
		if (direction != null) {
			try {
				IDoor targetDoor = g.getAtRoom().getExits().get(direction)
						.getDoor();
				synchronized (targetDoor) {
					switch (targetDoor.getDoorStatus()) {
					case OPENED:
					case CLOSED:
						CommandServer.informGroup(g, "�o�����èS���W��C\n");
						return false;
					case LOCKED:
						if (targetDoor.onUnlock(c)) {
							targetDoor.setDoorStatus(doorStatus.CLOSED);
							g.getAtRoom()
									.informRoom(
											c.getChiName() + "�Ѷ}�F"
													+ direction.chineseName
													+ "�誺����C\n");
							if (g.getInBattle())
								return true;
						} else
							CommandServer.informGroup(g, "�A���W�èS���a�ۦX�A���_�͡C\n");
					}
				}
			} catch (NullPointerException e) {
				CommandServer.informGroup(g, "�o�Ӥ�V�S������C\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(g, g.getAtRoom().getItemList(), command[2]);
		if (container != null){
			if (container.onUnlock(c)) return true;
		}

		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/unlock.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

}
