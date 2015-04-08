package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.item.useable.AbstractUsableItem;
import module.utility.BattleUtil;

public class Juice extends AbstractUsableItem{
	private int count;
	
	public Juice(){
		this("一杯果汁", "juice");
		this.setDescription("看起來像是葡萄汁的顏色，希望還沒過期。");
		this.setPrice(50);
		count = 0;
	}
	
	public Juice(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean onUse(ICharacter src) {
		Group g = src.getMyGroup();
		
		if (super.onUse(src)){
			StringBuffer buf = new StringBuffer();
			buf.append(src.getChiName() + "喝了一口果汁，");
			if (BattleUtil.characterAttributeChange(src, attribute.HP, 5))
				buf.append("回復了5點體力。\n");
			else
				buf.append("但什麼事都沒發生。\n");
			
			g.getAtRoom().informRoom(buf.toString());
			count++;
			checkEmpty(src);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();
		
		if (super.onUse(src)){
			if (src == target) return this.onUse(src);
			
			StringBuffer buf = new StringBuffer();
			buf.append(String.format("%s讓%s喝了一口果汁，", src.getChiName(), target.getChiName()));
			if (BattleUtil.characterAttributeChange(target, attribute.HP, 5))
				buf.append("使他回復了5點體力。\n");
			else
				buf.append("但什麼事都沒發生。\n");
			
			g.getAtRoom().informRoom(buf.toString());
			count++;
			checkEmpty(src);
			return true;
		}
		return false;
	}

	@Override
	public String useEffect() {
		return "吸一口回復5點體力。";
	}
	
	private void checkEmpty(ICharacter src){
		if (count >= 15){
			src.getMyGroup().getAtRoom().informRoom(String.format("杯子裡的果汁喝完了，%s隨手扔掉了它。\n", 
					src.getChiName()));
			src.getMyGroup().getInventory().removeItem(this);
		}
	}
}
