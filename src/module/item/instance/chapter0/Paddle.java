package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Paddle extends AbstractWeapon {
	
	public Paddle(){
		this("刀板", "paddle");
		this.setDescription("普通商店可以買到的刀板，韜哥愛用。");
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
