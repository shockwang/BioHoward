package module.utility;

import module.character.CharList;
import module.character.PlayerCharacter;
import module.character.SingleCharList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.SingleItemList;
import module.item.api.IItem;
import module.map.api.IRoom;
import module.map.constants.CExit;
import module.server.PlayerServer;

public class NpcActionUtil {
	public static void randomMove(ICharacter c){
		String[] select = CExit.getAccessibleExitsRoom(c.getAtRoom());
		
		if (select == null) return;
		
		int ddd = PlayerServer.getRandom().nextInt(select.length);
		String[] output = {select[ddd]};
		CommandServer.readCommand(c, output);
	}
	
	public static void randomGet(ICharacter c){
		int itemNum = 0;
		for (SingleItemList sil : c.getAtRoom().getItemList().itemList){
			itemNum += sil.list.size();
		}
		if (itemNum == 0) return;
		
		int ddd = PlayerServer.getRandom().nextInt(itemNum);
		IItem target = null;
		int index = 0, count = 0;
		for (SingleItemList sil : c.getAtRoom().getItemList().itemList){
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
		CommandServer.readCommand(c, result);
	}
	
	public static void randomDrop(ICharacter c){
		int itemNum = 0;
		for (SingleItemList sil : c.getInventory().itemList){
			itemNum += sil.list.size();
		}
		if (itemNum == 0) return;
		
		IItem target = null;
		int ddd = PlayerServer.getRandom().nextInt(itemNum);
		int index = 0, count = 0;
		for (SingleItemList sil : c.getInventory().itemList){
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
		CommandServer.readCommand(c, result);
	}
	
	public static void attackRandomPlayer(ICharacter c){
		String target = locateRandomPlayer(c.getAtRoom().getCharList());
		
		if (target == null) return;
		String[] output = new String[2];
		output[0] = "attack";
		output[1] = target;
		System.out.println(c.getChiName() + "attack + " + target);
		CommandServer.readCommand(c, output);
	}
	
	
	public static String locateRandomPlayer(CharList cList){
		int num = 0;
		for (SingleCharList scl : cList.charList) {
			for (ICharacter c : scl.list) {
				if (c instanceof PlayerCharacter) {
					num++;
				}
			}
		}
		
		if (num == 0) return null;
		int ddd = PlayerServer.getRandom().nextInt(num);
		int count = 0;
		for (SingleCharList scl : cList.charList) {
			for (ICharacter c : scl.list){
				if (c instanceof PlayerCharacter) {
					if (ddd == count) {
						return c.getEngName();
					}
					count++;
				}
			}
		}
		
		return null;
	}
	
	public static void checkAutoAttackPlayer(IRoom r){
		for (SingleCharList scl : r.getCharList().charList) {
			for (ICharacter c : scl.list) {
				if (c.getHostile()) {
					attackRandomPlayer(c);
				}
			}
		}
	}
}
