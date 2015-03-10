package module.map.constants;

import java.util.concurrent.ConcurrentHashMap;

import module.map.Neighbor;
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
		if (exitMap.isEmpty())
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
	
	public static String[] getExitsRoom(IRoom r){
		ConcurrentHashMap<CExit.exit, Neighbor> exitMap = r.getExits();
		String[] result = new String[exitMap.size()];
		
		if (exitMap.isEmpty()) return null;
		else {
			int index = 0;
			
			if (exitMap.containsKey(exit.EAST)){
				result[index] = "e";
				index++;
			}
			if (exitMap.containsKey(exit.WEST)){
				result[index] = "w";
				index++;
			}
			if (exitMap.containsKey(exit.SOUTH)){
				result[index] = "s";
				index++;
			}
			if (exitMap.containsKey(exit.NORTH)){
				result[index] = "n";
				index++;
			}
			if (exitMap.containsKey(exit.UP)){
				result[index] = "u";
				index++;
			}
			if (exitMap.containsKey(exit.DOWN)){
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
