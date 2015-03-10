package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.Command;
import module.utility.Parse;

public class Talk implements Command {
	private String[] name;
	
	public Talk(){
		name = new String[2];
		name[0] = "talk";
		name[1] = "ta";
	}
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (g.getInBattle()){
			CommandServer.informGroup(g, "戰鬥中是沒空讓你講話的喔。\n");
		}
		else if (command.length == 1) {
			CommandServer.informGroup(g, "你想要跟誰講話?\n");
		}
		else {
			String tt = Parse.mergeString(command, 1, ' ');
			ICharacter target = g.getAtRoom().getGroupList().findCharExceptGroup(g, tt);
			if (target != null) CommandServer.informGroup(g, target.onTalk() + "\n");
			else CommandServer.informGroup(g, "這裡沒有你想講話的對象。\n");
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
