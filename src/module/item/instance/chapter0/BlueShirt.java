package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class BlueShirt extends BaseEquipment{
	
	public BlueShirt(){
		this("淺藍色襯衫", "blue shirt", EquipType.ARMOR);
		String desc = "霍華常穿的淺藍色襯衫，樣式很樸素。夏天的時候他喜歡把它披在肩\n";
		desc += "上當裝飾。";
		this.setDescription(desc);
		this.setPrice(299);
		this.statMap.put(status.ARMOR, 2);
	}

	public BlueShirt(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}

}
