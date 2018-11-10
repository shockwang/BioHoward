package module.character;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;
import module.item.ItemList;
import module.item.api.IEquipment;
import module.map.api.IRoom;
import module.server.PlayerServer;
import module.utility.NpcActionUtil;
import module.utility.NpcBattleActionUtil;

public abstract class AbstractCharacter implements ICharacter{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4972134458071665612L;
	
	protected ConcurrentHashMap<attribute, IntPair> attributeMap = null;
	private ConcurrentHashMap<specialStatus, Integer> specialStatusMap = null;
	protected ConcurrentHashMap<status, Integer> statusMap = null;
	protected ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = null;
	protected HashSet<specialStatus> resistSpecialStatusSet = null;
	protected String[] bodyPartList = null;
	private int level = 1;
	private boolean hostile = true;
	private boolean inBattle = false;
	private boolean talking = false;
	private boolean inEvent = false;
	private boolean isRespawn = false;
	protected int timeCount = 0;
	protected int updateCount = 0;

	private String chiName = null;
	private String engName = null;
	private String description = null;
	
	private ItemList itemList;
	private IRoom initialRoom;
	private IRoom atRoom;

	public AbstractCharacter(String chiName, String engName) {
		this.chiName = chiName;
		this.engName = engName;
		attributeMap = new ConcurrentHashMap<attribute, IntPair>();
		addAttribute(attribute.HP, 50);
		specialStatusMap = new ConcurrentHashMap<specialStatus, Integer>();
		statusMap = new ConcurrentHashMap<status, Integer>();
		resistSpecialStatusSet = new HashSet<specialStatus>();
		// default character body attribute
		statusMap.put(status.SPEED, 4000);
		statusMap.put(status.STRENGTH, 25);
		statusMap.put(status.CONSTITUTION, 5);
		equipMap = new ConcurrentHashMap<IEquipment.EquipType, IEquipment>();
		
		itemList = new ItemList();
		initialRoom = null;
		atRoom = null;
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
			if (ddd < 7) NpcActionUtil.randomMove(this);
			timeCount = 0;
		}
		
		// auto attack player group
		if (this.getHostile()){
			int ddd = PlayerServer.getRandom().nextInt(10);
			if (ddd < 7) NpcActionUtil.attackRandomPlayer(this);
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
			int value = pair.getCurrent() + (int) (pair.getMax() * 0.02) + 1;
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
	public void onTalk(ICharacter c){
		c.getAtRoom().informRoom(String.format("%s嘗試要和%s交談，但%s看來不想理他。\n", 
				c.getChiName(), this.getChiName(), this.getChiName()));
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
	public boolean battleAction(CharList enemyList) {
		//NpcBattleActionUtil.randomAttack(this, enemyList);
		return true;
	}
	
	@Override
	public void doEventWhenGroupDown(PlayerCharacter pg){
		// do nothing default
		return;
	}
	
	@Override
	public String getRandomBodyPart(){
		int choose = PlayerServer.getRandom().nextInt(this.bodyPartList.length);
		return this.bodyPartList[choose];
	}
	
	@Override
	public void setSpecialStatusResistance(CSpecialStatus.specialStatus ss){
		this.resistSpecialStatusSet.add(ss);
	}
	
	@Override
	public boolean removeSpecialStatusResistance(CSpecialStatus.specialStatus ss){
		return this.removeSpecialStatusResistance(ss);
	}
	
	@Override
	public boolean resistSpecialStatus(CSpecialStatus.specialStatus ss){
		return this.resistSpecialStatusSet.contains(ss);
	}
	
	@Override
	public ItemList getInventory() {
		return this.itemList;
	}
	
	@Override
	public IRoom getAtRoom() {
		return this.atRoom;
	}
	
	@Override
	public void setAtRoom(IRoom r) {
		this.atRoom = r;
	}
	
	@Override
	public void setInitialRoom(IRoom r) {
		this.initialRoom = r;
	}
	
	@Override
	public IRoom getInitialRoom() {
		return this.initialRoom;
	}
	
	@Override
	public void setInBattle(boolean value) {
		this.inBattle = value;
	}
	
	@Override
	public boolean getInBattle() {
		return this.inBattle;
	}
	
	@Override
	public void setTalking(boolean value) {
		this.talking = value;
	}
	
	@Override
	public boolean getTalking() {
		return this.talking;
	}
	
	@Override
	public void setInEvent(boolean value) {
		this.inEvent = value;
	}
	
	@Override
	public boolean getInEvent() {
		return this.inEvent;
	}
	
	@Override
	public void setIsRespawn(boolean value) {
		this.isRespawn = value;
	}
	
	@Override
	public boolean getIsRespawn() {
		return this.isRespawn;
	}
}
