package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.Parse;

public class Say implements ICommand {
	private String[] name;
	
	public Say() {
		name = new String[2];
		name[0] = "say";
		name[1] = "sa";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		if (command.length == 1) {
			CommandServer.informCharacter(c, "你想說什麼?\n");
		} else {
			String msgToSay = Parse.mergeString(command, 1, ' ');
			String result = String.format("%s說: \'%s\'\n", c.getChiName(), msgToSay);
			c.getAtRoom().informRoomExceptCharacter(c, result);
			String toMe = String.format("你說: \'%s\'\n", msgToSay);
			CommandServer.informCharacterNoChange(c, toMe);
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
