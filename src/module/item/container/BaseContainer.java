package module.item.container;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.AbstractItem;
import module.item.ItemList;
import module.item.SingleItemList;
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
	protected String keyName = null;

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
		
		if (this.status != doorStatus.OPENED){
			CommandServer.informGroup(g, "這個容器現在是關著的。\n");
			return;
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append(this.getChiName() + "裡面有下列物品：\n");
		String content = list.displayInfo();
		if (content.equals(""))
			buf.append("空無一物。\n");
		else
			buf.append(content + "\n");
		CommandServer.informGroup(g, buf.toString());
	}

	@Override
	public boolean onGetContent(ICharacter c, String target) {
		Group g = c.getMyGroup();
		
		if (this.status != doorStatus.OPENED){
			CommandServer.informGroup(g, "這個容器現在是關著的。\n");
			return false;
		}
		IItem obj = list.findItem(target);
		if (obj != null) {
			list.removeItem(obj);
			g.getInventory().addItem(obj);
			g.getAtRoom().informRoom(String.format("%s從%s中拿出了%s。\n", 
					g.list.get(0).charList.get(0).getChiName(),
					this.getChiName(), obj.getChiName()));
			return true;
		} else
			CommandServer.informGroup(g, this.getChiName() + "裡面並沒有你想拿的東西。\n");
		return false;
	}

	@Override
	public boolean onPutContent(ICharacter c, String target) {
		Group g = c.getMyGroup();
		
		if (this.status != doorStatus.OPENED){
			CommandServer.informGroup(g, "這個容器現在是關著的。\n");
			return false;
		}
		IItem obj = g.getInventory().findItem(target);
		if (obj != null){
			g.getInventory().removeItem(obj);
			list.addItem(obj);
			g.getAtRoom().informRoom(String.format("%s把%s放入了%s。\n", 
					g.list.get(0).charList.get(0).getChiName(),
					obj.getChiName(), this.getChiName()));
			return true;
		} else
			CommandServer.informGroup(g, "你身上並沒有那樣東西。\n");
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
		Group g = c.getMyGroup();
		
		if (this.attribute == doorAttribute.LOCKABLE){
			switch (this.status){
			case OPENED:
				CommandServer.informGroup(g, "這個容器現在是打開著的。\n");
				break;
			case LOCKED:
				CommandServer.informGroup(g, "這個容器已經是鎖著的了。\n");
				break;
			case CLOSED:
				if (hasKey(c)){
					this.status = doorStatus.LOCKED;
					g.getAtRoom().informRoom(String.format("%s鎖上了%s。\n", c.getChiName(), this.getChiName()));
					return true;
				} else
					CommandServer.informGroup(g, c.getChiName() + "身上並沒有帶著合適的鑰匙。\n");
			}
		} else
			CommandServer.informGroup(g, "這個容器是無法上鎖的喔。\n");
		return false;
	}

	@Override
	public boolean onUnlock(ICharacter c) {
		Group g = c.getMyGroup();
		
		switch (this.status){
		case OPENED: case CLOSED:
			CommandServer.informGroup(g, "這個容器並沒有上鎖。\n");
			break;
		case LOCKED:
			if (hasKey(c)){
				this.status = doorStatus.CLOSED;
				g.getAtRoom().informRoom(String.format("%s解開了%s上面的鎖。\n", 
						c.getChiName(), this.getChiName()));
				return true;
			} else
				CommandServer.informGroup(g, c.getChiName() + "身上並沒有帶著合適的鑰匙。\n");
		}
		
		return false;
	}

	@Override
	public ItemList getItemList() {
		return this.list;
	}

	@Override
	public boolean onOpen(ICharacter c) {
		Group g = c.getMyGroup();
		
		if (this.attribute == doorAttribute.BROKEN) {
			CommandServer.informGroup(g, "這個容器是打不開的喔!\n");
			return false;
		}
		
		switch (this.status){
		case OPENED:
			CommandServer.informGroup(g, "這個容器已經是開著的了。\n");
			break;
		case LOCKED:
			CommandServer.informGroup(g, "這個容器現在是鎖著的。\n");
			break;
		case CLOSED:
			this.status = doorStatus.OPENED;
			g.getAtRoom().informRoom(String.format("%s打開了%s。\n", 
					c.getChiName(), this.getChiName()));
			return true;
		}
		return false;
	}

	@Override
	public boolean onClose(ICharacter c) {
		Group g = c.getMyGroup();
		
		if (this.attribute == doorAttribute.BROKEN){
			CommandServer.informGroup(g, "這個容器是關不起來的喔!\n");
			return false;
		}
		
		switch (this.status){
		case CLOSED: case LOCKED:
			CommandServer.informGroup(g, "這個容器已經是關著的了。\n");
			break;
		case OPENED:
			this.status = doorStatus.CLOSED;
			g.getAtRoom().informRoom(String.format("%s關上了%s。\n", 
					c.getChiName(), this.getChiName()));
		}
		return false;
	}
	
	private boolean hasKey(ICharacter c) {
		for (SingleItemList list : c.getMyGroup().getInventory().itemList) {
			if (keyName.equals(list.list.get(0).getEngName()))
				return true;
		}
		return false;
	}
}
