package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.Command;
import module.map.constants.CExit;

public class Look implements Command{
	private String[] name;
	
	public Look(){
		name = new String[2];
		name[0] = "look";
		name[1] = "l";
	}
	
	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (command.length == 1){
			// look at the environment
			CommandServer.informGroup(g, g.getAtRoom().displayRoomExceptGroup(g));
		}
		else {
			// look at the specific object
			// TODO: define the see-object method
			// group name case
			Group tg = g.getAtRoom().getGroupList().findGroupExceptGroup(command[1], g);
			if (tg != null){
				if (command.length == 2){
					CommandServer.informGroup(g, tg.displayInfo());
				} else {
					ICharacter tc = tg.findAliveChar(command[2]);
					if (tc != null) 
						CommandServer.informGroup(g, tc.getDesc() + "\n");
					else CommandServer.informGroup(g, "該隊伍中沒有你想觀察的對象。\n");
				}
			} else CommandServer.informGroup(g, "這裡沒有你想要看的東西。\n");
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getName() {
		return name;
	}

}
