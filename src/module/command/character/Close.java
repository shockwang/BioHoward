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
			CommandServer.informGroup(g, "你想讓" + c.getChiName() + "關上什麼呢?\n");
			return false;
		}
		exit direction = MoveUtil.getWay(command[2]);
		if (direction != null){
			try {
				IDoor targetDoor = g.getAtRoom().getExits().get(direction).getDoor();
				synchronized (targetDoor){
					switch (targetDoor.getDoorStatus()){
					case CLOSED:
						CommandServer.informGroup(g, "這個方向的門已經是關著的了。\n");
						break;
					case BROKEN:
						CommandServer.informGroup(g, "這個方向的門壞掉了，關不起來。\n");
						break;
					case LOCKED:
						CommandServer.informGroup(g, "這個方向的門是鎖著的。\n");
						break;
					case OPENED:
						targetDoor.setDoorStatus(doorStatus.CLOSED);
						g.getAtRoom().informRoom(c.getChiName() + "關上了" + direction.chineseName + "方的門。\n");
						if (g.getInBattle()) return true;
					}
				}
			} catch (NullPointerException e){
				CommandServer.informGroup(g, "這個方向沒有門喔。\n");
			}
		} else CommandServer.informGroup(g, "這裡沒有你想關上的東西。\n");

		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
