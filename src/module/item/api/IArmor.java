package module.item.api;

public interface IArmor extends IItem {
	enum ArmorType{
		HEAD,
		BODY,
		GLOVES,
		BOOTS
	}
	
	void underAttack(); // defense for special kind of attack?
}
