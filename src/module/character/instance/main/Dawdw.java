package module.character.instance.main;

import module.character.BaseHumanCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Speaker;

public class Dawdw extends BaseHumanCharacter{

	public Dawdw(){
		this("永康", "dawdw");
		String desc = "臉上隨時帶著嘲諷表情的男子，講話一直都不太正經。他身材練的很\n";
		desc += "結實，身上穿著有些髒汙的襯衫，一副剛從工地裡出來的模樣。你從\n";
		desc += "他手上拿的大聲公可以看出他是負責監工的。";
		this.setDesc(desc);
		this.setHostile(false);
		
		this.addAttribute(attribute.HP, 180);
		
		this.statusMap.put(status.STRENGTH, 45);
		this.statusMap.put(status.CONSTITUTION, 15);
		this.statusMap.put(status.SPEED, 3800);
		
		// add equipment
		this.equipMap.put(EquipType.WEAPON, new Speaker());
	}
	
	public Dawdw(String chiName, String engName) {
		super(chiName, engName);
	}

}
