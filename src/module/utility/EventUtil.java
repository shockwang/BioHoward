package module.utility;

import java.io.BufferedReader;
import java.util.HashMap;

import module.character.Group;
import module.event.api.IEvent;
import module.event.api.IRoomCommand;
import module.event.map.SkipEventException;

public class EventUtil {
	public static HashMap<String, IEvent> mapEventMap = new HashMap<String, IEvent>();
	public static HashMap<String, IRoomCommand> mapCommandMap = new HashMap<String, IRoomCommand>();
	
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
	
	public static void informCheckReset(Group g, StringBuffer buf, BufferedReader in) throws SkipEventException{
		g.getAtRoom().informRoom(buf.toString() + "<ENTER>\n");
		checkSkipEvent(IOUtil.readLineFromClientSocket(in));
		buf.setLength(0);
	}
	
	public static void informReset(Group g, StringBuffer buf, BufferedReader in){
		g.getAtRoom().informRoom(buf.toString() + "<ENTER>\n");
		IOUtil.readLineFromClientSocket(in);
		buf.setLength(0);
	}
	
	public static boolean doRoomCommand(Group g, String[] msg){
		IRoomCommand r = mapCommandMap.get(g.getAtRoom().getPosition().toString());
		if (r != null) return r.roomSpecialCommand(g, msg);
		return false;
	}
}
