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
				CommandServer.informCharacter(c, "戰鬥中是沒空讓你講話的喔。\n");
			} else if (command.length == 1) {
				CommandServer.informCharacter(c, "你想要跟誰講話?\n");
			} else {
				String tt = Parse.mergeString(command, 1, ' ');
				target = c.getAtRoom().getCharList().findChar(tt);
				if (target != null) {
					if (target.getTalking())
						CommandServer.informCharacter(c, "你選擇的對象正在和別人講話，等一下吧。\n");
					else if (target.getInEvent())
						CommandServer.informCharacter(c, "你選擇的對象正在劇情事件中，等一下吧。\n");
					else if (target.getInBattle())
						CommandServer.informCharacter(c, "你選擇的對象正在戰鬥中，沒空理你。\n");
					else {
						// truly get in talk state
						canTalk = true;
						target.setTalking(true);
						c.setTalking(true);
					}
				} else
					CommandServer.informCharacter(c, "這裡沒有你想講話的對象。\n");
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
