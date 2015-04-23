package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;

public class DarkShadow extends AbstractCharacter{

	public DarkShadow() {
		this("�¼v", "dark shadow");
		String desc = "�@�j�Τ��_�ܴ��Ϊ����¼v�A���ɦ��X�¦⪺Ĳ��b�¦�ζ��P���\n";
		desc += "�ʵۡC�����o�۳��N���𮧡A���|�P���Ů𭰷ŤF�n�X�סC�A�q�����W�P����\n";
		desc += "���j�������P�A�O�A���TŸ�ݰ_�ӡC";
		this.setDesc(desc);
		
		this.bodyPartList = new String[] {
				"���߰϶�", "����Ĳ��", "�k��Ĳ��"
		};
		
		this.addAttribute(attribute.HP, 200);
		this.statusMap.put(status.STRENGTH, 300);
		this.statusMap.put(status.CONSTITUTION, 10);
		this.statusMap.put(status.SPEED, 4000);
		this.resistSpecialStatusSet.add(specialStatus.BLIND);
	}
	
	public DarkShadow(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public String getBareHandAttackMessage() {
		return "���XĲ���V";
	}

	@Override
	public void normalAction() {
		// do nothing
	}
}
