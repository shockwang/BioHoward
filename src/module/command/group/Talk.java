package module.command.group;

import module.character.Group;
import module.character.PlayerGroup;
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
		Group g = c.getMyGroup();

		ICharacter target = null;
		boolean canTalk = false;

		synchronized (g.getAtRoom()) {
			if (g.getInBattle()) {
				CommandServer.informGroup(g, "戰鬥中是沒空讓你講話的喔。\n");
			} else if (command.length == 1) {
				CommandServer.informGroup(g, "你想要跟誰講話?\n");
			} else {
				String tt = Parse.mergeString(command, 1, ' ');
				target = g.getAtRoom().getGroupList()
						.findCharExceptGroup(g, tt);
				if (target != null) {
					if (target.getMyGroup().getTalking())
						CommandServer.informGroup(g, "你選擇的對象正在和別人講話，等一下吧。\n");
					else if (target.getMyGroup().getInEvent())
						CommandServer.informGroup(g, "你選擇的對象正在劇情事件中，等一下吧。\n");
					else if (target.getMyGroup().getInBattle())
						CommandServer.informGroup(g, "你選擇的對象正在戰鬥中，沒空理你。\n");
					else {
						// truly get in talk state
						canTalk = true;
						target.getMyGroup().setTalking(true);
						g.setTalking(true);
						// target.getMyGroup().setTalking(false);
					}
				} else
					CommandServer.informGroup(g, "這裡沒有你想講話的對象。\n");
			}
		}
		
		// do the real talk action
		if (canTalk) {
			target.onTalk((PlayerGroup) g);
			target.getMyGroup().setTalking(false);
			g.setTalking(false);
		}
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/talk.help");
		return output;
	}

}
