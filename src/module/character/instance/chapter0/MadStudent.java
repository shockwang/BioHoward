package module.character.instance.chapter0;

import module.battle.BattleTask;
import module.character.BaseHumanCharacter;
import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.character.constants.CStatus.status;

public class MadStudent extends BaseHumanCharacter{
	
	public MadStudent(){
		this("�g�ɪ��ǥ�", "mad student");
		StringBuffer buf = new StringBuffer();
		buf.append("�j�Ǹ��H�B�i�����ǥ͡C�ѩ󥢥h�z�����t�G�A��ۭ�ä����A���W�]\n");
		buf.append("���O�ǹСA��M�����a�b�M��i�H�������ؼСC�A�ݨ�L�R���c�N���V\n");
		buf.append("�A���L�ӡA�u�n�����`�N�ǳ����ԡC");
		this.setDesc(buf.toString());
		this.setStatus(status.STRENGTH, 28);
		this.setHostile(true);
	}
	
	public MadStudent(String chiName, String engName) {
		super(chiName, engName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onTalk(ICharacter c){
		c.getAtRoom().informRoom(String.format("%s�j�q�@�n�A��%s���F�L��!\n", 
				this.getChiName(), c.getChiName()));
		new BattleTask(this.getMyGroup(), g);
	}
}
