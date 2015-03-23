package module.map.constants;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import module.map.Neighbor;
import module.map.api.IDoor;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorStatus;

public class CExit {
	public static enum exit {
		EAST("東"),
		WEST("西"),
		SOUTH("南"),
		NORTH("北"),
		UP("上"),
		DOWN("下");
		
		public String chineseName;
		exit (String chineseName){
			this.chineseName = chineseName;
		}
	}
	
	public static String displayRoomExits(IRoom r){
		StringBuffer buffer = new StringBuffer();
		buffer.append("[出口:");
		ConcurrentHashMap<CExit.exit, Neighbor> exitMap = r.getExits();
		
		int count = 0;
		for (Entry<exit, Neighbor> entry : exitMap.entrySet()){
			IDoor targetDoor = entry.getValue().getDoor();
			if (targetDoor == null || (targetDoor.getDoorStatus() != doorStatus.CLOSED
					&& targetDoor.getDoorStatus() != doorStatus.LOCKED))
				count++;
		}
		
		if (count == 0)
			buffer.append(" 無 ]\n");
		else {
			buffer.append(displaySingleExit(exitMap, exit.EAST));
			buffer.append(displaySingleExit(exitMap, exit.WEST));
			buffer.append(displaySingleExit(exitMap, exit.SOUTH));
			buffer.append(displaySingleExit(exitMap, exit.NORTH));
			buffer.append(displaySingleExit(exitMap, exit.UP));
			buffer.append(displaySingleExit(exitMap, exit.DOWN));
			buffer.append(" ]\n");
		}
		return buffer.toString();
	}
	
	public static String[] getAccessibleExitsRoom(IRoom r){
		ConcurrentHashMap<CExit.exit, Neighbor> exitMap = r.getExits();
		ConcurrentHashMap<exit, Neighbor> accessibleExitMap = new ConcurrentHashMap<exit, Neighbor>();
		for (Entry<exit, Neighbor> entry : exitMap.entrySet()){
			IDoor door = entry.getValue().getDoor();
			if (door == null || (door.getDoorStatus() != doorStatus.CLOSED
					&& door.getDoorStatus() != doorStatus.LOCKED))
				accessibleExitMap.put(entry.getKey(), entry.getValue());
		}
		String[] result = new String[accessibleExitMap.size()];
		
		if (accessibleExitMap.isEmpty()) return null;
		else {
			int index = 0;
			
			if (accessibleExitMap.containsKey(exit.EAST)){
				result[index] = "e";
				index++;
			}
			if (accessibleExitMap.containsKey(exit.WEST)){
				result[index] = "w";
				index++;
			}
			if (accessibleExitMap.containsKey(exit.SOUTH)){
				result[index] = "s";
				index++;
			}
			if (accessibleExitMap.containsKey(exit.NORTH)){
				result[index] = "n";
				index++;
			}
			if (accessibleExitMap.containsKey(exit.UP)){
				result[index] = "u";
				index++;
			}
			if (accessibleExitMap.containsKey(exit.DOWN)){
				result[index] = "d";
				index++;
			}
		}
		return result;
	}
	
	private static String displaySingleExit(ConcurrentHashMap<CExit.exit, Neighbor> map, exit way){
		if (map.get(way) != null){
			Neighbor nei = map.get(way);
			if (nei.getDoor() == null || nei.getDoor().getDoorStatus() == doorStatus.OPENED)
				return " " + way.chineseName;
		}
		return "";
	}
}
