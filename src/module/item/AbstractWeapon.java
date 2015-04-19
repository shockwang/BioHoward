package module.item;

import module.item.api.IWeapon;

public abstract class AbstractWeapon extends BaseEquipment implements IWeapon{
	private WeaponType type;
	protected double hitRatio = 0.8;
	
	public AbstractWeapon(String chiName, String engName){
		this(chiName, engName, EquipType.WEAPON);
	}

	public AbstractWeapon(String chiName, String engName, EquipType type) {
		super(chiName, engName, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setWeaponType(WeaponType type) {
		this.type = type;
	}

	@Override
	public WeaponType getWeaponType() {
		return type;
	}

	@Override
	public double getHitRatio() {
		return hitRatio;
	}
	
	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(super.display());
		buf.append("©R¤¤²v¡G" + this.hitRatio * 100.0 + "%\n");
		return buf.toString();
	}
}
