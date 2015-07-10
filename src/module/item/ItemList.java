package module.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.command.api.IndexStringPair;
import module.item.api.IItem;
import module.utility.Parse;
import module.utility.Search;

public class ItemList implements Serializable{
	public List<SingleItemList> itemList;
	
	public ItemList(){
		itemList = Collections.synchronizedList(new ArrayList<SingleItemList>());
	}
	
	public void addItem(IItem obj){
		for (SingleItemList sil : itemList){
			if (sil.list.get(0).getEngName().equals(obj.getEngName())){
				sil.list.add(obj);
				return;
			}
		}
		itemList.add(new SingleItemList(obj));
	}
	
	public boolean removeItem(IItem obj){
		for (SingleItemList sil : itemList){
			if (sil.list.contains(obj)){
				sil.list.remove(obj);
				if (sil.list.size() == 0) itemList.remove(sil);
				return true;
			}
		}
		return false;
	}
	
	public IItem findItem(String name){
		IndexStringPair pair = Parse.parseName(name);
		
		int index = 0;
		for (SingleItemList sil : itemList){
			for (IItem obj : sil.list){
				if (Search.searchName(obj.getEngName(), pair.name)){
					if (index == pair.index) return obj;
					else index++;
				}
			}
		}
		return null;
	}
	
	public String displayInfo(){
		if (itemList.size() == 0) return "";
		
		StringBuffer buf = new StringBuffer();
		for (SingleItemList sil : itemList){
			buf.append("   " + sil.displayInfo());
		}
		return buf.toString();
	}
}
