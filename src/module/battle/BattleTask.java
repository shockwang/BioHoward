package module.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CConfig.config;
import module.character.constants.CStatus.status;
import module.command.CommandServer;
import module.utility.EnDecoder;
import module.utility.ItemUtil;

public class BattleTask extends TimerTask {
	private GroupList team1List;
	private GroupList team2List;
	private ConcurrentHashMap<ICharacter, IntPair> timeMap;
	private ArrayList<ICharacter> ready;
	private Timer battleTimer;
	private int updateCounter = 0;

	public BattleTask(Group team1, Group team2) {
		team1.setInBattle(true);
		team2.setInBattle(true);
		this.team1List = new GroupList();
		this.team2List = new GroupList();
		this.team1List.gList.add(team1);
		this.team2List.gList.add(team2);

		team1.setBattleTask(this);
		team2.setBattleTask(this);

		timeMap = new ConcurrentHashMap<ICharacter, IntPair>();
		addTimeMap(team1);
		addTimeMap(team2);

		battleTimer = new Timer();
		battleTimer.schedule(this, 0, 100);
	}

	public void addBattleGroup(Group addSide, Group targetG) {
		GroupList side;
		if (team1List.gList.contains(addSide))
			side = this.team1List;
		else
			side = this.team2List;

		targetG.setInBattle(true);
		side.gList.add(targetG);
		targetG.setBattleTask(this);
		addTimeMap(targetG);
	}
	
	public void addBattleOppositeGroup(Group opposite, Group targetG){
		GroupList side;
		if (team1List.gList.contains(opposite))
			side = this.team2List;
		else
			side = this.team1List;
		
		targetG.setInBattle(true);
		side.gList.add(targetG);
		targetG.setBattleTask(this);
		addTimeMap(targetG);
	}
	
	public void removeBattleGroup(Group g){
		GroupList side;
		if (team1List.gList.contains(g)) side = this.team1List;
		else side = this.team2List;
		
		if (g instanceof PlayerGroup){
			for (Group gg : this.getEnemyGroups(g).gList){
				gg.getAtRoom().informRoom(
						String.format("由於擊退玩家隊伍，%s士氣大振，回復到最佳狀態!\n", gg.getChiName()));
				gg.recoverGroup();
			}
		}
		side.gList.remove(g);
		g.setInBattle(false);
		g.setBattleTask(null);
		this.removeGroupFromTimeMap(g);
		if (side.gList.size() == 0)
			checkBattleEnd();
	}

	public void run() {
		synchronized (this) {
			ready = updateTime();
			updatePlayerStatus();
		}
		updatePlayerStatus(team1List.gList);
		updatePlayerStatus(team2List.gList);
		try {
			for (ICharacter c : ready) {
				if (c.getMyGroup() instanceof PlayerGroup) {
					if (((PlayerGroup) c.getMyGroup()).getConfigData().get(
							config.REALTIMEBATTLE)) {
						// real time battle

					} else {
						// blocks when a character in player's group is ready
						try {
							synchronized (this) {
								wait();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				c.battleAction(getEnemyGroups(c));
			}
		} catch (NullPointerException e) {
			return; // no one is ready, return
		}
	}

	private void updatePlayerStatus(List<Group> groupList) {
		for (Group g : groupList) {
			if (g instanceof PlayerGroup) {
				String output = "status:" + showPlayerStatus(g);
				output = EnDecoder.encodeChangeLine(output);
				EnDecoder.sendUTF8Packet(((PlayerGroup) g).getOutToClient(),
						output);
			}
		}
	}

	private String showPlayerStatus(Group g) {
		StringBuilder buffer = new StringBuilder();

		int count = 1;
		synchronized (timeMap) {
			for (CharList cList : g.list) {
				for (ICharacter c : cList.charList) {
					double percentage = (double) timeMap.get(c).getCurrent()
							/ (double) timeMap.get(c).getMax() * 100.0;
					buffer.append(String.format("%d%%\tNo. %d %s\n",
							(int) percentage, count, c.showStatus()));
					count++;
				}
			}
		}
		return buffer.toString();
	}
	
	public void updatePlayerStatus(PlayerGroup g){
		String output = "status:" + showPlayerStatus(g);
		output = EnDecoder.encodeChangeLine(output);
		EnDecoder.sendUTF8Packet(g.getOutToClient(),
				output);
	}

	private void addTimeMap(Group newGroup) {
		for (CharList cList : newGroup.list) {
			for (ICharacter c : cList.charList) {
				timeMap.put(c, new IntPair(0, c.getStatus(status.SPEED))); // temp assign = 3000
			}
		}
	}

	public GroupList getEnemyGroups(ICharacter c) {
		if (team1List.gList.contains(c.getMyGroup()))
			return team2List;
		else
			return team1List;
	}

	public GroupList getEnemyGroups(Group g) {
		if (team1List.gList.contains(g))
			return team2List;
		else
			return team1List;
	}

	public GroupList getMyGroups(ICharacter c) {
		if (team1List.gList.contains(c.getMyGroup()))
			return team1List;
		else
			return team2List;
	}

	private ArrayList<ICharacter> updateTime() {
		// update the battle time per 100ms
		ArrayList<ICharacter> readyList = new ArrayList<ICharacter>();
		int max, current;

		synchronized (timeMap) {
			for (Entry<ICharacter, IntPair> entry : timeMap.entrySet()) {
				if (entry.getKey().isDown())
					continue;
				current = entry.getValue().getCurrent();
				max = entry.getValue().getMax();
				if (current >= max)
					readyList.add(entry.getKey());
				else if (current + 100 >= max) {
					readyList.add(entry.getKey());
					entry.getValue().setCurrent(current + 100);
				} else
					entry.getValue().setCurrent(current + 100);
				/*
				 * if (current >= max) readyList.add(entry.getKey()); else
				 * entry.getValue().setCurrent(current + 100);
				 */
			}
			if (readyList.size() == 0)
				return null;
			else
				return readyList;
		}
	}

	private void updatePlayerStatus() {
		updateCounter++;
		if (updateCounter == 30) {
			updateCounter = 0;

			for (Group g : team1List.gList)
				g.updateTime();
			for (Group g : team2List.gList)
				g.updateTime();
		}
	}

	public void resetBattleTime(ICharacter c) {
		synchronized (timeMap) {
			IntPair target = timeMap.get(c);
			int current, max;
			current = target.getCurrent();
			max = target.getMax();
			target.setCurrent(max - current);
		}
	}

	public ConcurrentHashMap<ICharacter, IntPair> getTimeMap() {
		return this.timeMap;
	}

	public boolean checkBattleEnd() {
		boolean over = false;
		/*
		 * if (team1List.gList.isEmpty()) over = true; if
		 * (team2List.gList.isEmpty()) over = true;
		 */

		if (checkGroupListDown(team1List) || checkGroupListDown(team2List))
			over = true;

		if (over == true) {
			// free the battle resources
			battleTimer.cancel();
			for (Group g : team1List.gList) {
				g.setInBattle(false);
				g.setBattleTask(null);
				if (g instanceof PlayerGroup)
					CommandServer.informGroup(g,
							"status:" + ((PlayerGroup) g).showGroupStatus());
			}
			for (Group g : team2List.gList) {
				g.setInBattle(false);
				g.setBattleTask(null);
				if (g instanceof PlayerGroup)
					CommandServer.informGroup(g,
							"status:" + ((PlayerGroup) g).showGroupStatus());
			}
			return true;
		}
		return false;
	}

	private boolean checkGroupListDown(GroupList list) {
		boolean allDown = true;
		boolean groupDown;
		boolean isDown = false;
		while (!isDown) {
			isDown = true;
			for (Group g : list.gList) {
				groupDown = true;
				for (CharList cList : g.list) {
					for (ICharacter c : cList.charList) {
						if (!c.isDown()) {
							allDown = false;
							groupDown = false;
						}
					}
				}
				if (groupDown) {
					if (g instanceof PlayerGroup) {
						// TODO: implement player group dead action
						CommandServer.informGroup(g, "你的隊伍全滅了...\n");
					} else {
						ItemUtil.createLootingItem(g);
						// group inventory drop to the ground
						ItemUtil.dropAllItemOnDefeat(g);
					}
					
					// a group is down, remove data from this battle
					removeGroupFromTimeMap(g);
					g.getAtRoom().getGroupList().gList.remove(g);
					g.setAtRoom(null);
					g.setInBattle(false);
					g.setBattleTask(null);
					list.gList.remove(g);
					isDown = false;
					break;
				}
			}
		}
		return allDown;
	}

	private void removeGroupFromTimeMap(Group g) {
		for (CharList cList : g.list) {
			for (ICharacter c : cList.charList) {
				this.timeMap.remove(c);
			}
		}
	}
}
