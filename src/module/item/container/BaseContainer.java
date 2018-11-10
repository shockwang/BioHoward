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
			CommandServer.informCharacter(c, "�o�Ӯe���{�b�O���۪��C\n");
			return;
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append(this.getChiName() + "�̭����U�C���~�G\n");
		String content = list.displayInfo();
		if (content.equals(""))
			buf.append("�ŵL�@���C\n");
		else
			buf.append(content + "\n");
		CommandServer.informCharacter(c, buf.toString());
	}

	@Override
	public boolean onGetContent(ICharacter c, String target) {
		if (this.status != doorStatus.OPENED){
			CommandServer.informCharacter(c, "�o�Ӯe���{�b�O���۪��C\n");
			return false;
		}
		IItem obj = list.findItem(target);
		if (obj != null) {
			list.removeItem(obj);
			c.getInventory().addItem(obj);
			c.getAtRoom().informRoom(String.format("%s�q%s�����X�F%s�C\n", 
					c.getChiName(),
					this.getChiName(), obj.getChiName()));
			return true;
		} else
			CommandServer.informCharacter(c, this.getChiName() + "�̭��èS���A�Q�����F��C\n");
		return false;
	}

	@Override
	public boolean onPutContent(ICharacter c, String target) {
		if (this.status != doorStatus.OPENED){
			CommandServer.informCharacter(c, "�o�Ӯe���{�b�O���۪��C\n");
			return false;
		}
		IItem obj = c.getInventory().findItem(target);
		if (obj != null){
			c.getInventory().removeItem(obj);
			list.addItem(obj);
			c.getAtRoom().informRoom(String.format("%s��%s��J�F%s�C\n", 
					c.getChiName(),
					obj.getChiName(), this.getChiName()));
			return true;
		} else
			CommandServer.informCharacter(c, "�A���W�èS�����˪F��C\n");
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
				CommandServer.informCharacter(c, "�o�Ӯe���{�b�O���}�۪��C\n");
				break;
			case LOCKED:
				CommandServer.informCharacter(c, "�o�Ӯe���w�g�O��۪��F�C\n");
				break;
			case CLOSED:
				if (hasKey(c)){
					this.status = doorStatus.LOCKED;
					c.getAtRoom().informRoom(String.format("%s��W�F%s�C\n", c.getChiName(), this.getChiName()));
					return true;
				} else
					CommandServer.informCharacter(c, "�A���W�èS���a�ۦX�A���_�͡C\n");
			}
		} else
			CommandServer.informCharacter(c, "�o�Ӯe���O�L�k�W�ꪺ��C\n");
		return false;
	}

	@Override
	public boolean onUnlock(ICharacter c) {
		switch (this.status){
		case OPENED: case CLOSED:
			CommandServer.informCharacter(c, "�o�Ӯe���èS���W��C\n");
			break;
		case LOCKED:
			if (hasKey(c)){
				this.status = doorStatus.CLOSED;
				c.getAtRoom().informRoom(String.format("%s�Ѷ}�F%s�W������C\n", 
						c.getChiName(), this.getChiName()));
				return true;
			} else
				CommandServer.informCharacter(c, "�A���W�èS���a�ۦX�A���_�͡C\n");
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
			CommandServer.informCharacter(c, "�o�Ӯe���}�f�a���F�A�����}�C\n");
			return false;
		}
		
		switch (this.status){
		case OPENED:
			CommandServer.informCharacter(c, "�o�Ӯe���w�g�O�}�۪��F�C\n");
			break;
		case LOCKED:
			CommandServer.informCharacter(c, "�o�Ӯe���{�b�O��۪��C\n");
			break;
		case CLOSED:
			this.status = doorStatus.OPENED;
			c.getAtRoom().informRoom(String.format("%s���}�F%s�C\n", 
					c.getChiName(), this.getChiName()));
			return true;
		}
		return false;
	}

	@Override
	public boolean onClose(ICharacter c) {
		if (this.attribute == doorAttribute.BROKEN){
			CommandServer.informCharacter(c, "�o�Ӯe���}�f�a���F�A�����_�ӡC\n");
			return false;
		}
		
		switch (this.status){
		case CLOSED: case LOCKED:
			CommandServer.informCharacter(c, "�o�Ӯe���w�g�O���۪��F�C\n");
			break;
		case OPENED:
			this.status = doorStatus.CLOSED;
			c.getAtRoom().informRoom(String.format("%s���W�F%s�C\n", 
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
