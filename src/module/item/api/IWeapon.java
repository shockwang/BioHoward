package module.item.api;

import module.character.api.ICharacter;

public interface IWeapon extends IEquipment{
	enum WeaponType{
		PINGPONG,
		AMPLIFIER,
		GUITAR,
		BOOK,
		JOYSTICK
	}
	
	// weapon type
	void setWeaponType(WeaponType type);
	WeaponType getWeaponType();
	
	void onAttack(ICharacter src, ICharacter target);
}
