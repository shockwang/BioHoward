package module.item;

import java.util.concurrent.ConcurrentHashMap;

import module.character.Group;
import module.character.api.ICharacter;
import module.item.api.IEquipment;
import module.utility.ItemUtil;

public abstract class AbstractEquipment extends AbstractItem implements IEquipment{
	
	public AbstractEquipment(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean onWear(ICharacter c){
		Group g = c.getMyGroup();
		
		if (c.getLevel() < this.getLevel()){
			g.getAtRoom().informRoom(String.format(
					"%s嘗試裝備%d, 但由於等級不足而失敗。\n", c.getChiName(), this.getChiName()));
			return false;
		} 
		
		ConcurrentHashMap<EquipType, IEquipment> equipMap = c.getEquipment();
		IEquipment oldEquip = equipMap.get(this.getEquipType());
		if (oldEquip != null){
			if (oldEquip.onRemove(c)) {
				g.getAtRoom().informRoom(c.getChiName() + ItemUtil.wearMsg(this));
				equipMap.put(this.getEquipType(), this);
			} else return false;
		}
		return true;
	}
	
	@Override
	public boolean onRemove(ICharacter c){
		// default remove action
		c.getEquipment().remove(this);
		c.getMyGroup().getAtRoom().informRoom(c.getChiName() + 
				"卸下了" + this.getChiName() + "\n");
		return true;
	}
}
