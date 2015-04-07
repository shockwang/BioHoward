package module.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.item.api.IContainer;
import module.item.api.IItem;
import module.utility.ItemUtil;

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
		if (list.get(0) instanceof IContainer){
			StringBuffer buf = new StringBuffer();
			for (IItem obj : list)
				buf.append(String.format("%s[%s]/%s\n", obj.getChiName(), 
						ItemUtil.showContainerStatus((IContainer) obj), obj.getEngName()));
			return buf.toString();
		}
		String output = String.format("%s/%s\n", list.get(0).getChiName(), list.get(0).getEngName());
		if (list.size() > 1) return String.format("(%d) %s", list.size(), output);
		else return output;
	}
}
