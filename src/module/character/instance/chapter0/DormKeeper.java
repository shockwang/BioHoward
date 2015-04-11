package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.constants.CAttribute.attribute;

public class DormKeeper extends AbstractCharacter{

	public DormKeeper(){
		this("�N��", "dorm keeper");
		StringBuffer buf = new StringBuffer();
		buf.append("�q�N���J�ٺ޲z�H���A���`��ǥ����M�������A���]���t�C�{�b�o��W\n");
		buf.append("�����K�ҡA�H�S�ۦV�A�ĹL�ӡA�A���ߪ��P�ɤ��T�h�æo����঳�o\n");
		buf.append("��j���O��C");
		this.setDesc(buf.toString());
		this.addAttribute(attribute.HP, 50);
		this.setHostile(true);
	}
	
	public DormKeeper(String chiName, String engName) {
		super(chiName, engName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
}
