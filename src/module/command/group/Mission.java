package module.command.group;

import java.util.Map.Entry;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.mission.api.IMission;
import module.server.PlayerServer;
import module.utility.HelpUtil;

public class Mission implements ICommand {
	private String[] name;

	public Mission() {
		name = new String[2];
		name[0] = "mission";
		name[1] = "m";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		StringBuffer buf = new StringBuffer();
		buf.append("你目前手邊的任務狀況：\n");
		
		StringBuffer buf2 = new StringBuffer();
		for (Entry<String, IMission> entry : PlayerServer.getMissionMap().entrySet()){
			buf2.append(entry.getValue().displayState());
		}
		if (buf2.toString().equals("")) buf.append("無");
		else buf.append(buf2.toString());
		
		CommandServer.informGroup(c.getMyGroup(), buf.toString() + "\n");
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/mission.help");
		return output;
	}

}
