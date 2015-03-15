package module.command.group;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.Parse;

public class Talk implements ICommand {
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
		g.setTalking(true);
		
		if (g.getInBattle()){
			CommandServer.informGroup(g, "�԰����O�S�����A���ܪ���C\n");
		}
		else if (command.length == 1) {
			CommandServer.informGroup(g, "�A�Q�n�������?\n");
		}
		else {
			String tt = Parse.mergeString(command, 1, ' ');
			ICharacter target = g.getAtRoom().getGroupList().findCharExceptGroup(g, tt);
			if (target != null) {
				if (target.getMyGroup().getTalking()) 
					CommandServer.informGroup(g, "�A��ܪ���H���b�M�O�H���ܡA���@�U�a�C\n");
				else if (target.getMyGroup().getInBattle())
					CommandServer.informGroup(g, "�A��ܪ���H���b�԰����A�S�Ųz�A�C\n");
				else {
					target.getMyGroup().setTalking(true);
					CommandServer.informGroup(g, target.onTalk((PlayerGroup) g) + "\n");
					target.getMyGroup().setTalking(false);
				}
			}
			else CommandServer.informGroup(g, "�o�̨S���A�Q���ܪ���H�C\n");
		}
		g.setTalking(false);
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
