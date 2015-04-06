package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Cake extends AbstractItem {
	
	public Cake(){
		this("85度C蛋糕", "cake");
		this.setDescription("一塊85度C買的起司蛋糕，看起來很可口!");
	}
	
	public Cake(String chiName, String engName) {
		super(chiName, engName);
	}

}
