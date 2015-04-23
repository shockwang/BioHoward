package module.character.instance.chapter0;

import module.character.AbstractCharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;

public class DarkShadow extends AbstractCharacter{

	public DarkShadow() {
		this("黑影", "dark shadow");
		String desc = "一大團不斷變換形狀的黑影，不時伸出黑色的觸手在黑色團塊周圍晃\n";
		desc += "動著。它散發著陰冷的氣息，讓四周的空氣降溫了好幾度。你從它身上感受到\n";
		desc += "巨大的壓迫感，令你不禁顫抖起來。";
		this.setDesc(desc);
		
		this.bodyPartList = new String[] {
				"中心區塊", "左邊觸手", "右邊觸手"
		};
		
		this.addAttribute(attribute.HP, 200);
		this.statusMap.put(status.STRENGTH, 300);
		this.statusMap.put(status.CONSTITUTION, 10);
		this.statusMap.put(status.SPEED, 4000);
		this.resistSpecialStatusSet.add(specialStatus.BLIND);
	}
	
	public DarkShadow(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public String getBareHandAttackMessage() {
		return "伸出觸手刺向";
	}

	@Override
	public void normalAction() {
		// do nothing
	}
}
