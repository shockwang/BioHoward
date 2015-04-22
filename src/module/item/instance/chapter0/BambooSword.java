package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class BambooSword extends AbstractWeapon{
	
	public BambooSword(){
		this("�˼C", "bamboo sword");
		this.setDescription("�C�D�Ϊ��˼C�A���ӥ��H�i�O�ܵh����C");
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
