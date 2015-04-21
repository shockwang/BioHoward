package module.item.instance.chapter0;

import module.item.BaseEquipment;

public class Watch extends BaseEquipment {
	
	public Watch(){
		this("も况", "watch", EquipType.ACCESSORY);
		String desc = "繬地盽拦も况琵ぃハ甶瞷华";
		this.setDescription(desc);
		this.setPrice(199);
	}

	public Watch(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
