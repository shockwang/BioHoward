package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class NikeShoes extends BaseEquipment{

	public NikeShoes(){
		this("NIKE球鞋", "nike shoes", EquipType.BOOTS);
		String desc = "一雙NIKE的球鞋，因為一段時間沒洗了而有點污漬。鞋子兩旁有\n";
		desc += "些磨損，顯示出它的使用頻率。";
		this.setDescription(desc);
		this.setPrice(999);
		this.statMap.put(status.ARMOR, 1);
	}
	
	public NikeShoes(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
