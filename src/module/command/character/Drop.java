package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IItem;

public class Drop implements ICommand{
	private String[] name;
	
	public Drop(){
		name = new String[2];
		name[0] = "drop";
		name[1] = "dr";
	}
	
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (command.length == 2){
			CommandServer.informGroup(g, "�A�Q��" + c.getChiName() + "��U����O?\n");
			return false;
		}
		
		if (g.getInBattle()){
			if (command[2].equals("all")){
				CommandServer.informGroup(g, "�A���b�԰����A�L�k�@����U�h�Ӫ��~�C\n");
				return false;
			}
			
			IItem obj = g.getInventory().findItem(command[2]);
			if (obj != null){
				dropSingleItem(c, g, obj);
				return true;
			} else CommandServer.informGroup(g, "�A���W�S���Q�᪺�F��C\n");
		} else {
			IItem obj = null;
			if (command[2].equals("all")){
				while (g.getInventory().itemList.size() > 0){
					obj = g.getInventory().itemList.get(0).findItem(0);
					dropSingleItem(c, g, obj);
				}
				CommandServer.informGroup(g, "OK.\n");
				return false;
			}
			
			obj = g.getInventory().findItem(command[2]);
			if (obj != null){
				dropSingleItem(c, g, obj);
			} else CommandServer.informGroup(g, "�A���W�S���Q�᪺�F��C\n");
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void dropSingleItem(ICharacter c, Group g, IItem obj){
		g.getInventory().removeItem(obj);
		g.getAtRoom().getItemList().addItem(obj);
		g.getAtRoom().informRoom(c.getChiName() + "��U�F" + obj.getChiName() + "�C\n");
	}
}
