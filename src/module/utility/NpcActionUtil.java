package module.utility;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.SingleItemList;
import module.item.api.IItem;
import module.map.constants.CExit;
import module.server.PlayerServer;

public class NpcActionUtil {
	public static void randomMove(Group g){
		String[] select = CExit.getAccessibleExitsRoom(g.getAtRoom());
		
		int ddd = PlayerServer.getRandom().nextInt(select.length);
		String[] output = {select[ddd]};
		CommandServer.readCommand(g, output);
	}
	
	public static void randomGet(ICharacter c){
		Group g = c.getMyGroup();
		
		int itemNum = 0;
		for (SingleItemList sil : g.getAtRoom().getItemList().itemList){
			itemNum += sil.list.size();
		}
		if (itemNum == 0) return;
		
		int ddd = PlayerServer.getRandom().nextInt(itemNum);
		IItem target = null;
		int index = 0, count = 0;
		for (SingleItemList sil : g.getAtRoom().getItemList().itemList){
			index = 1;
			for (IItem obj : sil.list){
				if (count == ddd) {
					target = obj;
					break;
				}
				count++;
				index++;
			}
		}
		
		String[] result = new String[3];
		result[0] = Parse.getFirstWord(c.getEngName());
		result[1] = "get";
		result[2] = index + "." + Parse.getFirstWord(target.getEngName());
		CommandServer.readCommand(g, result);
	}
	
	public static void randomDrop(ICharacter c){
		Group g = c.getMyGroup();

		int itemNum = 0;
		for (SingleItemList sil : g.getInventory().itemList){
			itemNum += sil.list.size();
		}
		if (itemNum == 0) return;
		
		IItem target = null;
		int ddd = PlayerServer.getRandom().nextInt(itemNum);
		int index = 0, count = 0;
		for (SingleItemList sil : g.getInventory().itemList){
			index = 1;
			for (IItem obj : sil.list){
				if (count == ddd){
					target = obj;
					break;
				}
				count++;
				index++;
			}
		}
		
		String[] result = {Parse.getFirstWord(c.getEngName()), "drop", 
				index + "." + Parse.getFirstWord(target.getEngName())};
		CommandServer.readCommand(g, result);
	}
}
