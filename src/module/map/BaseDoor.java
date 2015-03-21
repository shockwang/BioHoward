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
	private String keyName = null;

	public BaseDoor(String desc, PositionDoor pd) {
		this.description = desc;
		this.pd = pd;
		this.da = doorAttribute.UNLOCKABLE;
		this.ds = doorStatus.CLOSED;
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
	public void setKeyName(String name) {
		this.keyName = name;
	}

	@Override
	public String getKeyName() {
		return this.keyName;
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
			if (keyName.equals(list.list.get(0).getEngName()))
				return true;
		}
		return false;
	}
}
