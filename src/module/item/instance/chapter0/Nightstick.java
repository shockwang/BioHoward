package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Nightstick extends AbstractWeapon{

	public Nightstick(){
		this("警棍", "nightstick");
		this.setDescription("鋼鐵製的警棍，重量不重但質地堅硬，是頗具殺傷力的武器。");
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
