package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Bat extends AbstractWeapon{

	public Bat(){
		this("瞴次", "bat");
		String desc = "ゴ次瞴ノれ籹瞴次パ砆ㄓ篤阑筁臟籹怠め逆瞴次\n";
		desc += "竒跑眔ぃキ";
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
