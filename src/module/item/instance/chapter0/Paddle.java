package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Paddle extends AbstractWeapon {
	
	public Paddle(){
		this("�M�O", "paddle");
		this.setDescription("���q�ө��i�H�R�쪺�M�O�A�����R�ΡC");
		this.setWeaponType(WeaponType.PINGPONG);
		
		this.hitRatio = 0.85;
		this.statMap.put(status.WEAPON_ATTACK, 10);
	}

	public Paddle(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// shows nothing
	}

}
