package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class TrashCanCap extends BaseEquipment{
	public TrashCanCap(){
		this("©U§£±í»\", "trash can cap", EquipType.SHIELD);
		String desc = "ÅK»sªº©U§£±í»\¡A¥i¥H®³¨Ó©è¾×§ðÀ»¡C";
		this.setDescription(desc);
		this.statMap.put(status.ARMOR, 3);
	}
	
	public TrashCanCap(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
