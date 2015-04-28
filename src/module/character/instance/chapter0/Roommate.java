package module.character.instance.chapter0;

import module.battle.BattleTask;
import module.battle.chapter0.FirstTutorialBattle;
import module.character.BaseHumanCharacter;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CConfig.config;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class Roommate extends BaseHumanCharacter{
	
	public Roommate(){
		this("�Ǥ�", "enf's roommate");
	}
	
	public Roommate(String chiName, String engName) {
		super(chiName, engName);
		String desc = "�N�ت��Ǥ͡C���ɷ|�M�N�ؤ@�_��CS�A�@�_���U���ܵ����C���L�{\n";
		desc += "�b�ݰ_�ӫܤ����`�A�y�W�o�g�����H�Υ����嵷�����������A�Pı��L��\n";
		desc += "�M�I�ʡC�b�A�٨Ӥ��νT�{�o�ͤF����Ƥ��e�A�L�w�g���t�a�V�A���L�ӡC";
		this.setDesc(desc);
		this.addAttribute(attribute.HP, 50);
		
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		EventUtil.executeEventMessage(g, "talk_to_roommate");
		if (g.getConfigData().get(config.TUTORIAL_ON)){
			EventUtil.executeEventMessage(g, "battle_tutorial");
			// TODO: add modify config method & explanation
			// buf.append("�����]�w���w�]�Ȭ�\"�D�Y�ɾ԰�\"�A�A�i�H�z�Lblabla�ק復�C");
			new FirstTutorialBattle(this.getMyGroup(), g);
		}
		else {
			new BattleTask(this.getMyGroup(), g);
			g.setInEvent(false);
		}
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
	
	@Override
	public void doEventWhenGroupDown(PlayerGroup pg){
		MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
		mm.setState(MainMission.State.AFTER_FIRST_BATTLE);
		pg.setInBattle(false);
		pg.setBattleTask(null);
		EventUtil.doRoomEvent(pg);
	}
}
