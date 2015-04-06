package module.item.container;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.AbstractItem;
import module.item.ItemList;
import module.item.api.IContainer;
import module.item.api.IItem;
import module.map.constants.CDoorAttribute;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;

public class BaseContainer extends AbstractItem implements IContainer{
	protected ItemList list;
	protected IContainer.Type type;
	protected CDoorAttribute.doorAttribute attribute;
	protected CDoorAttribute.doorStatus status;

	public BaseContainer(String chiName, String engName) {
		super(chiName, engName);
		list = new ItemList();
		type = Type.MOVEABLE;
		attribute = doorAttribute.UNLOCKABLE;
		status = doorStatus.CLOSED;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayContent(ICharacter c) {
		Group g = c.getMyGroup();
		
		StringBuffer buf = new StringBuffer();
		buf.append(this.getChiName() + "�̭����U�C���~�G\n");
		String content = list.displayInfo();
		if (content.equals(""))
			buf.append("�ŵL�@���C\n");
		else
			buf.append(content + "\n");
		CommandServer.informGroup(g, buf.toString());
	}

	@Override
	public boolean onGetContent(ICharacter c, String target) {
		Group g = c.getMyGroup();
		
		IItem obj = list.findItem(target);
		if (obj != null) {
			list.removeItem(obj);
			g.getInventory().addItem(obj);
			g.getAtRoom().informRoom(String.format("%s�q%s�����X�F%s�C\n", 
					g.list.get(0).charList.get(0).getChiName(),
					this.getChiName(), obj.getChiName()));
			return true;
		} else
			CommandServer.informGroup(g, this.getChiName() + "�̭��èS���A�Q�����F��C\n");
		return false;
	}

	@Override
	public boolean onPutContent(ICharacter c, String target) {
		Group g = c.getMyGroup();
		
		IItem obj = g.getInventory().findItem(target);
		if (obj != null){
			g.getInventory().removeItem(obj);
			list.addItem(obj);
			g.getAtRoom().informRoom(String.format("%s��%s��J�F%s�C\n", 
					g.list.get(0).charList.get(0).getChiName(),
					obj.getChiName(), this.getChiName()));
			return true;
		} else
			CommandServer.informGroup(g, "�A���W�èS�����˪F��C\n");
		return false;
	}

	@Override
	public void setType(Type t) {
		this.type = t;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public void setAttribute(doorAttribute attr) {
		this.attribute = attr;
	}

	@Override
	public doorAttribute getAttribute() {
		return this.attribute;
	}

	@Override
	public void setStatus(doorStatus status) {
		this.status = status;
	}

	@Override
	public doorStatus getStatus() {
		return this.status;
	}

	@Override
	public boolean onLock(ICharacter c) {
		return false;
	}

	@Override
	public boolean onUnlock(ICharacter c) {
		return false;
	}

	@Override
	public ItemList getItemList() {
		return this.list;
	}
}
