package module.map.instance.chapter0;

import java.util.ArrayList;

import module.character.Group;
import module.character.PlayerGroup;
import module.map.BaseRoom;
import module.map.api.IRoom;

public class YiDormitoryEventRoom {
	public static ArrayList<IRoom> roomList = new ArrayList<IRoom>();
	
	public static void initialize(){
		roomList.add(new YiDormitoryEventRoom.EventTestRoom());
	}
	
	private class EventTestRoom extends BaseRoom {
		@Override
		public boolean triggerRoomEvent(Group g){
			if (g instanceof PlayerGroup) return true;
			return false;
		}
	}
}
