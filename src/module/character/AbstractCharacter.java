package module.character;

import java.util.concurrent.ConcurrentHashMap;

import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CSpecialStatus.specialStatus;
import module.character.constants.CStatus.status;
import module.item.api.IEquipment;
import module.server.PlayerServer;
import module.utility.NpcActionUtil;

public abstract class AbstractCharacter implements ICharacter {
	private ConcurrentHashMap<attribute, IntPair> attributeMap = null;
	private ConcurrentHashMap<specialStatus, Integer> specialStatusMap = null;
	private ConcurrentHashMap<status, Integer> statusMap = null;
	private ConcurrentHashMap<IEquipment.EquipType, IEquipment> equipMap = null;
	private int level = 1;

	private String chiName = null;
	private String engName = null;
	private Group myGroup = null;

	public AbstractCharacter(String chiName, String engName) {
		this.chiName = chiName;
		this.engName = engName;
		attributeMap = new ConcurrentHashMap<attribute, IntPair>();
		addAttribute(attribute.HP, 100);
		specialStatusMap = new ConcurrentHashMap<specialStatus, Integer>();
		statusMap = new ConcurrentHashMap<status, Integer>();
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
		return buffer.toString();
	}

	@Override
	public void normalAction() {
		// TODO: define the normal behavior
		//NpcActionUtil.randomMove(myGroup);
		int ddd = PlayerServer.getRandom().nextInt(50);
		if (ddd < 25) NpcActionUtil.randomGet(this);
		//ddd = PlayerServer.getRandom().nextInt(50);
		//if (ddd < 25) NpcActionUtil.randomDrop(this);
	}

	@Override
	public void updateTime() {
		// TODO: define the recover method
		IntPair pair = this.attributeMap.get(attribute.HP);
		int value = pair.getCurrent() + (int) (pair.getMax() * 0.02);
		if (value > pair.getMax())
			value = pair.getMax();
		pair.setCurrent(value);
	}
	
	@Override
	public String onTalk(PlayerGroup g){
		return this.getChiName() + "看來不想理你。";
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
}
