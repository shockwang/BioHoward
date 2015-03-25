package module.utility;

import java.util.HashMap;

import module.character.Group;
import module.event.api.IEvent;
import module.event.map.SkipEventException;

public class EventUtil {
	public static HashMap<String, IEvent> mapEventMap = new HashMap<String, IEvent>();
	
	public static boolean triggerRoomEvent(Group g){
		try {
			if (mapEventMap.get(g.getAtRoom().getPosition().toString()).isTriggered(g))
				return true;
		} catch (Exception e){
			// do nothing
		}
		return false;
	}
	
	public static void doRoomEvent(Group g){
		mapEventMap.get(g.getAtRoom().getPosition().toString()).doEvent(g);
	}
	
	public static void checkSkipEvent(String msg) throws SkipEventException{
		if (msg.equals("q")) throw new SkipEventException();
		return;
	}
}
