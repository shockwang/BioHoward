package module.character.instance.chapter0;

import module.character.BaseHumanCharacter;
import module.character.api.ICharacter;
import module.character.constants.CStatus.status;

public class SluggishStudent extends BaseHumanCharacter{

	public SluggishStudent(){
		this("�b�����ǥ�", "sluggish student");
		StringBuffer buf = new StringBuffer();
		buf.append("�H�B�i�����@��j�ǥ͡C���줣���W���v�T�A�{�b�إ��b���A���L���C\n");
		buf.append("�L�ʤ]���ʪ��ۦb�a�W�A�L�ڷL�}�A��A����Ӥ@�I�����]�S���C");
		this.setDesc(buf.toString());
		this.setHostile(false);
		this.setStatus(status.STRENGTH, 25);
	}
	
	public SluggishStudent(String chiName, String engName) {
		super(chiName, engName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
	
	@Override
	public void onTalk(ICharacter c){
		c.getAtRoom().informRoom(String.format("%s���ո�%s��͡A���L�@�I�����]�S���C\n", 
				c.getChiName(), this.getChiName()));
	}
}
