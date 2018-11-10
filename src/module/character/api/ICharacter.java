package module.character.api;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.PlayerCharacter;
import module.character.constants.CAttribute;
import module.character.constants.CSpecialStatus;
import module.character.constants.CStatus;
import module.item.ItemList;
import module.item.api.IEquipment;
import module.map.api.IRoom;
import module.time.api.Updatable;

public interface ICharacter extends Updatable, Serializable{
	// setup character English name
	void setEngName(String name);
	String getEngName();
	
	// setup character Chinese name
	void setChiName(String name);
	String getChiName();
	
	// setup character description
	void setDesc(String description);
	String getDesc();
	
	// add/delete Attributes (ex. hp, mp, sp...)
	void setAttributeMap(ConcurrentHashMap<CAttribute.attribute, IntPair> map);
	ConcurrentHashMap<CAttribute.attribute, IntPair> getAttributeMap();
	void addAttribute(CAttribute.attribute atr, int MaxValue);
	void removeAttribute(CAttribute.attribute atr);
	
	// setup Attribute values
	void setCurrentAttribute(CAttribute.attribute atr, int value);
	int getCurrentAttribute(CAttribute.attribute atr);
	void setMaxAttribute(CAttribute.attribute atr, int value);
	int getMaxAttribute(CAttribute.attribute atr);
	
	// setup Status
	void setStatusMap(ConcurrentHashMap<CStatus.status, Integer> map);
	ConcurrentHashMap<CStatus.status, Integer> getStatusMap();
	void setStatus(CStatus.status s, int value);
	int getStatus(CStatus.status s);
	
	// setup SpecialStatus
	void setSpecialStatusMap(ConcurrentHashMap<CSpecialStatus.specialStatus, Integer> map);
	ConcurrentHashMap<CSpecialStatus.specialStatus, Integer> getSpecialStatusMap();
	void setSpecialStatus(CSpecialStatus.specialStatus ss, int time);
	int getSpecialStatus(CSpecialStatus.specialStatus ss);
	
	// resistance of special status
	void setSpecialStatusResistance(CSpecialStatus.specialStatus ss);
	boolean removeSpecialStatusResistance(CSpecialStatus.specialStatus ss);
	boolean resistSpecialStatus(CSpecialStatus.specialStatus ss);
	
	// battle action, true = done the action, false = not yet
	boolean battleAction(CharList enemyList);
	boolean isDown();  // true if the character is not able to fight anymore.
	void doEventWhenGroupDown(PlayerCharacter pg); // do events when the belong group is down
	String getRandomBodyPart();
	String getBareHandAttackMessage();
	void setInBattle(boolean value);
	boolean getInBattle();
	void setIsRespawn(boolean value);
	boolean getIsRespawn();
	
	// auto attack player group or not
	void setHostile(boolean value);
	boolean getHostile();
	
	// character level
	void setLevel(int level);
	int getLevel();
	
	// character equipment
	void setEquipment(ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap);
	ConcurrentHashMap<IEquipment.EquipType, IEquipment> getEquipment();
	
	// normal action when is not in battle
	void normalAction();
	
	// other info
	ItemList getInventory();
	void setAtRoom(IRoom r);
	IRoom getAtRoom();
	
	// show status
	String showStatus();
	
	// react with others
	void onTalk(ICharacter c);
	void setTalking(boolean value);
	boolean getTalking();
	void setInEvent(boolean value);
	boolean getInEvent();
	
	// left items when dead
	void looting();
	
	// location
	void setInitialRoom(IRoom r);
	IRoom getInitialRoom();
}
