package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.server.PlayerServer;
import module.utility.HelpUtil;

public class MyTime implements ICommand {
	private String[] name = null;
	
	public MyTime(){
		name = new String[1];
		name[0] = "time";
	}
	
	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		CommandServer.informGroup(g, PlayerServer.getSystemTime().getTime() + "\n");
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/time.help");
		return output;
	}

	@Override
	public String[] getName() {
		return name;
	}

}
