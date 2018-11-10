package test.module.map;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentHashMap;

import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.character.instance.main.Dawdw;
import module.character.instance.main.Enf;
import module.client.ClientUser;
import module.item.BaseEquipment;
import module.item.api.IEquipment.EquipType;
import module.item.instance.chapter0.BlueShirt;
import module.item.instance.chapter0.NikeShoes;
import module.item.instance.chapter0.Watch;
import module.map.BaseDoor;
import module.map.BaseRoom;
import module.map.Neighbor;
import module.map.Position;
import module.map.PositionDoor;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorAttribute;
import module.map.constants.CExit.exit;
import module.server.PlayerServer;
import module.utility.MapUtil;

import org.junit.Test;

public class MapTest {
	private IRoom start = null;
	
	
	public IRoom getStart() {return start;}
	
	public void initialize() {
		// build a 3*3*1 blocks map
		start = createRoom(new Position(0, 0, 0), "start", "0, 0, 0");
		IRoom n = createRoom(new Position(0, 1, 0), "n", "0, 1, 0");
		IRoom s = createRoom(new Position(0, -1, 0), "s", "0, -1, 0");
		IRoom e = createRoom(new Position(1, 0, 0), "e", "1, 0, 0");
		IRoom w = createRoom(new Position(-1, 0, 0), "w", "-1, 0, 0");
		IRoom ne = createRoom(new Position(1, 1, 0), "ne", "1, 1, 0");
		IRoom nw = createRoom(new Position(-1, 1, 0), "nw", "-1, 1, 0");
		IRoom se = createRoom(new Position(1, -1, 0), "ne", "1, -1, 0");
		IRoom sw = createRoom(new Position(-1, -1, 0), "ne", "-1, -1, 0");
		
		// update start neighbor
		ConcurrentHashMap<exit, Neighbor> map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.NORTH, new Neighbor(n));
		map.put(exit.SOUTH, new Neighbor(s));
		map.put(exit.EAST, new Neighbor(e));
		map.put(exit.WEST, new Neighbor(w));
		start.setExits(map);
		
		// update e neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.NORTH, new Neighbor(ne));
		map.put(exit.SOUTH, new Neighbor(se));
		map.put(exit.WEST, new Neighbor(start));
		e.setExits(map);
		
		// update w neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.EAST, new Neighbor(start));
		map.put(exit.NORTH, new Neighbor(nw));
		map.put(exit.SOUTH, new Neighbor(sw));
		w.setExits(map);
		
		// update s neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.NORTH, new Neighbor(start));
		map.put(exit.EAST, new Neighbor(se));
		map.put(exit.WEST, new Neighbor(sw));
		s.setExits(map);
		
		// update n neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.SOUTH, new Neighbor(start));
		map.put(exit.EAST, new Neighbor(ne));
		map.put(exit.WEST, new Neighbor(nw));
		n.setExits(map);
		
		// update se neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.NORTH, new Neighbor(e));
		map.put(exit.WEST, new Neighbor(s));
		se.setExits(map);
		
		// update sw neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.EAST, new Neighbor(s));
		map.put(exit.NORTH, new Neighbor(w));
		sw.setExits(map);
		
		// update ne neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.SOUTH, new Neighbor(e));
		map.put(exit.WEST, new Neighbor(n));
		ne.setExits(map);
		
		// update nw neighbor
		map = new ConcurrentHashMap<exit, Neighbor>();
		map.put(exit.SOUTH, new Neighbor(w));
		map.put(exit.EAST, new Neighbor(n));
		nw.setExits(map);
		
		// door test
		PositionDoor pd = new PositionDoor(
				s.getPosition(), exit.SOUTH, start.getPosition(), exit.NORTH);
		BaseDoor testDoor = new BaseDoor("就是扇門", pd);
		start.getExits().get(exit.SOUTH).setDoor(testDoor);
		s.getExits().get(exit.NORTH).setDoor(testDoor);
		
		// lockable door test
		PositionDoor pd2 = new PositionDoor(
				start.getPosition(), exit.SOUTH, n.getPosition(), exit.NORTH);
		BaseDoor testDoor2 = new BaseDoor("就是扇可以鎖的門", pd2);
		testDoor2.setDoorAttribute(doorAttribute.LOCKABLE);
		testDoor2.setKeyName("opg");
		start.getExits().get(exit.NORTH).setDoor(testDoor2);
		n.getExits().get(exit.SOUTH).setDoor(testDoor2);
	}
	
	private IRoom createRoom(Position pos, String title, String description){
		IRoom result = new BaseRoom();
		result.setPosition(pos);
		result.setTitle(title);
		result.setDescription(description);
		return result;
	}
	
	@Test
	public void jsonParsingTest(){
		MapUtil.parseMapFromJSON("jsonTest.txt");
	}
	
	@Test
	public void humanTest() {
		MapTest mt = new MapTest();
		mt.initialize();
		
		PlayerServer singletonServer = new PlayerServer();
		singletonServer.setPort(12312);
		singletonServer.start();

		ClientUser oneUser = new ClientUser();
		assertTrue(oneUser.connectToServer("localhost", 12312));
		oneUser.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		PlayerCharacter pc = new PlayerCharacter("霍華", "enf");
		pc.setDesc("就是霍華。");
		pc.setStatus(status.CONSTITUTION, 6);
		pc.setStatus(status.STRENGTH, 29);
		pc.setStatus(status.SPEED, 3500);
		pc.addAttribute(attribute.HP, 99);
		
		// add equipment
		pc.getInventory().addItem(new BlueShirt());
		pc.getInventory().addItem(new NikeShoes());
		pc.getInventory().addItem(new Watch());
		pc.setInitialRoom(mt.getStart());
		pc.setAtRoom(mt.getStart());
		mt.getStart().getCharList().addChar(pc);
		
		// add npc
		ICharacter dawdw = new Dawdw();
		dawdw.setInitialRoom(mt.getStart());
		dawdw.setAtRoom(mt.getStart());
		mt.getStart().getCharList().addChar(dawdw);

		PlayerServer.pList.get(0).setPlayer(pc);
		pc.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		pc.setInFromClient(PlayerServer.pList.get(0).getInFromClient());
		
		// set player group to system time
		PlayerServer.getSystemTime().addCharacter(pc);
		
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
