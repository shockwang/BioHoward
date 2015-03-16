package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.MoveUtil;

public class Close implements ICommand{
	private String[] name;
	
	public Close(){
		name = new String[2];
		name[0] = "close";
		name[1] = "cl";
	}
	
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (command.length == 2){
			CommandServer.informGroup(g, "�A�Q��" + c.getChiName() + "���W����O?\n");
			return false;
		}
		exit direction = MoveUtil.getWay(command[2]);
		IDoor targetDoor = null;
		try {
			targetDoor = g.getAtRoom().getExits().get(direction).getDoor();
		} catch (NullPointerException e){
			// do nothing
		}
		if (targetDoor == null) {
			CommandServer.informGroup(g, "�o�Ӥ�V�S������C\n");
			return false;
		}
		
		synchronized (targetDoor){
			switch (targetDoor.getDoorStatus()){
			case OPENED:
				CommandServer.informGroup(g, "�o�Ӥ�V�����w�g�O�}�۪��F�C\n");
				break;
			case BROKEN:
				CommandServer.informGroup(g, "�o�Ӥ�V�����a���F�A�����_�ӡC\n");
				break;
			case LOCKED:
				CommandServer.informGroup(g, "�o�Ӥ�V�����O��۪��C\n");
				break;
			case CLOSED:
				targetDoor.setDoorStatus(doorStatus.OPENED);
				g.getAtRoom().informRoom(c.getChiName() + "���W�F" + direction.chineseName + "�誺���C\n");
				if (g.getInBattle()) return true;
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
