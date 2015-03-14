package module.command.character;

import java.util.Map.Entry;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.item.api.IEquipment;
import module.utility.Search;

public class Remove implements ICommand{
	private String[] name;
	
	public Remove(){
		name = new String[2];
		name[0] = "remove";
		name[1] = "rem";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (command.length == 2){
			CommandServer.informGroup(g, "你想讓" + c.getChiName() + "卸下什麼呢?\n");
			return false;
		}
		
		for (Entry<IEquipment.EquipType, IEquipment> entry : c.getEquipment().entrySet()){
			if (Search.searchName(entry.getValue().getEngName(), command[2])) {
				entry.getValue().onRemove(c);
				if (g.getInBattle()) return true;
				else return false;
			}
		}
		CommandServer.informGroup(g, c.getChiName() + "並沒有穿著這件裝備。\n");
		
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
