package test.module.map;

import module.map.BaseRoom;
import module.map.Neighbor;
import module.map.api.IRoom;
import module.map.constants.CExit.exit;

import org.junit.Test;

public class RoomTest {
	
	@Test
	public void roomTest(){
		IRoom r1 = new BaseRoom();
		r1.setTitle("Test room");
		r1.setDescription("1\n234\n56789");
		
		IRoom r2 = new BaseRoom();
		r2.setTitle("Test room2");
		r2.setDescription("1\n1\n1");
		
		r1.setSingleExit(exit.EAST, new Neighbor(r2));
		r2.setSingleExit(exit.WEST, new Neighbor(r1));
		
		System.out.println(r1.displayRoom());
		System.out.println(r2.displayRoom());
	}
}
