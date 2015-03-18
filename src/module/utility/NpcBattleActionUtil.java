package module.utility;

import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.server.PlayerServer;

public class NpcBattleActionUtil {
	public static void randomAttack(ICharacter c, GroupList enemyGroup){
		// totally random choose
		int charNum = 0;
		for (Group gg : enemyGroup.gList){
			for (CharList cList : gg.list){
				charNum += cList.charList.size();
			}
		}
		int ddd = PlayerServer.getRandom().nextInt(charNum);
		
		String[] target = locateTargetCommand(enemyGroup, ddd);
		String[] output = new String[target.length + 2];
		output[0] = Parse.getFirstWord(c.getEngName());
		output[1] = "attack";
		for (int i = 0; i < target.length; i++){
			output[i + 2] = target[i];
			System.out.print(target[i] + " ");
		}
		System.out.println();
		CommandServer.readCommand(c.getMyGroup(), output);
	}
	
	public static String[] locateTargetCommand(GroupList targetG, int index){
		int groupId = 1, charId = 1, count = 0;
		for (Group gg : targetG.gList){
			for (CharList cList : gg.list){
				for (ICharacter cc : cList.charList){
					if (count == index){
						if (groupId == 1){
							String[] result = {
									Parse.getFirstWord(gg.getEngName()),
									charId + "." + Parse.getFirstWord(cc.getEngName())};
							return result;
						} else {
							// check how many same group name before target group
							int duplicate = 1, gCount = 1;
							for (Group ggg : gg.getAtRoom().getGroupList().gList){
								if (gCount == groupId) break;
								if (ggg.getEngName().equals(cc.getMyGroup().getEngName()))
									duplicate++;
								gCount++;
							}
							String[] result = {
									duplicate + "." + Parse.getFirstWord(cc.getMyGroup().getEngName()),
									charId + "." + Parse.getFirstWord(cc.getEngName())
							};
							return result;
						}
					}
					count++;
					charId++;
				}
				charId = 1;
			}
			groupId++;
		}
		return null;
	}
}
