package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Soap extends AbstractItem{

	public Soap(){
		this("肥皂", "soap");
		String desc = "一塊普通的肥皂。如果你看到它出現在地上千萬不要亂撿，否則...\n";
		desc += "嘿嘿嘿...";
		this.setDescription(desc);
		this.setPrice(19);
	}
	
	public Soap(String chiName, String engName) {
		super(chiName, engName);
	}
}
