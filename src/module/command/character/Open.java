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

public class Open implements ICommand {
	private String[] name;

	public Open() {
		name = new String[2];
		name[0] = "open";
		name[1] = "op";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		if (command.length == 1) {
			CommandServer.informCharacter(c, "你想打開什麼?\n");
			return false;
		}
		
		// exit case
		exit target = MoveUtil.getWay(command[1]);
		if (target != null){
			try {
				IDoor targetDoor = c.getAtRoom().getExits().get(target).getDoor();
				synchronized (targetDoor) {
					if (targetDoor.getDoorStatus() == doorStatus.OPENED)
						CommandServer.informCharacter(c, "這個方向的門早就是開著的了。\n");
					else if (targetDoor.getDoorStatus() == doorStatus.LOCKED)
						CommandServer.informCharacter(c, "這扇門上鎖了喔。\n");
					else {
						if (targetDoor.getDoorAttribute() == doorAttribute.BROKEN){
							CommandServer.informCharacter(c, "這扇門壞掉了，打不開。\n");
							return false;
						}
						targetDoor.setDoorStatus(doorStatus.OPENED);
						c.getAtRoom().informRoom(c.getChiName() + "打開了"
								+ target.chineseName + "方的門。\n");
						if (c.getInBattle())
							return true;
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
			if (container.onOpen(c)) return true;
		}

		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/open.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 50;
	}

}
