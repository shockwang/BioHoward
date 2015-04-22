package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Bat extends AbstractWeapon{

	public Bat(){
		this("�y��", "bat");
		String desc = "���βy�Ϊ���s�y�ΡA�ѩ�Q���Ӭ����V���L�K�s��������A�y�Ϊ�\n";
		desc += "���w�g�ܱo�W�Y�����C";
		this.setDescription(desc);
		
		this.hitRatio = 0.75;
		this.statMap.put(status.WEAPON_ATTACK, 9);
	}
	
	public Bat(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// do nothing
	}

}
