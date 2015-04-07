package test.module.event;

import static org.junit.Assert.assertTrue;
import module.character.CharList;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CStatus.status;
import module.client.ClientUser;
import module.command.CommandServer;
import module.event.map.instance.chapter0.YiDormitoryEvent;
import module.event.map.instance.chapter0.YiDormitoryRoomCommand;
import module.item.BaseEquipment;
import module.item.api.IEquipment.EquipType;
import module.item.api.IItem;
import module.item.container.instance.chapter0.Refrigerator;
import module.item.instance.chapter0.FireExtinguisher;
import module.map.api.IRoom;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.MapUtil;

import org.junit.Before;
import org.junit.Test;

import test.module.battle.BattleTaskTest;

public class EventTest {
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	private PlayerGroup pg = null;
	private BattleTaskTest btt;
	
	@Before
	public void initialize(){
		singletonServer = new PlayerServer();
		singletonServer.setPort(12312);
		singletonServer.start();
		
		oneUser = new ClientUser();
		assertTrue(oneUser.connectToServer("localhost", 12312));
		oneUser.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		btt = new BattleTaskTest();
		
		pg = new PlayerGroup(btt.new CharForTest("霍華", "enf"){
			@Override
			public boolean battleAction(GroupList gList){
				return false;
			}
		});
		for (CharList cList : pg.list){
			for (ICharacter c : cList.charList){
				c.setMyGroup(pg);
				c.setHostile(false);
			}
		}
		
		PlayerServer.pList.get(0).setGroup(pg);
		pg.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		pg.setInFromClient(PlayerServer.pList.get(0).getInFromClient());
		
		// map initialize
		MapUtil.parseMapFromJSON("map/chapter0/YiDormitory.map");
		MapUtil.parseDoorFromJSON("map/chapter0/YiDormitory.door");
		YiDormitoryEvent.initialize();
		YiDormitoryRoomCommand.initialize();
	}
	
	@Test
	public void startSearchingTest(){
		BaseEquipment testEquip = new BaseEquipment("普物課本", "physics book", EquipType.WEAPON);
		testEquip.setDescription("又厚又重。");
		testEquip.setPrice(100);
		testEquip.getStatus().put(status.WEAPON_ATTACK, 10);
		pg.getInventory().addItem(testEquip);
		pg.getInventory().addItem(btt.new ItemForTest("義齋306號房的鑰匙", "key 306", "就是鑰匙"));
		
		// set player group mission
		MainMission mm = new MainMission();
		mm.setState(MainMission.State.START_SEARCHING);
		PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
		
		// set player group start position
		IRoom start = MapUtil.roomMap.get("102,100,3");
		pg.setAtRoom(start);
		pg.setInitialRoom(start);
		start.getGroupList().gList.add(pg);
		
		// set container test
		IItem refrigerator = new Refrigerator(start);
		start.getItemList().addItem(refrigerator);
		
		// set a place to put fire extinguisher
		IRoom r1 = MapUtil.roomMap.get("101,99,1");
		r1.getItemList().addItem(new FireExtinguisher());
		
		// set player group to system time
		PlayerServer.getSystemTime().addGroup(pg);
		
		String[] msg = {"look"};
		CommandServer.readCommand(pg, msg);
		
		try {
			while (true) {
				Thread.sleep(1000000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
