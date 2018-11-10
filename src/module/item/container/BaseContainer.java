package module.item.container;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = -1559407894289799330L;
	
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
		if (this.status != doorStatus.OPENED){
			CommandServer.informCharacter(c, "這個容器現在是關著的。\n");
			return;
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append(this.getChiName() + "裡面有下列物品：\n");
		String content = list.displayInfo();
		if (content.equals(""))
			buf.append("空無一物。\n");
		else
			buf.append(content + "\n");
		CommandServer.informCharacter(c, buf.toString());
	}

	@Override
	public boolean onGetContent(ICharacter c, String target) {
		if (this.status != doorStatus.OPENED){
			CommandServer.informCharacter(c, "這個容器現在是關著的。\n");
			return false;
		}
		IItem obj = list.findItem(target);
		if (obj != null) {
			list.removeItem(obj);
			c.getInventory().addItem(obj);
			c.getAtRoom().informRoom(String.format("%s從%s中拿出了%s。\n", 
					c.getChiName(),
					this.getChiName(), obj.getChiName()));
			return true;
		} else
			CommandServer.informCharacter(c, this.getChiName() + "裡面並沒有你想拿的東西。\n");
		return false;
	}

	@Override
	public boolean onPutContent(ICharacter c, String target) {
		if (this.status != doorStatus.OPENED){
			CommandServer.informCharacter(c, "這個容器現在是關著的。\n");
			return false;
		}
		IItem obj = c.getInventory().findItem(target);
		if (obj != null){
			c.getInventory().removeItem(obj);
			list.addItem(obj);
			c.getAtRoom().informRoom(String.format("%s把%s放入了%s。\n", 
					c.getChiName(),
					obj.getChiName(), this.getChiName()));
			return true;
		} else
			CommandServer.informCharacter(c, "你身上並沒有那樣東西。\n");
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
		if (this.attribute == doorAttribute.LOCKABLE){
			switch (this.status){
			case OPENED:
				CommandServer.informCharacter(c, "這個容器現在是打開著的。\n");
				break;
			case LOCKED:
				CommandServer.informCharacter(c, "這個容器已經是鎖著的了。\n");
				break;
			case CLOSED:
				if (hasKey(c)){
					this.status = doorStatus.LOCKED;
					c.getAtRoom().informRoom(String.format("%s鎖上了%s。\n", c.getChiName(), this.getChiName()));
					return true;
				} else
					CommandServer.informCharacter(c, "你身上並沒有帶著合適的鑰匙。\n");
			}
		} else
			CommandServer.informCharacter(c, "這個容器是無法上鎖的喔。\n");
		return false;
	}

	@Override
	public boolean onUnlock(ICharacter c) {
		switch (this.status){
		case OPENED: case CLOSED:
			CommandServer.informCharacter(c, "這個容器並沒有上鎖。\n");
			break;
		case LOCKED:
			if (hasKey(c)){
				this.status = doorStatus.CLOSED;
				c.getAtRoom().informRoom(String.format("%s解開了%s上面的鎖。\n", 
						c.getChiName(), this.getChiName()));
				return true;
			} else
				CommandServer.informCharacter(c, "你身上並沒有帶著合適的鑰匙。\n");
		}
		
		return false;
	}

	@Override
	public ItemList getItemList() {
		return this.list;
	}

	@Override
	public boolean onOpen(ICharacter c) {
		if (this.attribute == doorAttribute.BROKEN) {
			CommandServer.informCharacter(c, "這個容器開口壞掉了，打不開。\n");
			return false;
		}
		
		switch (this.status){
		case OPENED:
			CommandServer.informCharacter(c, "這個容器已經是開著的了。\n");
			break;
		case LOCKED:
			CommandServer.informCharacter(c, "這個容器現在是鎖著的。\n");
			break;
		case CLOSED:
			this.status = doorStatus.OPENED;
			c.getAtRoom().informRoom(String.format("%s打開了%s。\n", 
					c.getChiName(), this.getChiName()));
			return true;
		}
		return false;
	}

	@Override
	public boolean onClose(ICharacter c) {
		if (this.attribute == doorAttribute.BROKEN){
			CommandServer.informCharacter(c, "這個容器開口壞掉了，關不起來。\n");
			return false;
		}
		
		switch (this.status){
		case CLOSED: case LOCKED:
			CommandServer.informCharacter(c, "這個容器已經是關著的了。\n");
			break;
		case OPENED:
			this.status = doorStatus.CLOSED;
			c.getAtRoom().informRoom(String.format("%s關上了%s。\n", 
					c.getChiName(), this.getChiName()));
		}
		return false;
	}
	
	private boolean hasKey(ICharacter c) {
		for (SingleItemList list : c.getInventory().itemList) {
			if (keyName.equals(list.list.get(0).getEngName()))
				return true;
		}
		return false;
	}
}
