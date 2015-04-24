package module.character.instance.chapter0;

import module.character.BaseHumanCharacter;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.Nightstick;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class DormKeeper extends BaseHumanCharacter{

	public DormKeeper(){
		this("齋媽", "dorm keeper");
		StringBuffer buf = new StringBuffer();
		buf.append("義齋的宿舍管理人員，平常對學生雖然不熱情，但也不差。現在她手上\n");
		buf.append("緊抓著鐵棍，咆嘯著向你衝過來，你恐懼的同時不禁懷疑她為何能有這\n");
		buf.append("麼大的力氣。");
		this.setDesc(buf.toString());
		this.addAttribute(attribute.HP, 200);
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
	public void doEventWhenGroupDown(PlayerGroup pg){
		MainMission mm = (MainMission) PlayerServer.getMissionMap()
				.get(MainMission.class.toString());
		pg.setInEvent(true);
		EventUtil.executeEventMessage(pg, "after_beat_keeper");
		mm.setState(MainMission.State.AFTER_DEFEAT_MANAGER);
		pg.setInEvent(false);
	}
}
