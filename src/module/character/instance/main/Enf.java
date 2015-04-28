package module.character.instance.main;

import module.character.BaseHumanCharacter;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.command.CommandServer;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.BlueShirt;
import module.item.instance.chapter0.NikeShoes;
import module.item.instance.chapter0.Watch;

public class Enf extends BaseHumanCharacter{

	public Enf(){
		this("�N��", "enf");
		// TODO: set description
		this.setDesc("�N�O�N�ءC");
		this.setStatus(status.CONSTITUTION, 6);
		this.setStatus(status.STRENGTH, 29);
		this.setStatus(status.SPEED, 3500);
		this.addAttribute(attribute.HP, 99);
		this.setHostile(false);
		
		// add equipment
		this.equipMap.put(EquipType.ARMOR, new BlueShirt());
		this.equipMap.put(EquipType.BOOTS, new NikeShoes());
		this.equipMap.put(EquipType.ACCESSORY, new Watch());
	}
	
	public Enf(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean battleAction(GroupList gList){
		return false;
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		CommandServer.informGroup(g, "�N�ج�M�}�l�ۨ��ۻy�A���D�O���U�X���D�F?\n");
	}
}
