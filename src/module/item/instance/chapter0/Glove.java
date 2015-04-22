package module.item.instance.chapter0;

import module.character.constants.CStatus.status;
import module.item.BaseEquipment;

public class Glove extends BaseEquipment{
	
	public Glove(){
		this("�βy��M", "gloves", EquipType.GLOVES);
		this.setDescription("���βy�ɬ�����������M�A���@�w���p�סC");
		
		this.statMap.put(status.ARMOR, 1);
		this.setPrice(239);
	}

	public Glove(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
