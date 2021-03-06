package module.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import module.character.CharList;
import module.character.ICharacter;
import module.character.GroupList;
import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.character.api.IntPair;
import module.character.constants.CConfig.config;
import module.character.constants.CStatus.status;
import module.command.CommandServer;
import module.utility.EnDecoder;
import module.utility.ItemUtil;

public class BattleTask extends TimerTask {
	protected GroupList team1List;
	protected GroupList team2List;
	private ConcurrentHashMap<ICharacter, IntPair> timeMap;
	protected ArrayList<ICharacter> ready;
	protected Timer battleTimer;
	private int updateCounter = 0;
	protected boolean isBlocked = false;  // used if there's something need to pause the timer

	public BattleTask(ICharacter team1, ICharacter team2) {
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

	public void addBattleGroup(ICharacter addSide, ICharacter targetG) {
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
	
	public void addBattleOppositeGroup(ICharacter opposite, ICharacter targetG){
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
	
	public void removeBattleGroup(ICharacter g){
		GroupList side;
		if (team1List.gList.contains(g)) side = this.team1List;
		else side = this.team2List;
		
		if (g instanceof PlayerCharacter){
			for (ICharacter gg : this.getEnemyGroups(g).gList){
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
		if (isBlocked) return;
		
		synchronized (this) {
			ready = updateTime();
			updatePlayerStatus();
		}
		updatePlayerStatus(team1List.gList);
		updatePlayerStatus(team2List.gList);
		try {
			for (ICharacter c : ready) {
				if (c.getMyGroup() instanceof PlayerCharacter) {
					if (((PlayerCharacter) c.getMyGroup()).getConfigData().get(
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
				else c.battleAction(getEnemyGroups(c));
			}
		} catch (NullPointerException e) {
			return; // no one is ready, return
		}
	}

	protected void updatePlayerStatus(List<ICharacter> groupList) {
		for (ICharacter g : groupList) {
			if (g instanceof PlayerCharacter) {
				String output = "status:" + showPlayerStatus(g);
				output = EnDecoder.encodeChangeLine(output);
				EnDecoder.sendUTF8Packet(((PlayerCharacter) g).getOutToClient(),
						output);
			}
		}
	}

	private String showPlayerStatus(ICharacter g) {
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
	
	public void updatePlayerStatus(PlayerCharacter g){
		String output = "status:" + showPlayerStatus(g);
		output = EnDecoder.encodeChangeLine(output);
		EnDecoder.sendUTF8Packet(g.getOutToClient(),
				output);
	}

	private void addTimeMap(ICharacter newGroup) {
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

	public GroupList getEnemyGroups(ICharacter g) {
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

	protected ArrayList<ICharacter> updateTime() {
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

	protected void updatePlayerStatus() {
		updateCounter++;
		if (updateCounter == 20) {
			updateCounter = 0;

			for (ICharacter g : team1List.gList)
				g.updateTime();
			for (ICharacter g : team2List.gList)
				g.updateTime();
		}
	}

	public void resetBattleTime(ICharacter c) {
		synchronized (timeMap) {
			IntPair target = timeMap.get(c);
			target.setCurrent(0);
		}
	}

	public ConcurrentHashMap<ICharacter, IntPair> getTimeMap() {
		return this.timeMap;
	}

	public void checkBattleEnd() {
		boolean over = false;
		GroupList aliveGroups = null;
		
		if (checkGroupListDown(team1List)) {
			over = true;
			aliveGroups = team2List;
		} else if (checkGroupListDown(team2List)){
			over = true;
			aliveGroups = team1List;
		}

		if (over == true) {
			// inform room that battle is end
			aliveGroups.gList.get(0).getAtRoom().informRoom("戰鬥結束!\n");
			
			// free the battle resources
			battleTimer.cancel();
			for (ICharacter g : team1List.gList) {
				g.setInBattle(false);
				g.setBattleTask(null);
				if (g instanceof PlayerCharacter)
					CommandServer.informCharacter(g,
							"status:" + ((PlayerCharacter) g).showGroupStatus());
			}
			for (ICharacter g : team2List.gList) {
				g.setInBattle(false);
				g.setBattleTask(null);
				if (g instanceof PlayerCharacter)
					CommandServer.informCharacter(g,
							"status:" + ((PlayerCharacter) g).showGroupStatus());
			}
		}
	}

	protected boolean checkGroupListDown(GroupList list) {
		boolean allDown = true;
		boolean groupDown;
		boolean isDown = false;
		while (!isDown) {
			isDown = true;
			for (ICharacter g : list.gList) {
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
					if (g instanceof PlayerCharacter) {
						// TODO: implement player group dead action
						CommandServer.informCharacter(g, "你的隊伍全滅了...\n");
						removeBattleGroup(g);
						g.getAtRoom().getGroupList().gList.remove(g);
						CommandServer.informCharacter(g, "因為是教學任務，就直接傳送你回起始位置吧。\n");
						CommandServer.informCharacter(g, "可以慢慢熟悉怎麼樣活下來喔!\n");
						g.recoverGroup();
						g.setAtRoom(g.getInitialRoom());
						g.getAtRoom().getGroupList().gList.add(g);
						String out = "status:" + ((PlayerCharacter) g).showGroupStatus();
						CommandServer.informCharacter(g, out);
						return true;
					} else {
						ItemUtil.createLootingItem(g);
						// group inventory drop to the ground
						ItemUtil.dropAllItemOnDefeat(g);
						// do group down event if there's any
						GroupList oppositeGroups = getEnemyGroups(g);
						PlayerCharacter pg = null;
						for (ICharacter px : oppositeGroups.gList){
							if (px instanceof PlayerCharacter){
								pg = (PlayerCharacter) px;
								break;
							}
						}
						if (pg != null){
							this.isBlocked = true;
							g.list.get(0).list.get(0).doEventWhenGroupDown(pg);
							this.isBlocked = false;
						}
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

	protected void removeGroupFromTimeMap(ICharacter g) {
		for (CharList cList : g.list) {
			for (ICharacter c : cList.charList) {
				this.timeMap.remove(c);
			}
		}
	}
}
