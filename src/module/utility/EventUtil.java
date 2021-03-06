package module.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.event.api.IEvent;
import module.event.api.IRoomCommand;
import module.event.map.SkipEventException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EventUtil {
	public static HashMap<String, IEvent> mapEventMap = new HashMap<String, IEvent>();
	public static HashMap<String, IRoomCommand> mapCommandMap = new HashMap<String, IRoomCommand>();
	public static HashMap<String, JSONArray> eventMessageMap = new HashMap<String, JSONArray>();
	private static JSONParser parser = new JSONParser();
	
	public static boolean triggerRoomEvent(ICharacter c){
		try {
			if (mapEventMap.get(c.getAtRoom().getPosition().toString()).isTriggered(c))
				return true;
		} catch (Exception e){
			// do nothing
		}
		return false;
	}
	
	public static void doRoomEvent(ICharacter g){
		mapEventMap.get(g.getAtRoom().getPosition().toString()).doEvent(g);
	}
	
	public static void checkSkipEvent(String msg) throws SkipEventException{
		if (msg.equals("q")) throw new SkipEventException();
		return;
	}
	
	public static void informCheckReset(ICharacter g, StringBuffer buf, BufferedReader in) throws SkipEventException{
		g.getAtRoom().informRoom(buf.toString() + "<ENTER>\n");
		checkSkipEvent(IOUtil.readLineFromClientSocket(in));
		buf.setLength(0);
	}
	
	public static void informReset(ICharacter g, StringBuffer buf, BufferedReader in){
		g.getAtRoom().informRoom(buf.toString() + "<ENTER>\n");
		IOUtil.readLineFromClientSocket(in);
		buf.setLength(0);
	}
	
	public static boolean doRoomCommand(ICharacter c, String[] msg){
		IRoomCommand r = mapCommandMap.get(c.getAtRoom().getPosition().toString());
		if (r != null) return r.roomSpecialCommand(c, msg);
		return false;
	}
	
	public static void parseEventFromJSON(String filename){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			JSONObject oooo = (JSONObject) parser.parse(isr);
			JSONArray eventArray = (JSONArray) oooo.get("event");
			
			for (Object obj : eventArray){
				JSONObject eventObj = (JSONObject) obj;
				String eventName = (String) eventObj.get("name");
				JSONArray descArray = (JSONArray) eventObj.get("description");
				eventMessageMap.put(eventName, descArray);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void executeEventMessage(PlayerCharacter pg, String eventName){
		BufferedReader in = pg.getInFromClient();
		StringBuffer buf = new StringBuffer();
		
		JSONArray descArray = eventMessageMap.get(eventName);
		for (Object obj : descArray){
			JSONArray oneTimeDesc = (JSONArray) obj;
			int i = 0;
			buf.append((String) oneTimeDesc.get(0));
			for (i = 1; i < oneTimeDesc.size(); i++)
				buf.append("\n" + (String) oneTimeDesc.get(i));
			try {
				informCheckReset(pg, buf, in);
			} catch (SkipEventException e) {
				CommandServer.informCharacter(pg, "���L�@���C\n");
				return;
			}
		}
	}
	
	public static void showMessageToClient(DataOutputStream toClient, String eventName){
		JSONArray descArray = eventMessageMap.get(eventName);
		
		StringBuffer buf = new StringBuffer();
		for (Object obj : descArray) {
			JSONArray oneTimeDesc = (JSONArray) obj;
			for (int i = 0; i < oneTimeDesc.size(); i++)
			buf.append(((String) oneTimeDesc.get(i)) + "\n");
		}
		EnDecoder.sendUTF8Packet(toClient, buf.toString());
	}
}
