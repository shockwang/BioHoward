package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class BambooSword extends AbstractWeapon{
	
	public BambooSword(){
		this("竹劍", "bamboo sword");
		this.setDescription("劍道用的竹劍，拿來打人可是很痛的喔。");
		this.setPrice(789);
		
		this.statMap.put(status.WEAPON_ATTACK, 7);
		this.hitRatio = 0.95;
	}
	
	public BambooSword(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		// do nothing
	}

}
