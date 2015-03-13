package module.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.item.api.IItem;

public class SingleItemList {
	public List<IItem> list;
	
	public SingleItemList(IItem obj){
		list = Collections.synchronizedList(new ArrayList<IItem>());
		list.add(obj);
	}
	
	public IItem findItem(int index){
		try {
			return list.get(index);
		} catch (Exception e){
			return null;
		}
	}
	
	public String displayInfo(){
		String output = String.format("%s/%s\n", list.get(0).getChiName(), list.get(0).getEngName());
		if (list.size() > 1) return String.format("(%d) %s", list.size(), output);
		else return output;
	}
}
