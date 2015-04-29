package module.command.group;

import java.io.IOException;

import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.HelpUtil;
import module.utility.IOUtil;

public class Quit implements ICommand{
	private String[] name;
	
	public Quit(){
		name = new String[1];
		name[0] = "quit";
	}
	
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		PlayerGroup pg = (PlayerGroup) c.getMyGroup();
		
		// TODO: add exit mechanism in the future
		String check = "�T�w�n���}�C����? <y/n>\n";
		CommandServer.informGroup(pg, check);
		String answer = IOUtil.readLineFromClientSocket(pg.getInFromClient());
		if (!answer.equals("y")) {
			CommandServer.informGroup(pg, "�^��C�����C\n");
			return false;
		}
		try {
			pg.getOutToClient().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/quit.help");
		return output;
	}

}
