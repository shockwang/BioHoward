package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class PhysicsBook extends AbstractWeapon{

	public PhysicsBook(){
		this("�����ҥ�", "physics book");
		String desc = "��u�t�j�@���ת������ҥ��A��r���٫p�A��j�Y�٭��C�C���W�ҳ�\n";
		desc += "�a�۳o��²���O�b���V���C";
		this.setDescription(desc);
		this.statMap.put(status.WEAPON_ATTACK, 100);
		this.setPrice(1300);
	}
	
	public PhysicsBook(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// TODO Auto-generated method stub
		
	}
}
