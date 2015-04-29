package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Dumbbell extends AbstractWeapon {
	
	public Dumbbell(){
		this("啞鈴", "dumbbell");
		this.setDescription("健身常用的啞鈴，重量估計有5公斤，拿起來十分沉重。");
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
