package module.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.character.api.ICharacter;

public class CharList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6350303926974208673L;
	public List<ICharacter> charList;
	
	public CharList(ICharacter obj){
		charList = Collections.synchronizedList(new ArrayList<ICharacter>());
		charList.add(obj);
	}
	
	public ICharacter findChar(int index){
		try {
			return charList.get(index);
		} catch (IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public boolean removeChar(ICharacter target){  // return true if object exists in this list
		return charList.remove(target);
	}
	
	public String displayInfo(){
		StringBuffer buffer = new StringBuffer();
		for (ICharacter obj : charList){
			if (!obj.isDown())
				buffer.append(String.format("%s/%s\n", obj.getChiName(), obj.getEngName()));
		}
		return buffer.toString();
	}
	
	public String displayChar(int index){
		String output = "";
		try {
			ICharacter target = charList.get(index);
			output += String.format("%s/%s\n", target.getChiName(), target.getEngName());
			output += "Character information.\n";
			return output;
		} catch (IndexOutOfBoundsException e){
			return "你並沒有看到那樣東西.\n";
		}
	}
}
