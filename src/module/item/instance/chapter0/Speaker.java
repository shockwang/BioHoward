package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.AbstractWeapon;
import module.server.PlayerServer;

public class Speaker extends AbstractWeapon{

	public Speaker(){
		this("�j�n��", "speaker");
		this.setDescription("�i�H���n����j���j�n���A�u�a�ʤu���ƥΫ~�C");
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
			String msg = String.format("%s�]���_�ʦ��z�X��ժ������A��%s�Q������!\n", 
					this.getChiName(), target.getChiName());
			int damage = PlayerServer.getRandom().nextInt(5) + 1;
			msg += target.getChiName() + "����F" + damage + "�I�ˮ`�C\n";
			target.setCurrentAttribute(attribute.HP, 
					target.getCurrentAttribute(attribute.HP) - damage);
			src.getAtRoom().informRoom(msg);
		}
	}

}
