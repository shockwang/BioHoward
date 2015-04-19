package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class NikeShoes extends BaseEquipment{

	public NikeShoes(){
		this("NIKE�y�c", "nike shoes", EquipType.BOOTS);
		String desc = "�@��NIKE���y�c�A�]���@�q�ɶ��S�~�F�Ӧ��I�ú{�C�c�l��Ǧ�\n";
		desc += "�ǿi�l�A��ܥX�����ϥ��W�v�C";
		this.setDescription(desc);
		this.setPrice(999);
		this.statMap.put(status.ARMOR, 1);
	}
	
	public NikeShoes(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
