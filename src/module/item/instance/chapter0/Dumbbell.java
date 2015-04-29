package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Dumbbell extends AbstractWeapon {
	
	public Dumbbell(){
		this("�׹a", "dumbbell");
		this.setDescription("�����`�Ϊ��׹a�A���q���p��5����A���_�ӤQ���I���C");
		this.statMap.put(status.WEAPON_ATTACK, 12);
		this.hitRatio = 0.55;
		this.setPrice(499);
	}
	
	public Dumbbell(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// do nothing
	}

}
