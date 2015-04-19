package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Key306 extends AbstractItem{
	
	public Key306(){
		this("義齋306號房的鑰匙", "key 306");
		StringBuffer buf = new StringBuffer();
		buf.append("霍華住的宿舍房間鑰匙。");
		this.setDescription(buf.toString());
		this.setPrice(9);
	}
	
	public Key306(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean isExpired(){
		// special item, not going to expire
		return false;
	}

}
