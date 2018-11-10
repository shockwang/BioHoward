package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.BattleUtil;
import module.utility.EventUtil;
import module.utility.HelpUtil;
import module.utility.ItemUtil;
import module.utility.MoveUtil;

public class Look implements ICommand {
	private String[] name;

	public Look() {
		name = new String[2];
		name[0] = "look";
		name[1] = "l";
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		// add for room event
		boolean triggerRoomEvent = false;
		synchronized (c.getAtRoom()) {
			if (command.length == 1) {
				// look at the environment
				CommandServer.informCharacter(c, c.getAtRoom()
						.displayRoomExceptCharacter(c));
				triggerRoomEvent = EventUtil.triggerRoomEvent(c);
				if (triggerRoomEvent) c.setTalking(true);
			} else if (command.length >= 3 && command[1].equals("in")) {
				// look in container
				IContainer container = ItemUtil.checkIsContainer(c, c.getAtRoom().getItemList(), command[2]);
				if (container != null){
					container.displayContent(c);
				}
				return false;
			} else {
				// look at the specific object
				if (command.length == 2) {
					IItem obj = ItemUtil.findEquipByName(c, command[1]);
					if (obj == null)
						obj = c.getInventory().findItem(command[1]);
					if (obj != null) {
						CommandServer.informCharacter(c, obj.display());
						return false;
					}
					obj = c.getAtRoom().getItemList().findItem(command[1]);
					if (obj != null) {
						CommandServer.informCharacter(c, obj.display());
						return false;
					}
					
					// look at direction
					exit way = MoveUtil.getWay(command[1]);
					if (way != null){
						if (c.getAtRoom().getExits().containsKey(way)){
							IDoor door = c.getAtRoom().getExits().get(way).getDoor();
							if (door == null || door.getDoorStatus() == doorStatus.OPENED){
								CommandServer.informCharacter(c, 
										c.getAtRoom().getExits().get(way).getRoom().displayRoom());
								return false;
							}
							else {
								CommandServer.informCharacter(c, door.getDescription() + "\n");
								return false;
							}
						} else {
							CommandServer.informCharacter(c, "那個方向並沒有什麼特別的。\n");
							return false;
						}
					}
					
					// look at character
					ICharacter target = c.getAtRoom().getCharList().findChar(command[1]);
					if (target != null) {
						if (target != c) {
							String msg = String.format("%s正上下打量著%s.\n", c.getChiName(), target.getChiName());
							c.getAtRoom().informRoom(msg);
						}
						CommandServer.informCharacterNoChange(c, target.getDesc() + "\n");
						String msg = target.getChiName() + "正裝備著:\n";
						msg += ItemUtil.showLookEquip(target);
						CommandServer.informCharacter(c, msg);
						return false;
					}
				}
				CommandServer.informCharacter(c, "這裡沒有你想要看的東西。\n");
			}
		}
		if (triggerRoomEvent){
			EventUtil.doRoomEvent(c);
			c.setTalking(false);
		}
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/look.help");
		return output;
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
