package module.character.instance.main;

import module.character.BaseHumanCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Paddle;

public class Tao extends BaseHumanCharacter{

	public Tao(){
		this("����", "tao");
		String desc = "�������D���k�H�A�y�W�L�ɵL�賣�a�۲H�w�����A�ϩ��ƶ��S����\n";
		desc += "��Ʊ������ʥL�C�L��ۤ@�����K���Ʀ��B�ʭm�A�⤤����y����\n";
		desc += "�Aı�o�L�O�ǳƥh�������P���y�ɡC";
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
