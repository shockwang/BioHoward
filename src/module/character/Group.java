package module.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.battle.BattleTask;
import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CAttribute;
import module.command.api.IndexStringPair;
import module.item.ItemList;
import module.map.api.IRoom;
import module.time.api.Updatable;
import module.utility.Parse;
import module.utility.Search;

public class Group implements Updatable {
	public List<CharList> list;
	private String Chiname;
	private String EngName;
	private boolean inBattle = false;
	private BattleTask myBattle = null;
	private IRoom atRoom = null; // judge this group is at what place
	private IRoom initialRoom = null; // the place that group should locate at
										// when respawn
	private IntPair respawnTime = new IntPair(0, 30);
	private boolean isRespawn = true;  // group will respawn after period of time if true
	private int charNum;
	private ItemList itemList = null;
	private Boolean isTalking = false;
	private Boolean isInEvent = false;

	public String getChiName() {
		return Chiname;
	}

	public String getEngName() {
		return EngName;
	}

	public void setInBattle(boolean in) {
		synchronized (this) {
			this.inBattle = in;
		}
	}

	public boolean getInBattle() {
		synchronized (this) {
			return inBattle;
		}
	}

	public void setBattleTask(BattleTask task) {
		this.myBattle = task;
	}

	public BattleTask getBattleTask() {
		return this.myBattle;
	}

	public void setAtRoom(IRoom here) {
		this.atRoom = here;
	}

	public IRoom getAtRoom() {
		return this.atRoom;
	}

	public IRoom getInitialRoom() {
		return this.initialRoom;
	}

	public void setInitialRoom(IRoom room) {
		this.initialRoom = room;
	}

	public IntPair getRespawnTime() {
		return this.respawnTime;
	}

	public Group(ICharacter obj) {
		CharList firstObj = new CharList(obj); // Group is responsible for
		// newing CharList
		obj.setMyGroup(this);
		list = Collections.synchronizedList(new ArrayList<CharList>());
		list.add(firstObj);
		this.Chiname = obj.getChiName();
		this.EngName = String.format(obj.getEngName());
		this.charNum = 1;
		this.itemList = new ItemList();
	}

	public void addChar(ICharacter obj) {
		this.charNum++;
		this.judgeName();
		for (CharList exist : list) {
			if (Search.searchName(exist.charList.get(0).getEngName(),
					obj.getEngName())) {
				exist.charList.add(obj);
				obj.setMyGroup(this);
				return;
			}
		}
		// no data matched, create a new CharList
		list.add(new CharList(obj));
		obj.setMyGroup(this);
	}

	public boolean removeChar(ICharacter target) {
		boolean objExists = false;
		for (CharList exist : list) {
			if (Search.searchName(exist.charList.get(0).getEngName(),
					target.getEngName())) {
				if (exist.removeChar(target)) {
					if (exist.charList.isEmpty())
						list.remove(exist);
					objExists = true;
					target.setMyGroup(null);
					// decrease char number
					this.charNum--;
					this.judgeName();
					break;
				}
			}
		}
		return objExists;
	}

	public ICharacter findChar(String name, int index) {
		for (CharList exist : list) {
			if (Search.searchName(exist.charList.get(0).getEngName(), name)) {
				try {
					return exist.charList.get(index);
				} catch (IndexOutOfBoundsException e) {
					return null;
				}
			}
		}
		return null;
	}

	public ICharacter findChar(String name) {
		IndexStringPair pair = Parse.parseName(name);
		return this.findChar(pair.name, pair.index);
	}

	public ICharacter findAliveChar(String name, int index) {
		int count = 0;

		for (CharList exist : list) {
			for (ICharacter c : exist.charList) {
				if (Search.searchName(c.getEngName(), name)) {
					if (!c.isDown()) {
						if (count == index)
							return c;
						else
							count++;
					}
				}
			}
		}
		return null;
	}

	public ICharacter findAliveChar(String name) {
		IndexStringPair pair = Parse.parseName(name);
		return this.findAliveChar(pair.name, pair.index);
	}

	public String displayInfo() {
		if (this.charNum == 1)
			return this.list.get(0).charList.get(0).getDesc() + "\n";
		else {
			StringBuffer buffer = new StringBuffer();
			buffer.append(Chiname + "\n");
			for (CharList exist : list)
				buffer.append("   " + exist.displayInfo());
			return buffer.toString();
		}
	}
	
	public String displayInfoInBattle(){
		if (this.charNum == 1) return "";
		else {
			StringBuffer buffer = new StringBuffer();
			for (CharList exist : list)
				buffer.append("   " + exist.displayInfo());
			return buffer.toString();
		}
	}

	@Override
	public void updateTime() {
		// TODO Auto-generated method stub
		// must not implement time waste instructions in this method!
		if (getGroupDown() && isRespawn) { // waiting for respawn
			int current = respawnTime.getCurrent();
			System.out.println("respawn count: " + current);
			current++;
			if (current >= respawnTime.getMax()) {
				respawnTime.setCurrent(0);
				recoverGroup();
				this.initialRoom.getGroupList().gList.add(this);
				this.initialRoom
						.informRoom(this.getChiName() + "從一旁的草叢中鑽出來.\n");
				this.atRoom = this.initialRoom;
			} else
				respawnTime.setCurrent(current);
		} else {
			for (CharList cList : list) {
				for (ICharacter c : cList.charList) {
					c.updateTime();
				}
			}
			if (!this.inBattle)
				this.list.get(0).charList.get(0).normalAction();
		}

		/*
		 * else if (!this.inBattle) { // normal state for (CharList cList :
		 * list){ for (ICharacter c : cList.charList){ c.updateTime(); } }
		 * this.list.get(0).charList.get(0).normalAction(); } else if
		 * (this.inBattle){ // character is in the battle // do nothing first }
		 */
	}

	public boolean getGroupDown() {
		for (CharList cList : this.list) {
			for (ICharacter c : cList.charList) {
				if (!c.isDown())
					return false;
			}
		}
		return true;
	}

	public void recoverGroup() {
		for (CharList cList : this.list) {
			for (ICharacter c : cList.charList) {
				CAttribute.fullRecover(c);
			}
		}
	}

	private void judgeName() {
		if (this.charNum == 1) {
			this.EngName = this.list.get(0).charList.get(0).getEngName();
			this.Chiname = this.list.get(0).charList.get(0).getChiName();
		} else {
			this.EngName = String.format("%s's g", this.list.get(0).charList
					.get(0).getEngName());
			this.Chiname = String.format("%s的隊伍", this.list.get(0).charList
					.get(0).getChiName());
		}
	}
	
	public void setInventory(ItemList list){
		this.itemList = list;
	}
	
	public ItemList getInventory(){
		return this.itemList;
	}
	
	public void setTalking(boolean value){
		synchronized (this){
			this.isTalking = value;
		}
	}
	
	public boolean getTalking(){
		synchronized (this){
			return this.isTalking;
		}
	}
	
	public void setInEvent(boolean value){
		synchronized (this){
			this.isInEvent = value;
		}
	}
	
	public boolean getInEvent(){
		synchronized (this) {
			return this.isInEvent;
		}
	}
	
	public void setIsRespawn(boolean value){
		this.isRespawn = value;
	}
	
	public boolean getIsRespawn(){
		return this.isRespawn;
	}
}
