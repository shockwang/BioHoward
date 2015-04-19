package module.item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment;
import module.utility.ItemUtil;

public class BaseEquipment extends AbstractItem implements IEquipment{
	protected ConcurrentHashMap<attribute, Integer> attrMap = null;
	protected ConcurrentHashMap<status, Integer> statMap = null;
	private EquipType type;
	
	public BaseEquipment(String chiName, String engName, EquipType type) {
		super(chiName, engName);
		this.type = type;
		attrMap = new ConcurrentHashMap<attribute, Integer>();
		statMap = new ConcurrentHashMap<status, Integer>();
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
				g.getAtRoom().informRoom(c.getChiName() + ItemUtil.wearMsg(this) + "\n");
				g.getInventory().removeItem(this);
				equipMap.put(this.getEquipType(), this);
			} else return false;
		} else {
			g.getAtRoom().informRoom(c.getChiName() + ItemUtil.wearMsg(this) + "\n");
			g.getInventory().removeItem(this);
			equipMap.put(this.getEquipType(), this);
		}
		return true;
	}
	
	@Override
	public boolean onRemove(ICharacter c){
		// default remove action
		// TODO: remove limitation?
		c.getEquipment().remove(this);
		c.getMyGroup().getInventory().addItem(this);
		c.getEquipment().remove(this.type);
		c.getMyGroup().getAtRoom().informRoom(c.getChiName() + 
				"卸下了" + this.getChiName() + "。\n");
		return true;
	}
	
	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(super.display());
		buf.append("裝備類型：" + ItemUtil.getEquipmentType(this) + "\t");
		buf.append("裝備等級：" + this.getLevel() + "\n");
		buf.append("影響：\n");
		
		int count = 0;
		for (Map.Entry<attribute, Integer> entry : attrMap.entrySet()){
			buf.append(String.format(
					"%s：%d\t", entry.getKey().getName(), entry.getValue()));
			count++;
			if (count == 3) {
				buf.append("\n");
				count = 0;
			}
		}
		if (count != 0) buf.append("\n");
		
		count = 0;
		for (Map.Entry<status, Integer> entry : statMap.entrySet()){
			buf.append(String.format(
					"%s：%d\t", entry.getKey().getName(), entry.getValue()));
			count++;
			if (count == 3) {
				buf.append("\n");
				count = 0;
			}
		}
		if (count != 0) buf.append("\n");
		
		return buf.toString();
	}
	
	@Override
	public void setEquipType(EquipType type){
		this.type = type;
	}

	@Override
	public EquipType getEquipType() {
		return this.type;
	}

	@Override
	public ConcurrentHashMap<attribute, Integer> getAttribute() {
		return this.attrMap;
	}

	@Override
	public void setAttribute(ConcurrentHashMap<attribute, Integer> map) {
		this.attrMap = map;
	}

	@Override
	public ConcurrentHashMap<status, Integer> getStatus() {
		return this.statMap;
	}

	@Override
	public void setStatus(ConcurrentHashMap<status, Integer> map) {
		this.statMap = map;
	}
}
