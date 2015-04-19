package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Nightstick;

public class DormKeeper extends AbstractCharacter{

	public DormKeeper(){
		this("�N��", "dorm keeper");
		StringBuffer buf = new StringBuffer();
		buf.append("�q�N���J�ٺ޲z�H���A���`��ǥ����M�������A���]���t�C�{�b�o��W\n");
		buf.append("�����K�ҡA�H�S�ۦV�A�ĹL�ӡA�A���ߪ��P�ɤ��T�h�æo����঳�o\n");
		buf.append("��j���O��C");
		this.setDesc(buf.toString());
		this.addAttribute(attribute.HP, 200);
		this.setStatus(status.STRENGTH, 30);
		this.setHostile(true);
		
		// set equipment
		this.getEquipment().put(EquipType.WEAPON, new Nightstick());
	}
	
	public DormKeeper(String chiName, String engName) {
		super(chiName, engName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
}
