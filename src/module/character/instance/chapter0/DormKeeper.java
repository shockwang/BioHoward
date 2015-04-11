package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.constants.CAttribute.attribute;

public class DormKeeper extends AbstractCharacter{

	public DormKeeper(){
		this("齋媽", "dorm keeper");
		StringBuffer buf = new StringBuffer();
		buf.append("義齋的宿舍管理人員，平常對學生雖然不熱情，但也不差。現在她手上\n");
		buf.append("緊抓著鐵棍，咆嘯著向你衝過來，你恐懼的同時不禁懷疑她為何能有這\n");
		buf.append("麼大的力氣。");
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
