package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
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
		Group g = c.getMyGroup();
		
		// add for room event
		boolean triggerRoomEvent = false;
		synchronized (g.getAtRoom()) {
			if (command.length == 1) {
				// look at the environment
				CommandServer.informGroup(g, g.getAtRoom()
						.displayRoomExceptGroup(g));
				triggerRoomEvent = EventUtil.triggerRoomEvent(g);
				if (triggerRoomEvent) g.setTalking(true);
			} else if (command.length >= 3 && command[1].equals("in")){
				// look in container
				IContainer container = ItemUtil.checkIsContainer(g, g.getAtRoom().getItemList(), command[2]);
				if (container != null){
					container.displayContent(c);
				}
				return false;
			}
			else {
				// look at the specific object
				if (command.length == 2) {
					IItem obj = g.getInventory().findItem(command[1]);
					if (obj != null) {
						CommandServer.informGroup(g, obj.display());
						return false;
					}
					obj = g.getAtRoom().getItemList().findItem(command[1]);
					if (obj != null) {
						CommandServer.informGroup(g, obj.display());
						return false;
					}
					
					// look at direction
					exit way = MoveUtil.getWay(command[1]);
					if (way != null){
						if (g.getAtRoom().getExits().containsKey(way)){
							IDoor door = g.getAtRoom().getExits().get(way).getDoor();
							if (door == null || door.getDoorStatus() == doorStatus.OPENED){
								CommandServer.informGroup(g, 
										g.getAtRoom().getExits().get(way).getRoom().displayRoom());
								return false;
							}
							else {
								CommandServer.informGroup(g, door.getDescription() + "\n");
								return false;
							}
						} else {
							CommandServer.informGroup(g, "���Ӥ�V�èS������S�O���C\n");
							return false;
						}
					}
				}

				// group name case
				Group tg = g.getAtRoom().getGroupList()
						.findGroupExceptGroup(command[1], g);
				if (tg != null) {
					if (command.length == 2) {
						CommandServer.informGroup(g, tg.displayInfo());
					} else {
						ICharacter tc = tg.findAliveChar(command[2]);
						if (tc != null) {
							String out = tc.getDesc() + "\n";
							out += ItemUtil.showLookEquip(tc);
							CommandServer.informGroup(g, out);
						} else
							CommandServer.informGroup(g, "�Ӷ���S���A�Q�[���H�C\n");
					}
				} else
					CommandServer.informGroup(g, "�o�̨S���A�Q�n�ݪ��F��C\n");
			}
		}
		if (triggerRoomEvent){
			EventUtil.doRoomEvent(g);
			g.setTalking(false);
		}
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/look.help");
		output += "\n";
		return output;
	}

	@Override
	public String[] getName() {
		return name;
	}

}
