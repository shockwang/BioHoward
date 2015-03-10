package module.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.character.api.ICharacter;
import module.command.api.IndexStringPair;
import module.utility.Parse;
import module.utility.Search;

public class GroupList {
	public List<Group> gList;
	
	public GroupList(){
		gList = Collections.synchronizedList(new ArrayList<Group>());
	}
	
	public void createGroup(ICharacter leader){
		gList.add(new Group(leader));
	}
	
	public Group findGroup(String gName){
		IndexStringPair pair = Parse.parseName(gName);
		
		int count = 0;
		for (Group g : gList){
			if (Search.searchName(g.getEngName(), pair.name)){
				if (count == pair.index) return g;
				else count++;
			}
		}
		return null;
	}
	
	public Group findGroupExceptGroup(String gName, Group except){
		IndexStringPair pair = Parse.parseName(gName);
		
		if (gName.equals("team")) return except;
		
		int count = 0;
		for (Group g : gList){
			if (g == except) continue;
			if (Search.searchName(g.getEngName(), pair.name)){
				if (count == pair.index) return g;
				else count++;
			}
		}
		return null;
	}
	
	public ICharacter findChar(String groupName, String charName, int index){
		for (Group g : gList){
			if (Search.searchName(g.getEngName(), groupName)) {
				return g.findChar(charName, index);
			}
		}
		return null;
	}
	
	public ICharacter findChar(String groupName, int groupIndex, String charName, int charIndex){
		int gIndex = 0;
		for (Group g : gList) {
			if (Search.searchName(g.getEngName(), groupName)){
				if (gIndex == groupIndex) return g.findChar(charName, charIndex);
				else gIndex++;
			}
		}
		return null;
	}
	
	public ICharacter findChar(String groupName, String charName){
		IndexStringPair gPair = Parse.parseName(groupName);
		if (gPair == null) return null;
		IndexStringPair cPair = Parse.parseName(charName);
		if (cPair == null) return null;
		
		int gIndex = 0;
		for (Group g : gList) {
			if (Search.searchName(g.getEngName(), gPair.name)){
				if (gIndex == gPair.index) return g.findChar(cPair.name, cPair.index);
				else gIndex++;
			}
		}
		return null;
	}
	
	public ICharacter findCharExceptGroup(Group g, String name){
		String[] temp = name.split(" ");
		
		if (temp.length == 1){
			IndexStringPair pair = Parse.parseName(name);
			int index = 0;
			for (Group gg : gList){
				if (g == gg) continue;
				
				if (gg.list.size() != 1 || gg.list.get(0).charList.size() != 1) continue;
				else if (Search.searchName(gg.getEngName(), pair.name)){
					if (index == pair.index) return gg.list.get(0).charList.get(0);
					else index++;
				}
			}
		} else {
			return this.findCharExceptGroup(g, temp[0], temp[1]);
		}
		return null;
	}
	
	public ICharacter findAliveChar(String charName){
		IndexStringPair cPair = Parse.parseName(charName);
		if (cPair == null) return null;
		
		int gIndex = 0;
		for (Group g : gList) {
			for (CharList cList : g.list){
				for (ICharacter c : cList.charList){
					if (!c.isDown() && Search.searchName(c.getEngName(), cPair.name)){
						if (gIndex == cPair.index) return c;
						else gIndex++;
					}
				}
			}
		}
		return null;
	}
	
	public ICharacter findFirstAliveChar(){
		for (Group g : this.gList){
			for (CharList cList : g.list){
				for (ICharacter c : cList.charList){
					if (!c.isDown()) return c;
				}
			}
		}
		return null;  // should not be possible, it means no char alive but the battle is still not over!
	}
	
	public ICharacter findCharExceptGroup(Group myGroup, String groupName, String charName){
		IndexStringPair gPair = Parse.parseName(groupName);
		if (gPair == null) return null;
		IndexStringPair cPair = Parse.parseName(charName);
		if (cPair == null) return null;
		
		if (groupName.equals("team")) {
			return myGroup.findChar(cPair.name, cPair.index);
		}
		
		int gIndex = 0;
		for (Group g : gList) {
			if (Search.searchName(g.getEngName(), gPair.name)){
				if (g == myGroup) continue;
				if (gIndex == gPair.index) return g.findAliveChar(cPair.name, cPair.index);
				else gIndex++;
			}
		}
		return null;
	}
	
	public ICharacter findChar(int groupId, String name, int index){
		try {
			return gList.get(groupId).findChar(name, index);
		} catch (IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public boolean removeChar(ICharacter target){
		for (Group g : gList){
			if (g.removeChar(target)) return true;
		}
		return false;
	}
	
	public void addGroupChar(String groupName, ICharacter obj){
		for (Group g : gList){
			if (Search.searchName(g.getEngName(), groupName))
				g.addChar(obj);
		}
	}
	
	public void addGroupChar(int groupId, ICharacter obj){
		try {
			gList.get(groupId).addChar(obj);
		} catch (IndexOutOfBoundsException e){
			System.out.println("Error: No such groupId.");
		}
	}
	
	public String displayInfo(){
		String output = "";
		for (Group g : gList){
			output += String.format("%s/%s\n", g.getChiName(), g.getEngName());
		}
		return output;
	}
	
	public String displayInfoExceptGroup(Group g){
		String output = "";
		for (Group gg : gList){
			if (gg != g) {
				if (gg.getInBattle()){
					output += String.format("%s/%s ¥¿¦b§ðÀ»µÛ%s\n", gg.getChiName(), gg.getEngName()
							, gg.getBattleTask().getEnemyGroups(gg).gList.get(0).getChiName());
					output += gg.displayInfoInBattle();
				}
				else
					output += String.format("%s/%s\n", gg.getChiName(), gg.getEngName());
			}
		}
		return output;
	}
}
