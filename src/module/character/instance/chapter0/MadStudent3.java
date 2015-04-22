package module.character.instance.chapter0;

import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Bat;

public class MadStudent3 extends MadStudent{
	public MadStudent3(){
		super();
		this.equipMap.put(EquipType.WEAPON, new Bat());
	}
}
