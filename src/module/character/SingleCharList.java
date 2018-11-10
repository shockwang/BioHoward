package module.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.character.api.ICharacter;

public class SingleCharList {
	public List<ICharacter> list;
	
	public SingleCharList(ICharacter c) {
		list = Collections.synchronizedList(new ArrayList<ICharacter>());
		list.add(c);
	}
	
	public ICharacter findChar(int index) {
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	
}
