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

public class Lock implements ICommand {
	private String[] name;

	public Lock() {
		name = new String[2];
		name[0] = "lock";
		name[1] = "lo";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		if (command.length == 1) {
			CommandServer.informCharacter(c, "�A�Q������W�O?\n");
			return false;
		}
		
		// exit case
		exit direction = MoveUtil.getWay(command[1]);
		if (direction != null) {
			try {
				IDoor targetDoor = c.getAtRoom().getExits().get(direction)
						.getDoor();
				synchronized (targetDoor) {
					switch (targetDoor.getDoorStatus()) {
					case OPENED:
						CommandServer.informCharacter(c, "�A�o��������W��C\n");
						break;
					case LOCKED:
						CommandServer.informCharacter(c, "�o�����w�g�O��W���F�C\n");
						break;
					case CLOSED:
						if (targetDoor.getDoorAttribute() == doorAttribute.LOCKABLE) {
							if (targetDoor.onLock(c)) {
								targetDoor.setDoorStatus(doorStatus.LOCKED);
								c.getAtRoom().informRoom(
										c.getChiName() + "��W�F"
												+ direction.chineseName
												+ "�誺���C\n");
								if (c.getInBattle())
									return true;
							} else
								CommandServer
										.informCharacter(c, "�A���W�èS���a�ۦX�A���_�͡C\n");
						} else
							CommandServer.informCharacter(c, "�o�����O�L�k�W�ꪺ�C\n");
					}
				}
			} catch (NullPointerException e) {
				CommandServer.informCharacter(c, "�o�Ӥ�V�S������C\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(c, c.getAtRoom().getItemList(), command[1]);
		if (container != null){
			if (container.onLock(c)) return true;
		}

		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/lock.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
