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
			CommandServer.informGroup(src.getMyGroup(), String.format("%s�ѩ󵥯Ť����A�L�k�ϥ�%s!\n", 
					src.getChiName(), this.getChiName()));
			return false;
		}
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		if (src.getLevel() >= this.getLevel()) return true;
		else {
			CommandServer.informGroup(src.getMyGroup(), String.format("%s�ѩ󵥯Ť����A�L�k�ϥ�%s!\n", 
					src.getChiName(), this.getChiName()));
			return false;
		}
	}

	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(super.display());
		buf.append("\n�ϥε��šG" + this.getLevel() + "\n");
		buf.append("�ϥήĪG�G");
		buf.append(this.useEffect() + "\n");
		return buf.toString();
	}
}
