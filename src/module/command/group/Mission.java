package module.command.group;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.mission.api.IMission;
import module.server.PlayerServer;

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
		buf.append("�A�ثe���䪺���Ȫ��p�G\n");
		
		StringBuffer buf2 = new StringBuffer();
		for (IMission m : PlayerServer.getMissionSet()) {
			buf2.append(m.displayState());
		}
		if (buf2.toString().equals("")) buf.append("�L");
		else buf.append(buf2.toString());
		
		CommandServer.informGroup(c.getMyGroup(), buf.toString() + "\n");
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
