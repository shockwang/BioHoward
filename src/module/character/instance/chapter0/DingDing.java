package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;

public class DingDing extends AbstractCharacter{
	
	public DingDing(){
		this("�B�B", "ding ding");
		String desc = "�M�j�̭�����ƦѪ��ժ��A��L�������o�q�Ȧo�T���C�������ɭԧ�\n";
		desc += "�Ѥ@��@�᪺�ܥi�R�A���w��۾ǥͨ�B�]�A�]�ܷR��ǥ̪ͭ��A�C";
		this.setDesc(desc);
		this.bodyPartList = new String[] {
				"�Y��", "�ݳ�", "�I��", "�e�}", "��}", "����", "����"
		};
		
		this.statusMap.put(status.STRENGTH, 40);
		this.addAttribute(attribute.HP, 200);
		this.statusMap.put(status.SPEED, 800);
		this.setHostile(false);
	}
	
	public DingDing(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public String getBareHandAttackMessage() {
		return "�i�L�r�V";
	}
	
	@Override
	public void onTalk(PlayerGroup pg){
		pg.getAtRoom().informRoom("�B�B��" + pg.list.get(0).charList.get(0).getChiName() + "�������n���ڡC\n");
	}
}
