package module.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import module.character.Group;
import module.character.api.ICharacter;
import module.item.api.IItem;
import module.map.BaseDoor;
import module.map.BaseRoom;
import module.map.Neighbor;
import module.map.Position;
import module.map.PositionDoor;
import module.map.api.IDoor;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute;
import module.map.constants.CExit.exit;
import module.server.PlayerServer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapUtil {
	public static HashMap<String, IRoom> roomMap = new HashMap<String, IRoom>();
	public static HashMap<IRoom, HashMap<exit, Position>> linkMap = new HashMap<IRoom, HashMap<exit, Position>>();
	public static JSONParser parser = new JSONParser();
	
	public static void parseMapFromJSON(String filename){
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filename));
			JSONArray roomArray = (JSONArray) jsonObj.get("room");
			JSONObject jsb = null;
			IRoom roomToCreate = null;
			for (Object obj : roomArray){
				jsb = (JSONObject) obj;
				String name = (String) jsb.get("name");
				
				// get description
				JSONArray desArray = (JSONArray) jsb.get("description");
				StringBuffer desBuf = new StringBuffer();
				desBuf.append((String) desArray.get(0));
				for (int i = 1; i < desArray.size(); i++)
					desBuf.append("\n" + (String) desArray.get(i));
					
				Position pos = getPositionFromString((String) jsb.get("position"));
				roomToCreate = new BaseRoom();
				roomToCreate.setTitle(name);
				roomToCreate.setDescription(desBuf.toString());
				roomToCreate.setPosition(pos);
				JSONArray exitArray = (JSONArray) jsb.get("exit");
				for (Object direction : exitArray){
					JSONObject dirObj = (JSONObject) direction;
					exit way = MoveUtil.getWay((String) dirObj.get("direction"));
					Position connectTo = getPositionFromString(
							(String) dirObj.get("connectTo"));
					IRoom targetRoom = roomMap.get(connectTo.toString());
					if (targetRoom != null) {
						roomToCreate.setSingleExit(way, new Neighbor(targetRoom));
					}
					else {
						// add link into linkMap
						if (linkMap.containsKey(roomToCreate))
							linkMap.get(roomToCreate).put(way, connectTo);
						else {
							HashMap<exit, Position> tempMap = new HashMap<exit, Position>();
							tempMap.put(way, connectTo);
							linkMap.put(roomToCreate, tempMap);
						}
					}
				}
				roomMap.put(pos.toString(), roomToCreate);
			}
			
			// update room link
			for (Entry<IRoom, HashMap<exit, Position>> entry : linkMap.entrySet()){
				for (Entry<exit, Position> exitEntry : entry.getValue().entrySet()){
					IRoom targetRoom = roomMap.get(exitEntry.getValue().toString());
					entry.getKey().setSingleExit(exitEntry.getKey(), new Neighbor(targetRoom));
				}
			}
			
			// clear resources
			//roomList.clear();
			linkMap.clear();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("error happened when parsing room file.");
			e.printStackTrace();
		}
		
	}
	
	public static void parseDoorFromJSON(String filename){
		try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader(filename));
			JSONArray doorList = (JSONArray) obj.get("door");
			for (Object ooo : doorList){
				JSONObject doorObj = (JSONObject) ooo;
				
				// get description
				StringBuffer buf = new StringBuffer();
				JSONArray desArray = (JSONArray) doorObj.get("description");
				buf.append((String) desArray.get(0));
				for (int i = 1; i < desArray.size(); i++)
					buf.append("\n" + (String) desArray.get(i));
				
				// get PositionDoor
				JSONObject posObj = (JSONObject) doorObj.get("positionDoor");
				JSONObject connect1Obj = (JSONObject) posObj.get("connect1");
				exit way1 = MoveUtil.getWay((String) connect1Obj.get("direction"));
				Position pos1 = getPositionFromString((String) connect1Obj.get("connectTo"));
				JSONObject connect2Obj = (JSONObject) posObj.get("connect2");
				exit way2 = MoveUtil.getWay((String) connect2Obj.get("direction"));
				Position pos2 = getPositionFromString((String) connect2Obj.get("connectTo"));
				
				IDoor doorToConfig = new BaseDoor(buf.toString(), 
						new PositionDoor(pos1, way1, pos2, way2));
				
				// get other door configs
				String status = (String) doorObj.get("doorStatus");
				if (status != null) doorToConfig.setDoorStatus(
						CDoorAttribute.parseDoorStatusFromString(status));
				
				String attribute = (String) doorObj.get("doorAttribute");
				if (attribute != null){
					doorToConfig.setDoorAttribute(
							CDoorAttribute.parseDoorAttributeFromString(attribute));
					String keyName = (String) doorObj.get("keyName");
					doorToConfig.setKeyName(keyName);
				}
				
				// connect door with related rooms
				IRoom r1 = roomMap.get(pos1.toString());
				exit r1RelatedDirection = MoveUtil.getOppositeWay(way1);
				r1.getExits().get(r1RelatedDirection).setDoor(doorToConfig);
				
				IRoom r2 = roomMap.get(pos2.toString());
				exit r2RelatedDirection = MoveUtil.getOppositeWay(way2);
				r2.getExits().get(r2RelatedDirection).setDoor(doorToConfig);
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
	
	public static void parseNpcFromJSON(String filename){
		try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader(filename));
			JSONArray groupList = (JSONArray) obj.get("npc");
			for (Object ooo : groupList){
				JSONObject groupObj = (JSONObject) ooo;
				
				boolean newGroup = true;
				Group groupToConfig = null;
				JSONArray charList = (JSONArray) groupObj.get("character");
				for (Object oos : charList){
					Class<?> c = Class.forName((String) oos);
					ICharacter charToAdd = (ICharacter) c.newInstance();
					if (newGroup){
						groupToConfig = new Group(charToAdd);
						newGroup = false;
					} else
						groupToConfig.addChar(charToAdd);
				}
				
				// config initial position
				String position = (String) groupObj.get("position");
				IRoom location = roomMap.get(position);
				initializeGroupAtMap(groupToConfig, location);
				
				// check if respawn
				if (groupObj.get("respawn") != null){
					boolean isRespawn = (Boolean) groupObj.get("respawn");
					groupToConfig.setIsRespawn(isRespawn);
				}
				
				//check if inventory exists
				JSONArray inventoryList = (JSONArray) groupObj.get("inventory");
				if (inventoryList != null){
					for (Object ovo : inventoryList){
						Class<?> zz = Class.forName((String) ovo);
						IItem itemToAdd = (IItem) zz.newInstance();
						groupToConfig.getInventory().addItem(itemToAdd);
					}
				}
				
				// add group to SystemTime
				PlayerServer.getSystemTime().addGroup(groupToConfig);
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
		} catch (ClassNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseItemFromJSON(String filename){
		try {
			JSONObject obj = (JSONObject) parser.parse(new FileReader(filename));
			JSONArray itemList = (JSONArray) obj.get("item");
			
			for (Object ooo : itemList){
				JSONObject itemObj = (JSONObject) ooo;
				Class<?> zz = Class.forName((String) itemObj.get("name"));
				IItem itemToAdd = (IItem) zz.newInstance();
				
				// set item location
				IRoom roomToSet = roomMap.get((String) itemObj.get("position"));
				roomToSet.getItemList().addItem(itemToAdd);
				itemToAdd.setAtRoom(roomToSet);
				
				// not set time-out for first time item on the ground
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Position getPositionFromString(String input){
		int x, y, z;
		String[] temp = input.split(",");
		x = Integer.parseInt(temp[0]);
		y = Integer.parseInt(temp[1]);
		z = Integer.parseInt(temp[2]);
		return new Position(x, y, z);
	}
	
	public static void initializeGroupAtMap(Group g, IRoom r){
		r.getGroupList().gList.add(g);
		g.setAtRoom(r);
		g.setInitialRoom(r);
		PlayerServer.getSystemTime().addGroup(g);
	}
}
