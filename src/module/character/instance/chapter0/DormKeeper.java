package module.character.instance.chapter0;

import module.character.BaseHumanCharacter;
import module.character.PlayerCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Nightstick;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class DormKeeper extends BaseHumanCharacter{

	public DormKeeper(){
		this("�N��", "dorm keeper");
		StringBuffer buf = new StringBuffer();
		buf.append("�q�N���J�ٺ޲z�H���A���`��ǥ����M�������A���]���t�C�{�b�o��W\n");
		buf.append("�����K�ҡA�H�S�ۦV�A�ĹL�ӡA�A���ߪ��P�ɤ��T�h�æo����঳�o\n");
		buf.append("��j���O��C");
		this.setDesc(buf.toString());
		this.addAttribute(attribute.HP, 170);
		this.setStatus(status.STRENGTH, 30);
		this.setHostile(true);
		
		// set equipment
		this.getEquipment().put(EquipType.WEAPON, new Nightstick());
	}
	
	public DormKeeper(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
	
	@Override
	public void doEventWhenGroupDown(PlayerCharacter pc){
		MainMission mm = (MainMission) PlayerServer.getMissionMap()
				.get(MainMission.class.toString());
		pc.setInEvent(true);
		EventUtil.executeEventMessage(pc, "after_beat_keeper");
		mm.setState(MainMission.State.AFTER_DEFEAT_MANAGER);
		pc.setInEvent(false);
	}
}
