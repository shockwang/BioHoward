package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Key306 extends AbstractItem{
	
	public Key306(){
		this("�q�N306���Ъ��_��", "key 306");
		StringBuffer buf = new StringBuffer();
		buf.append("�N�ئ��J�٩ж��_�͡C");
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
