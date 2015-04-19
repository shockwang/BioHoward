package module.character;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute;
import module.character.constants.CSpecialStatus;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment;
import module.server.PlayerServer;
import module.utility.NpcActionUtil;
import module.utility.NpcBattleActionUtil;

public abstract class AbstractCharacter implements ICharacter {
	protected ConcurrentHashMap<attribute, IntPair> attributeMap = null;
	private ConcurrentHashMap<specialStatus, Integer> specialStatusMap = null;
	protected ConcurrentHashMap<status, Integer> statusMap = null;
	protected ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = null;
	private int level = 1;
	private boolean hostile = true;
	protected int timeCount = 0;
	protected int updateCount = 0;

	private String chiName = null;
	private String engName = null;
	private String description = null;
	private Group myGroup = null;

	public AbstractCharacter(String chiName, String engName) {
		this.chiName = chiName;
		this.engName = engName;
		attributeMap = new ConcurrentHashMap<attribute, IntPair>();
		addAttribute(attribute.HP, 60);
		specialStatusMap = new ConcurrentHashMap<specialStatus, Integer>();
		statusMap = new ConcurrentHashMap<status, Integer>();
		// default character body attribute
		statusMap.put(status.SPEED, 4000);
		statusMap.put(status.STRENGTH, 25);
		statusMap.put(status.CONSTITUTION, 5);
		equipMap = new ConcurrentHashMap<IEquipment.EquipType, IEquipment>();
	}

	@Override
	public void addAttribute(attribute atr, int MaxValue) {
		attributeMap.put(atr, new IntPair(MaxValue, MaxValue));
	}

	@Override
	public ConcurrentHashMap<attribute, IntPair> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public String getChiName() {
		return chiName;
	}

	@Override
	public int getCurrentAttribute(attribute atr) {
		return attributeMap.get(atr).getCurrent();
	}

	@Override
	public String getEngName() {
		return engName;
	}

	@Override
	public int getMaxAttribute(attribute atr) {
		return attributeMap.get(atr).getMax();
	}

	@Override
	public Group getMyGroup() {
		return myGroup;
	}

	@Override
	public int getSpecialStatus(specialStatus ss) {
		return specialStatusMap.get(ss);
	}

	@Override
	public ConcurrentHashMap<specialStatus, Integer> getSpecialStatusMap() {
		return specialStatusMap;
	}

	@Override
	public int getStatus(status s) {
		return statusMap.get(s);
	}

	@Override
	public ConcurrentHashMap<status, Integer> getStatusMap() {
		return statusMap;
	}

	@Override
	public void removeAttribute(attribute atr) {
		attributeMap.remove(atr);
	}

	@Override
	public void setAttributeMap(ConcurrentHashMap<attribute, IntPair> map) {
		this.attributeMap = map;
	}

	@Override
	public void setChiName(String name) {
		this.chiName = name;
	}

	@Override
	public void setCurrentAttribute(attribute atr, int value) {
		attributeMap.get(atr).setCurrent(value);
	}

	@Override
	public void setEngName(String name) {
		this.engName = name;
	}

	@Override
	public void setMaxAttribute(attribute atr, int value) {
		attributeMap.get(atr).setMax(value);
	}

	@Override
	public void setMyGroup(Group g) {
		this.myGroup = g;
	}

	@Override
	public void setSpecialStatus(specialStatus ss, int time) {
		this.specialStatusMap.put(ss, time);
	}

	@Override
	public void setSpecialStatusMap(
			ConcurrentHashMap<specialStatus, Integer> map) {
		this.specialStatusMap = map;
	}

	@Override
	public void setStatus(status s, int value) {
		this.statusMap.put(s, value);
	}

	@Override
	public void setStatusMap(ConcurrentHashMap<status, Integer> map) {
		this.statusMap = map;
	}

	@Override
	public boolean isDown() {
		if (attributeMap.get(attribute.HP).getCurrent() <= 0)
			return true;
		return false;
	}

	@Override
	public String showStatus() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(String.format("%s/%s ", this.chiName, this.engName));
		buffer.append(CAttribute.displayAttribute(this));
		// TODO: add the show special status method
		if (this.specialStatusMap.size() > 0){
			buffer.append("  " + CSpecialStatus.displaySpecialStatus(this));
		}
		return buffer.toString();
	}

	@Override
	public void normalAction() {
		// TODO: define the normal behavior
		this.timeCount++;
		if (timeCount % 3 == 0){
			if (PlayerServer.getRandom().nextBoolean())
				NpcActionUtil.randomGet(this);
		}
		
		if ((timeCount + 1) % 3 == 0){
			if (PlayerServer.getRandom().nextBoolean())
				NpcActionUtil.randomDrop(this);
		}
		
		if (timeCount == 10) {
			int ddd = PlayerServer.getRandom().nextInt(10);
			if (ddd < 7) NpcActionUtil.randomMove(this.getMyGroup());
			timeCount = 0;
		}
		
		// auto attack player group
		if (this.getHostile()){
			int ddd = PlayerServer.getRandom().nextInt(10);
			if (ddd < 7) NpcActionUtil.attackRandomPlayerGroup(this);
		}
	}

	@Override
	public void updateTime() {
		if (this.updateCount < 5) {
			this.updateCount++;
		} else {
			this.updateCount = 0;
			
			// TODO: define the recover method
			IntPair pair = this.attributeMap.get(attribute.HP);
			int value = pair.getCurrent() + (int) (pair.getMax() * 0.02);
			if (value > pair.getMax())
				value = pair.getMax();
			pair.setCurrent(value);
		}
		
		// TODO: define special status recover mechanism
		if (this.specialStatusMap.size() > 0){
			CSpecialStatus.updateSpecialStatus(this, 1);
		}
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		g.getAtRoom().informRoom(String.format("%s嘗試要和%s交談，但%s看來不想理他。\n", 
				g.list.get(0).charList.get(0).getChiName(), this.getChiName(), this.getChiName()));
	}
	
	@Override
	public int getLevel(){
		return this.level;
	}
	
	@Override
	public void setLevel(int level){
		this.level = level;
	}
	
	@Override
	public void setEquipment(ConcurrentHashMap<IEquipment.EquipType, IEquipment> map){
		this.equipMap = map;
	}
	
	@Override
	public ConcurrentHashMap<IEquipment.EquipType, IEquipment> getEquipment(){
		return this.equipMap;
	}
	
	@Override
	public void looting(){
		// default drop nothing
	}
	
	@Override
	public void setHostile(boolean value){
		this.hostile = value;
	}
	
	@Override
	public boolean getHostile(){
		return hostile;
	}
	
	@Override
	public void setDesc(String description) {
		this.description = description;
	}

	@Override
	public String getDesc() {
		return this.description;
	}

	@Override
	public boolean battleAction(GroupList enemyGroup) {
		NpcBattleActionUtil.randomAttack(this, enemyGroup);
		return true;
	}
}
