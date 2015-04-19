package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class TrashCanCap extends BaseEquipment{
	public TrashCanCap(){
		this("�U����\", "trash can cap", EquipType.SHIELD);
		String desc = "�K�s���U����\�A�i�H���ө�ק����C";
		this.setDescription(desc);
		this.statMap.put(status.ARMOR, 3);
	}
	
	public TrashCanCap(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
