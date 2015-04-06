package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Juice extends AbstractItem{
	
	public Juice(){
		this("一杯果汁", "juice");
		this.setDescription("看起來像是葡萄汁的顏色，希望還沒過期。");
	}
	
	public Juice(String chiName, String engName) {
		super(chiName, engName);
	}

}
