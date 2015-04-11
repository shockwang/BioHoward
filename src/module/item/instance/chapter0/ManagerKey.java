package module.item.instance.chapter0;

import module.item.AbstractItem;

public class ManagerKey extends AbstractItem{

	public ManagerKey(){
		this("宿舍集中管理鑰匙", "manager key");
		StringBuffer buf = new StringBuffer();
		buf.append("一大串宿舍的鑰匙，上面用標籤標出了各自負責的房門。有了這個就能\n");
		buf.append("打開所有宿舍房門了吧!");
		this.setDescription(buf.toString());
		this.setPrice(99);
	}
	
	public ManagerKey(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean isExpired(){
		// special item, not going to expire
		return false;
	}

}
