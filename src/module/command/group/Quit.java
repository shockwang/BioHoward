package module.command.group;

import java.io.IOException;

import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.command.api.ICommand;

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
		// TODO Auto-generated method stub
		return null;
	}

}
