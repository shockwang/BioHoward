package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class PhysicsBook extends AbstractWeapon{

	public PhysicsBook(){
		this("普物課本", "physics book");
		String desc = "資工系大一必修的普物課本，比字典還厚，比磚頭還重。每次上課都\n";
		desc += "帶著這個簡直是在重訓嘛。";
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
