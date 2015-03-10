package test.module.map;

import java.util.concurrent.ConcurrentHashMap;

import module.map.BaseRoom;
import module.map.Neighbor;
import module.map.Position;
import module.map.api.IRoom;
import module.map.constants.CExit.exit;

public class MapTest {
	private IRoom start = null;
	
	
	public IRoom getStart(){return start;}
	
	public void initialize(){
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
	}
	
	private IRoom createRoom(Position pos, String title, String description){
		IRoom result = new BaseRoom();
		result.setPosition(pos.x, pos.y, pos.z);
		result.setTitle(title);
		result.setDescription(description);
		return result;
	}
}
