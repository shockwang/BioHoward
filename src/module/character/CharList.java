package module.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.character.api.ICharacter;
import module.command.api.IndexStringPair;
import module.utility.Parse;
import module.utility.Search;

public class CharList {
	public List<SingleCharList> charList;
	
	public CharList(){
		charList = Collections.synchronizedList(new ArrayList<SingleCharList>());
	}
	
	public ICharacter findChar(String name){
		IndexStringPair pair = Parse.parseName(name);
		int index = 0;
		for (SingleCharList scl : charList) {
			for (ICharacter c : scl.list) {
				if (Search.searchName(c.getEngName(), pair.name)){
					if (index == pair.index) return c;
					else index++;
				}
			}
		}
		return null;
	}
	
	public String displayInfo(){
		StringBuffer buffer = new StringBuffer();
		for (SingleCharList scl : charList) {
			for (ICharacter c : scl.list) {
				if (!c.isDown()) {
					buffer.append(String.format("%s/%s\n", c.getChiName(), c.getEngName()));
				}
			}
		}
		return buffer.toString();
	}
	
	public String displayInfoExceptChar(ICharacter target) {
		StringBuffer buffer = new StringBuffer();
		for (SingleCharList scl : charList) {
			for (ICharacter c : scl.list) {
				if (!c.isDown() && c != target) {
					buffer.append(String.format("%s/%s\n", c.getChiName(), c.getEngName()));
				}
			}
		}
		return buffer.toString();
	}
	
	public String displayChar(String name){
		String output = "";
		ICharacter target = this.findChar(name);
		if (target != null) {
			output += String.format("%s/%s\n", target.getChiName(), target.getEngName());
			output += "Character information.\n";
			return output;
		} else {
			return null;
		}
	}
	
	public boolean removeChar(ICharacter target) {
		for (SingleCharList scl : charList) {
			for (ICharacter c : scl.list) {
				if (target == c) {
					boolean success = scl.list.remove(c);
					if (scl.list.size() == 0) {
						charList.remove(scl);
					}
					return success;
				}
			}
		}
		return false;
	}
	
	public void addChar(ICharacter c) {
		for (SingleCharList scl : charList) {
			if (scl.list.get(0).getEngName().equals(c.getEngName())) {
				// same list
				scl.list.add(c);
				return;
			}
		}
		// new list
		charList.add(new SingleCharList(c));
	}
}
