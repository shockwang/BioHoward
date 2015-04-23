package test.module.event;

import static org.junit.Assert.assertTrue;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.instance.chapter0.DingDing;
import module.character.instance.main.Enf;
import module.client.ClientUser;
import module.command.CommandServer;
import module.event.map.instance.chapter0.YiDormitoryEvent;
import module.event.map.instance.chapter0.YiDormitoryRoomCommand;
import module.item.BaseEquipment;
import module.item.instance.chapter0.HydraulicCut;
import module.item.instance.chapter0.Key306;
import module.item.instance.chapter0.PhysicsBook;
import module.map.api.IRoom;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.MapUtil;

import org.junit.Before;
import org.junit.Test;

public class EventTest {
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	private PlayerGroup pg = null;
	
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
		
		pg = new PlayerGroup(new Enf());
		
		PlayerServer.pList.get(0).setGroup(pg);
		pg.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		pg.setInFromClient(PlayerServer.pList.get(0).getInFromClient());
		
		// map initialize
		MapUtil.parseMapFromJSON("resources/map/chapter0/YiDormitory.map");
		MapUtil.parseDoorFromJSON("resources/map/chapter0/YiDormitory.door");
		YiDormitoryEvent.initialize();
		YiDormitoryRoomCommand.initialize();
		
		// npc initialize
		MapUtil.parseNpcFromJSON("resources/map/chapter0/YiDormitory.npc");
		
		// item initialize
		MapUtil.parseItemFromJSON("resources/map/chapter0/YiDormitory.item");
		
		// event message initialize
		EventUtil.parseEventFromJSON("resources/event/chapter0/YiDormitory.event");
	}
	
	@Test
	public void startSearchingTest(){
		BaseEquipment testEquip = new PhysicsBook();
		pg.getInventory().addItem(testEquip);
		pg.getInventory().addItem(new Key306());
		pg.getInventory().addItem(new HydraulicCut());
		
		// set player group mission
		MainMission mm = new MainMission();
		mm.setState(MainMission.State.AFTER_EXIT_DORMITORY);
		PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
		
		// set player group start position
		IRoom start = MapUtil.roomMap.get("104,109,1");
		pg.setAtRoom(start);
		pg.setInitialRoom(start);
		start.getGroupList().gList.add(pg);
		
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
