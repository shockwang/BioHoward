package module.character.instance.main;

import module.character.BaseHumanCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Speaker;

public class Dawdw extends BaseHumanCharacter{

	public Dawdw(){
		this("�ñd", "dawdw");
		String desc = "�y�W�H�ɱa�ۼJ�ت����k�l�A���ܤ@�������ӥ��g�C�L�����m����\n";
		desc += "����A���W��ۦ���ż����Ũ�m�A�@�ƭ�q�u�a�̥X�Ӫ��ҼˡC�A�q\n";
		desc += "�L��W�����j�n���i�H�ݥX�L�O�t�d�ʤu���C";
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
