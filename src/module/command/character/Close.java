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
		if (command.length == 1){
			CommandServer.informCharacter(c, "你想關上什麼呢?\n");
			return false;
		}
		
		// exit case
		exit direction = MoveUtil.getWay(command[1]);
		if (direction != null){
			try {
				IDoor targetDoor = c.getAtRoom().getExits().get(direction).getDoor();
				synchronized (targetDoor){
					switch (targetDoor.getDoorStatus()){
					case CLOSED: case LOCKED:
						CommandServer.informCharacter(c, "這個方向的門已經是關著的了。\n");
						break;
					case OPENED:
						if (targetDoor.getDoorAttribute() == doorAttribute.BROKEN){
							CommandServer.informCharacter(c, "這個方向的門壞掉了，關不起來。\n");
							return false;
						}
						targetDoor.setDoorStatus(doorStatus.CLOSED);
						c.getAtRoom().informRoom(c.getChiName() + "關上了" + direction.chineseName + "方的門。\n");
						if (c.getInBattle()) return true;
					}
				}
			} catch (NullPointerException e){
				CommandServer.informCharacter(c, "這個方向沒有門喔。\n");
			}
			return false;
		} 
		
		// container case
		IContainer container = ItemUtil.checkIsContainer(c, c.getAtRoom().getItemList(), command[1]);
		if (container != null){
			if (container.onClose(c)) return true;
		}

		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/close.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
