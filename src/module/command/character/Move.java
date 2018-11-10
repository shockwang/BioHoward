package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.EventUtil;
import module.utility.HelpUtil;
import module.utility.MoveUtil;
import module.utility.NpcActionUtil;

public class Move implements ICommand {
	private String[] name;

	public Move() {
		name = new String[12];
		name[0] = "north";
		name[1] = "n";
		name[2] = "south";
		name[3] = "s";
		name[4] = "east";
		name[5] = "e";
		name[6] = "west";
		name[7] = "w";
		name[8] = "up";
		name[9] = "u";
		name[10] = "down";
		name[11] = "d";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		exit go = MoveUtil.getWay(command[0]);
		exit from = MoveUtil.getOppositeWay(go);
		
		boolean triggerRoomEvent = false;
		synchronized (c.getAtRoom()) {
			if (c.getInBattle()) {
				CommandServer.informCharacter(c, "你正在戰鬥中，不能移動!\n");
			} else {
				// implement the go-north mechanism.
				if (c.getAtRoom().getExits().get(go) == null)
					CommandServer.informCharacter(c, "這個方向沒有路喔!\n");
				else if (c.getAtRoom().getExits().get(go).getDoor() == null
						|| c.getAtRoom().getExits().get(go).getDoor()
								.getDoorStatus() == doorStatus.OPENED) {
					IRoom here = c.getAtRoom();
					IRoom nRoom = here.getExits().get(go).getRoom();
					here.getCharList().removeChar(c);
					here.informRoom(c.getChiName() + "朝" + go.chineseName
							+ "邊離開了。\n");
					nRoom.getCharList().addChar(c);
					c.setAtRoom(nRoom);
					nRoom.informRoomExceptCharacter(c, c.getChiName() + "從"
							+ from.chineseName + "邊過來了。\n");
					CommandServer.informCharacter(c,
							nRoom.displayRoomExceptCharacter(c));
					
					// check if trigger room event
					triggerRoomEvent = EventUtil.triggerRoomEvent(c);
					if (triggerRoomEvent) c.setTalking(true);
					
					NpcActionUtil.checkAutoAttackPlayer(c.getAtRoom());
				} else
					CommandServer.informCharacter(c, "那邊的門是關著的。\n");
			}
		}
		if (triggerRoomEvent) {
			EventUtil.doRoomEvent(c);
			c.setTalking(false);
		}
		
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/move.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}
}
