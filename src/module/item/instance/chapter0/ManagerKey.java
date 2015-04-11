package module.item.instance.chapter0;

import module.item.AbstractItem;

public class ManagerKey extends AbstractItem{

	public ManagerKey(){
		this("�J�ٶ����޲z�_��", "manager key");
		StringBuffer buf = new StringBuffer();
		buf.append("�@�j��J�٪��_�͡A�W���μ��ҼХX�F�U�ۭt�d���Ъ��C���F�o�ӴN��\n");
		buf.append("���}�Ҧ��J�٩Ъ��F�a!");
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
