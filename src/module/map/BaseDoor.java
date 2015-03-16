package module.map;

import module.character.api.ICharacter;
import module.item.SingleItemList;
import module.item.api.IItem;
import module.map.api.IDoor;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CDoorAttribute.doorStatus;

public class BaseDoor implements IDoor {
	private String description = null;
	private PositionDoor pd = null;
	private doorAttribute da;
	private doorStatus ds;
	private IItem key = null;

	public BaseDoor(String desc, PositionDoor pd) {
		this.description = desc;
		this.pd = pd;
		this.da = doorAttribute.UNLOCKABLE;
		this.ds = doorStatus.OPENED;
	}

	@Override
	public void setDescription(String des) {
		this.description = des;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDoorPosition(PositionDoor pd) {
		this.pd = pd;
	}

	@Override
	public PositionDoor getDoorPosition() {
		return this.pd;
	}

	@Override
	public void setDoorAttribute(doorAttribute da) {
		this.da = da;
	}

	@Override
	public doorAttribute getDoorAttribute() {
		return this.da;
	}

	@Override
	public void setDoorStatus(doorStatus ds) {
		this.ds = ds;
	}

	@Override
	public doorStatus getDoorStatus() {
		return this.ds;
	}

	@Override
	public void setKey(IItem key) {
		this.key = key;
	}

	@Override
	public IItem getKey() {
		return this.key;
	}

	@Override
	public boolean onLock(ICharacter c) {
		return hasKey(c);
	}

	@Override
	public boolean onUnlock(ICharacter c) {
		return hasKey(c);
	}

	private boolean hasKey(ICharacter c) {
		for (SingleItemList list : c.getMyGroup().getInventory().itemList) {
			if (key.getEngName().equals(list.list.get(0).getEngName()))
				return true;
		}
		return false;
	}
}
