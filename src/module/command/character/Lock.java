package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
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
		Group g = c.getMyGroup();
		
		if (command.length == 2){
			CommandServer.informGroup(g, "�A�Q��" + c.getChiName() + "�⤰����W�O?\n");
			return false;
		}

		exit direction = MoveUtil.getWay(command[2]);
		if (direction != null) {
			try {
				IDoor targetDoor = g.getAtRoom().getExits().get(direction)
						.getDoor();
				synchronized (targetDoor) {
					switch (targetDoor.getDoorStatus()) {
					case OPENED:
					case LOCKED:
						CommandServer.informGroup(g, "�o�����w�g�O��W���F�C\n");
						break;
					case CLOSED:
						if (targetDoor.getDoorAttribute() == doorAttribute.LOCKABLE) {
							if (targetDoor.onLock(c)) {
								targetDoor.setDoorStatus(doorStatus.LOCKED);
								g.getAtRoom().informRoom(
										c.getChiName() + "��W�F"
												+ direction.chineseName
												+ "�誺���C\n");
								if (g.getInBattle())
									return true;
							} else
								CommandServer
										.informGroup(g, "�A���W�èS���a�ۦX�A���_�͡C\n");
						} else
							CommandServer.informGroup(g, "�o�����O�L�k�W�ꪺ�C\n");
					}
				}
			} catch (NullPointerException e) {
				CommandServer.informGroup(g, "�o�Ӥ�V�S������C\n");
			}
		} else
			CommandServer.informGroup(g, "�o�̨S���A�Q�W�ꪺ�F��C\n");

		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
