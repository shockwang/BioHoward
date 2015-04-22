package module.character.instance.chapter0;

import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.BambooSword;

public class MadStudent2 extends MadStudent{
	public MadStudent2(){
		super();
		this.addAttribute(attribute.HP, 80);
		this.statusMap.put(status.STRENGTH, 40);
		
		this.equipMap.put(EquipType.WEAPON, new BambooSword());
	}
}
