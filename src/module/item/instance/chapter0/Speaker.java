package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;
import module.server.PlayerServer;

public class Speaker extends AbstractWeapon{

	public Speaker(){
		this("大聲公", "speaker");
		this.setDescription("可以把聲音放大的大聲公，工地監工必備用品。");
		this.setWeaponType(WeaponType.AMPLIFIER);
		
		this.hitRatio = 0.8;
		this.statMap.put(status.WEAPON_ATTACK, 8);
	}
	
	public Speaker(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public void onAttack(ICharacter src, ICharacter target) {
		int ddd = PlayerServer.getRandom().nextInt(10);
		if (ddd >= 5){
			String msg = String.format("%s因為震動而爆出刺耳的高音，讓%s十分難受!\n", 
					this.getChiName(), target.getChiName());
			int damage = PlayerServer.getRandom().nextInt(5) + 1;
			msg += target.getChiName() + "受到了" + damage + "點傷害。\n";
			target.setCurrentAttribute(attribute.HP, 
					target.getCurrentAttribute(attribute.HP) - damage);
			src.getAtRoom().informRoom(msg);
		}
	}

}
