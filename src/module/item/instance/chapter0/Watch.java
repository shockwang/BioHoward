package module.item.instance.chapter0;

import module.item.BaseEquipment;

public class Watch extends BaseEquipment {
	
	public Watch(){
		this("���", "watch", EquipType.ACCESSORY);
		String desc = "�N�ر`��������A���L�i�H�ɤ��ɥϤ@�U�A�i�{�Ӯ𭷻��C";
		this.setDescription(desc);
		this.setPrice(199);
	}

	public Watch(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
