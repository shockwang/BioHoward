package module.command.character;

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
		if (command.length == 1) {
			CommandServer.informCharacter(c, "你想把什麼東西解鎖?\n");
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
					case CLOSED:
						CommandServer.informCharacter(c, "這扇門並沒有上鎖。\n");
						return false;
					case LOCKED:
						if (targetDoor.onUnlock(c)) {
							targetDoor.setDoorStatus(doorStatus.CLOSED);
							c.getAtRoom()
									.informRoom(
											c.getChiName() + "解開了"
													+ direction.chineseName
													+ "方的門鎖。\n");
							if (c.getInBattle())
								return true;
						} else
							CommandServer.informCharacter(c, "你身上並沒有帶著合適的鑰匙。\n");
					}
				}
			} catch (NullPointerException e) {
				CommandServer.informCharacter(c, "這個方向沒有門喔。\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(c, c.getAtRoom().getItemList(), command[1]);
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

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
