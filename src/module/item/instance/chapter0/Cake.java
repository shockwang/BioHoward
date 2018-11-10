package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.item.useable.AbstractUsableItem;
import module.utility.BattleUtil;

public class Cake extends AbstractUsableItem {
	int recover = 30;

	public Cake() {
		this("85度C蛋糕", "cake");
		this.setDescription("一塊在85度C買的起司蛋糕，看起來很可口!");
		this.setPrice(85);
	}

	public Cake(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean onUse(ICharacter src) {
		if (super.onUse(src)) {
			StringBuffer buf = new StringBuffer();
			buf.append(String.format("%s一口吃下%s，", src.getChiName(), this.getChiName()));
			if (BattleUtil.characterAttributeChange(src, attribute.HP, this.recover))
				buf.append("回復了" + recover + "點體力。\n");
			else buf.append("但什麼事都沒有發生。\n");
			
			src.getAtRoom().informRoom(buf.toString());
			src.getInventory().removeItem(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		if (super.onUse(src)) {
			if (src == target) return this.onUse(src);
			else {
				StringBuffer buf = new StringBuffer();
				buf.append(String.format("%s讓%s吃下了%s，", src.getChiName(), 
						target.getChiName(), this.getChiName()));
				if (BattleUtil.characterAttributeChange(target, attribute.HP, recover))
					buf.append("使他回復了" + recover + "點體力。\n");
				else buf.append("但什麼事都沒有發生。\n");
				
				src.getAtRoom().informRoom(buf.toString());
				src.getInventory().removeItem(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public String useEffect() {
		return "回復" + recover + "點體力。";
	}

}
