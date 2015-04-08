package module.command.character;

import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IItem;
import module.item.api.IUseable;
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
			CommandServer.informGroup(g, String.format("�A�Q��%s�ϥΤ��򪫫~�O?\n", c.getChiName()));
			return false;
		} 
		
		IItem obj = g.getInventory().findItem(command[2]);
		if (obj != null){
			if (obj instanceof IUseable){
				IUseable objToUse = (IUseable) obj;
				if (command.length == 3){
					if (objToUse.onUse(c)) return true;
				} else {
					String ttt = Parse.mergeString(command, 3, ' ');
					ICharacter target = g.getAtRoom().getGroupList()
							.findCharExceptGroup(g, ttt);
					if (target != null){
						return objToUse.onUse(c, target);
					} else if (g.getInBattle()){
						GroupList enemyList = g.getBattleTask().getEnemyGroups(c);
						target = enemyList.findAliveChar(command[2]);
						if (target != null){
							return objToUse.onUse(c, target);
						}
					}
					CommandServer.informGroup(g, "�A�Q�ϥΪ��~����H���b�o�̡C\n");
				}
			} else
				CommandServer.informGroup(g, "���˪��~���O�i�H�ϥΪ���!\n");
		} else
			CommandServer.informGroup(g, "�A���W�èS�����˪��~�C\n");
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}