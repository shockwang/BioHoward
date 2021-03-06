package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;

public class Bat extends AbstractWeapon{

	public Bat(){
		this("球棒", "bat");
		String desc = "打棒球用的木製球棒，由於被拿來狠狠敲擊過鐵製窗戶欄杆，球棒的\n";
		desc += "表面已經變得凹凸不平。";
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
