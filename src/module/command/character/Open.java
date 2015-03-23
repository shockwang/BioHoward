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
			CommandServer.informGroup(g, "你想讓" + c.getChiName() + "打開什麼?\n");
			return false;
		}
		exit target = MoveUtil.getWay(command[2]);
		if (target != null){
			try {
				IDoor targetDoor = g.getAtRoom().getExits().get(target).getDoor();
				synchronized (targetDoor) {
					if (targetDoor.getDoorStatus() == doorStatus.OPENED)
						CommandServer.informGroup(g, "這個方向的門早就是開著的了。\n");
					else if (targetDoor.getDoorStatus() == doorStatus.LOCKED)
						CommandServer.informGroup(g, "這扇門上鎖了喔。\n");
					else {
						if (targetDoor.getDoorAttribute() == doorAttribute.BROKEN){
							CommandServer.informGroup(g, "這扇門壞掉了，打不開。\n");
							return false;
						}
						targetDoor.setDoorStatus(doorStatus.OPENED);
						g.getAtRoom().informRoom(c.getChiName() + "打開了"
								+ target.chineseName + "方的門。\n");
						if (g.getInBattle())
							return true;
					}
				}
			} catch (NullPointerException e){
				CommandServer.informGroup(g, "這個方向沒有門喔。\n");
			}
		} else CommandServer.informGroup(g, "這裡沒有你想打開的東西。\n");

		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
