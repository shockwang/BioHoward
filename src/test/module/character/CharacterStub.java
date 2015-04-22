package test.module.character;

import java.util.concurrent.ConcurrentHashMap;

import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment;
import module.item.api.IEquipment.EquipType;

public class CharacterStub implements ICharacter{

	@Override
	public void addAttribute(attribute atr, int MaxValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConcurrentHashMap<attribute, IntPair> getAttributeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentAttribute(attribute atr) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEngName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxAttribute(attribute atr) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpecialStatus(specialStatus ss) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ConcurrentHashMap<specialStatus, Integer> getSpecialStatusMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus(status s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ConcurrentHashMap<status, Integer> getStatusMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAttribute(attribute atr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChiName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentAttribute(attribute atr, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEngName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxAttribute(attribute atr, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpecialStatus(specialStatus ss, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(status s, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttributeMap(ConcurrentHashMap<attribute, IntPair> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpecialStatusMap(ConcurrentHashMap<specialStatus, Integer> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatusMap(ConcurrentHashMap<status, Integer> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean battleAction(GroupList enemyGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Group getMyGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMyGroup(Group g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String showStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDesc(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTalk(PlayerGroup g) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setLevel(int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setEquipment(ConcurrentHashMap<EquipType, IEquipment> equipMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConcurrentHashMap<EquipType, IEquipment> getEquipment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void looting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHostile(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getHostile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doEventWhenGroupDown(PlayerGroup pg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRandomBodyPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBareHandAttackMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
