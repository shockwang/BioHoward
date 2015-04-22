package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class Glove extends BaseEquipment{
	
	public Glove(){
		this("棒球手套", "gloves", EquipType.GLOVES);
		this.setDescription("打棒球時穿戴的打擊手套，有一定的厚度。");
		
		this.statMap.put(status.ARMOR, 1);
		this.setPrice(239);
	}

	public Glove(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
