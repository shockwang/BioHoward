package module.character.instance.chapter0;

import module.character.BaseHumanCharacter;
import module.character.api.ICharacter;
import module.character.constants.CStatus.status;

public class SluggishStudent extends BaseHumanCharacter{

	public SluggishStudent(){
		this("呆滯的學生", "sluggish student");
		StringBuffer buf = new StringBuffer();
		buf.append("隨處可見的一般大學生。受到不知名的影響，現在目光呆滯，面無表情。\n");
		buf.append("他動也不動的蹲在地上，嘴巴微開，對你的到來一點反應也沒有。");
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
		c.getAtRoom().informRoom(String.format("%s嘗試跟%s交談，但他一點反應也沒有。\n", 
				c.getChiName(), this.getChiName()));
	}
}
