package module.command.character;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.HelpUtil;
import module.utility.Parse;

public class Talk implements ICommand {
	private String[] name;

	public Talk() {
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
		ICharacter target = null;
		boolean canTalk = false;

		synchronized (c.getAtRoom()) {
			if (c.getInBattle()) {
				CommandServer.informCharacter(c, "�԰����O�S�����A���ܪ���C\n");
			} else if (command.length == 1) {
				CommandServer.informCharacter(c, "�A�Q�n�������?\n");
			} else {
				String tt = Parse.mergeString(command, 1, ' ');
				target = c.getAtRoom().getCharList().findChar(tt);
				if (target != null) {
					if (target.getTalking())
						CommandServer.informCharacter(c, "�A��ܪ���H���b�M�O�H���ܡA���@�U�a�C\n");
					else if (target.getInEvent())
						CommandServer.informCharacter(c, "�A��ܪ���H���b�@���ƥ󤤡A���@�U�a�C\n");
					else if (target.getInBattle())
						CommandServer.informCharacter(c, "�A��ܪ���H���b�԰����A�S�Ųz�A�C\n");
					else {
						// truly get in talk state
						canTalk = true;
						target.setTalking(true);
						c.setTalking(true);
					}
				} else
					CommandServer.informCharacter(c, "�o�̨S���A�Q���ܪ���H�C\n");
			}
		}
		
		// do the real talk action
		if (canTalk) {
			target.onTalk(c);
			target.setTalking(false);
			c.setTalking(false);
		}
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/talk.help");
		return output;
	}

	@Override
	public int getEnergyCost() {
		return 0;
	}

}
