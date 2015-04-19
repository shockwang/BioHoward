package module.item.instance.chapter0;

import module.item.BaseEquipment;

public class Watch extends BaseEquipment {
	
	public Watch(){
		this("手錶", "watch", EquipType.ACCESSORY);
		String desc = "霍華常戴的手表，讓他可以時不時甩一下，展現帥氣風貌。";
		this.setDescription(desc);
		this.setPrice(199);
	}

	public Watch(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
	}
}
