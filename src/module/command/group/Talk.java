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
			CommandServer.informGroup(g, "�԰����O�S�����A���ܪ���C\n");
		}
		else if (command.length == 1) {
			CommandServer.informGroup(g, "�A�Q�n�������?\n");
		}
		else {
			String tt = Parse.mergeString(command, 1, ' ');
			ICharacter target = g.getAtRoom().getGroupList().findCharExceptGroup(g, tt);
			if (target != null) CommandServer.informGroup(g, target.onTalk() + "\n");
			else CommandServer.informGroup(g, "�o�̨S���A�Q���ܪ���H�C\n");
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
