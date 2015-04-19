package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class BlueShirt extends BaseEquipment{
	
	public BlueShirt(){
		this("�L�Ŧ�Ũ�m", "blue shirt", EquipType.ARMOR);
		String desc = "�N�ر`�諸�L�Ŧ�Ũ�m�A�˦��ܾ���C�L�Ѫ��ɭԥL���w�⥦�ܦb��\n";
		desc += "�W��˹��C";
		this.setDescription(desc);
		this.setPrice(299);
		this.statMap.put(status.ARMOR, 2);
	}

	public BlueShirt(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}

}
