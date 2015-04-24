package module.character.instance.main;

import module.character.BaseHumanCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Paddle;

public class Tao extends BaseHumanCharacter{

	public Tao(){
		this("韜哥", "tao");
		String desc = "身材高挑的男人，臉上無時無刻都帶著淡定的表情，彷彿事間沒有任\n";
		desc += "何事情能夠驚動他。他穿著一身輕便的排汗運動衫，手中的桌球拍讓\n";
		desc += "你覺得他是準備去打場輕鬆的球賽。";
		this.setDesc(desc);
		this.setHostile(false);
		
		this.addAttribute(attribute.HP, 150);
		
		this.statusMap.put(status.STRENGTH, 38);
		this.statusMap.put(status.CONSTITUTION, 13);
		this.statusMap.put(status.SPEED, 3200);
		
		// add equipment
		this.equipMap.put(EquipType.WEAPON, new Paddle());
	}
	
	public Tao(String chiName, String engName) {
		super(chiName, engName);
	}
}
