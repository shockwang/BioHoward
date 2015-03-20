package module.utility;

import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.SingleItemList;
import module.item.api.IItem;
import module.map.api.IRoom;
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
	
	public static void attackRandomPlayerGroup(ICharacter c){
		String[] targetS = locateRandomPlayerGroup(c.getMyGroup().getAtRoom().getGroupList());
		
		if (targetS == null) return;
		String[] output = new String[targetS.length + 2];
		output[0] = Parse.getFirstWord(c.getEngName());
		output[1] = "attack";
		for (int i = 0; i < targetS.length; i++) {
			output[i + 2] = targetS[i];
			System.out.print(targetS[i] + " ");
		}
		System.out.println();
		CommandServer.readCommand(c.getMyGroup(), output);
	}
	
	
	public static String[] locateRandomPlayerGroup(GroupList targetGList){
		int num = 0;
		for (Group gg : targetGList.gList){
			if (gg instanceof PlayerGroup) {
				num++;
			}
		}
		
		if (num == 0) return null;
		int ddd = PlayerServer.getRandom().nextInt(num);
		int count = 0, targetIndex = 0;
		for (Group gg : targetGList.gList){
			if (gg instanceof PlayerGroup) {
				if (ddd == count) {
					int charNum = 0;
					for (CharList cList : gg.list){
						charNum += cList.charList.size();
					}
					targetIndex += PlayerServer.getRandom().nextInt(charNum);
					String[] result = NpcBattleActionUtil.locateTargetCommand(
							gg.getAtRoom().getGroupList(), targetIndex);
					return result;
				}
				count++;
			}
			for (CharList cList : gg.list){
				targetIndex += cList.charList.size();
			}
		}
		return null;
	}
	
	public static void checkAutoAttackPlayerGroup(IRoom r){
		for (Group g : r.getGroupList().gList){
			if (g.list.get(0).charList.get(0).getHostile())
				attackRandomPlayerGroup(g.list.get(0).charList.get(0));
		}
	}
}
