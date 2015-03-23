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
			CommandServer.informGroup(g, "你想讓" + c.getChiName() + "把什麼鎖上呢?\n");
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
						CommandServer.informGroup(g, "這扇門已經是鎖上的了。\n");
						break;
					case CLOSED:
						if (targetDoor.getDoorAttribute() == doorAttribute.LOCKABLE) {
							if (targetDoor.onLock(c)) {
								targetDoor.setDoorStatus(doorStatus.LOCKED);
								g.getAtRoom().informRoom(
										c.getChiName() + "鎖上了"
												+ direction.chineseName
												+ "方的門。\n");
								if (g.getInBattle())
									return true;
							} else
								CommandServer
										.informGroup(g, "你身上並沒有帶著合適的鑰匙。\n");
						} else
							CommandServer.informGroup(g, "這扇門是無法上鎖的。\n");
					}
				}
			} catch (NullPointerException e) {
				CommandServer.informGroup(g, "這個方向沒有門喔。\n");
			}
		} else
			CommandServer.informGroup(g, "這裡沒有你想上鎖的東西。\n");

		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
