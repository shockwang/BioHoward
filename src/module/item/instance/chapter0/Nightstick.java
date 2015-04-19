package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Nightstick extends AbstractWeapon{

	public Nightstick(){
		this("ĵ��", "nightstick");
		this.setDescription("���K�s��ĵ�ҡA���q��������a��w�A�O�����ˤO���Z���C");
		this.setPrice(1499);
		this.hitRatio = 0.9;
		this.statMap.put(status.WEAPON_ATTACK, 10);
	}
	
	public Nightstick(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// no special message
	}
}
