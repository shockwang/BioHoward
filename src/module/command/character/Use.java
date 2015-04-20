package module.command.character;

import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IItem;
import module.item.api.IUseable;
import module.utility.HelpUtil;
import module.utility.Parse;

public class Use implements ICommand{
	private String[] name;
	
	public Use(){
		name = new String[2];
		name[0] = "use";
		name[1] = "us";
	}
	
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (command.length == 2){
			CommandServer.informGroup(g, String.format("你想讓%s使用什麼物品呢?\n", c.getChiName()));
			return false;
		} 
		
		IItem obj = g.getInventory().findItem(command[2]);
		if (obj != null){
			if (obj instanceof IUseable){
				IUseable objToUse = (IUseable) obj;
				if (command.length == 3){
					if (objToUse.onUse(c)) {
						String out = "status:" + ((PlayerGroup) g).showGroupStatus();
						CommandServer.informGroup(g, out);
						return true;
					}
				} else {
					String ttt = Parse.mergeString(command, 3, ' ');
					ICharacter target = g.getAtRoom().getGroupList()
							.findCharExceptGroup(g, ttt);
					if (target != null){
						boolean result = objToUse.onUse(c, target);
						if (result) {
							String out = "status:" + ((PlayerGroup) g).showGroupStatus();
							CommandServer.informGroup(g, out);
						}
						return result;
					} else if (g.getInBattle()){
						GroupList enemyList = g.getBattleTask().getEnemyGroups(c);
						target = enemyList.findAliveChar(command[2]);
						if (target != null){
							boolean result = objToUse.onUse(c, target);
							if (result) {
								String out = "status:" + ((PlayerGroup) g).showGroupStatus();
								CommandServer.informGroup(g, out);
							}
							return result;
						}
					}
					CommandServer.informGroup(g, "你想使用物品的對象不在這裡。\n");
				}
			} else
				CommandServer.informGroup(g, "那樣物品不是可以使用的喔!\n");
		} else
			CommandServer.informGroup(g, "你身上並沒有那樣物品。\n");
		return false;
	}

	@Override
	public String getHelp() {
		String output = HelpUtil.getHelp("resources/help/use.help");
		output += "\n";
		output += HelpUtil.getHelp("resources/help/chooseTeammate.help");
		return output;
	}

}
