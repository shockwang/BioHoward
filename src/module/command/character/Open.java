package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
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
		exit target = MoveUtil.getWay(command[2]);
		IDoor targetDoor = null;
		try {
			targetDoor = g.getAtRoom().getExits().get(target).getDoor();
		} catch (NullPointerException e){
			// do nothing
		}
		if (targetDoor == null) {
			CommandServer.informGroup(g, "�o�Ӥ�V�S������C\n");
			return false;
		}
		synchronized (targetDoor) {
			if (targetDoor.getDoorStatus() == doorStatus.OPENED
					|| targetDoor.getDoorStatus() == doorStatus.BROKEN)
				CommandServer.informGroup(g, "�o�Ӥ�V�������N�O�}�۪��F�C\n");
			else if (targetDoor.getDoorStatus() == doorStatus.LOCKED)
				CommandServer.informGroup(g, "�o�����W��F��C\n");
			else {
				targetDoor.setDoorStatus(doorStatus.OPENED);
				g.getAtRoom().informRoom(c.getChiName() + "���}�F"
						+ target.chineseName + "�誺���C\n");
				if (g.getInBattle())
					return true;
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
