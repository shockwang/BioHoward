package module.command.character;

import java.io.IOException;

import module.character.PlayerCharacter;
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
		PlayerCharacter pc = (PlayerCharacter) c;
		
		// TODO: add exit mechanism in the future
		String check = "確定要離開遊戲嗎? <y/n>\n";
		CommandServer.informCharacter(pc, check);
		String answer = IOUtil.readLineFromClientSocket(pc.getInFromClient());
		if (!answer.equals("y")) {
			CommandServer.informCharacter(pc, "回到遊戲中。\n");
			return false;
		}
		try {
			pc.getOutToClient().close();
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

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
