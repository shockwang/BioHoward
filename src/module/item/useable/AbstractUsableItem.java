package module.item.useable;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.AbstractItem;
import module.item.api.IUseable;

public abstract class AbstractUsableItem extends AbstractItem implements IUseable{

	public AbstractUsableItem(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean onUse(ICharacter src) {
		if (src.getLevel() >= this.getLevel()) return true;
		else {
			CommandServer.informGroup(src.getMyGroup(), String.format("%s由於等級不足，無法使用%s!\n", 
					src.getChiName(), this.getChiName()));
			return false;
		}
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		if (src.getLevel() >= this.getLevel()) return true;
		else {
			CommandServer.informGroup(src.getMyGroup(), String.format("%s由於等級不足，無法使用%s!\n", 
					src.getChiName(), this.getChiName()));
			return false;
		}
	}

	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(super.display());
		buf.append("\n使用等級：" + this.getLevel() + "\n");
		buf.append("使用效果：");
		buf.append(this.useEffect() + "\n");
		return buf.toString();
	}
}
