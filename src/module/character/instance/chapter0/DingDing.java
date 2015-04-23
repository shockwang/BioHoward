package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;

public class DingDing extends AbstractCharacter{
	
	public DingDing(){
		this("丁丁", "ding ding");
		String desc = "清大裡面資歷甚老的校狗，其他的狗都得敬畏她三分。走路的時候屁\n";
		desc += "股一扭一扭的很可愛，喜歡跟著學生到處跑，也很愛跟學生們玩耍。";
		this.setDesc(desc);
		this.bodyPartList = new String[] {
				"頭部", "胸部", "背部", "前腳", "後腳", "屁股", "腹部"
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
		return "張嘴咬向";
	}
	
	@Override
	public void onTalk(PlayerGroup pg){
		pg.getAtRoom().informRoom("丁丁對" + pg.list.get(0).charList.get(0).getChiName() + "熱情的搖尾巴。\n");
	}
}
