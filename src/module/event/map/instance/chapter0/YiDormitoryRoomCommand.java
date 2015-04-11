package module.event.map.instance.chapter0;

import module.character.Group;
import module.command.CommandServer;
import module.event.api.IRoomCommand;
import module.map.api.IDoor;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class YiDormitoryRoomCommand {
	public static void initialize() {
		EventUtil.mapCommandMap.put("101,92,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length == 0)
					return false;

				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (msg[0].equals("crash")) {
					if (mm.getState() == MainMission.State.BEFORE_BREAK_MANAGE_DOOR) {
						if (g.getInventory().findItem("fire") != null) {
							g.getAtRoom()
									.informRoom(
											g.list.get(0).charList.get(0)
													.getChiName()
													+ "舉起滅火器，用力地往玻璃門砸下去! 匡噹!\n只見門上的玻璃碎了一地。\n");
							mm.setState(MainMission.State.AFTER_BREAK_MANAGE_DOOR);
						} else {
							g.getAtRoom().informRoom(
									g.list.get(0).charList.get(0).getChiName()
											+ "身上並沒有帶著適當的工具。\n");
						}
					} else if (((MainMission.State) mm.getState()).index >= MainMission.State.AFTER_BREAK_MANAGE_DOOR.index) {
						g.getAtRoom().informRoom("門上的玻璃已經被打碎了。\n");
					} else {
						g.getAtRoom().informRoom("先去宿舍大門看看吧。\n");
					}
					return true;
				} else if (msg[0].equals("climb")) {
					synchronized (g.getAtRoom()) {
						MainMission.State state = (MainMission.State) mm
								.getState();
						if (state.index < MainMission.State.AFTER_BREAK_MANAGE_DOOR.index) {
							g.getAtRoom().informRoom("門上的玻璃還沒破呢，你想從哪爬進去?\n");
						} else {
							if (!g.getInBattle()) {
								g.getAtRoom().informRoom(
										String.format(
												"%s跨過門上破碎玻璃窗的空隙，鑽進了宿舍管理中心。\n",
												g.list.get(0).charList.get(0)
														.getChiName()));
								IRoom south = g.getAtRoom().getExits()
										.get(exit.SOUTH).getRoom();
								south.getGroupList().gList.add(g);
								g.getAtRoom().getGroupList().gList.remove(g);
								g.setAtRoom(south);
								CommandServer.informGroup(g, 
										south.displayRoomExceptGroup(g));
							} else
								CommandServer.informGroup(g, "隊伍正在戰鬥中喔!\n");
						}
						return true;
					}
				}
				return false;
			}
		});
		
		EventUtil.mapCommandMap.put("101,91,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length == 0) return false;
				
				if (msg[0].equals("climb")){
					synchronized (g.getAtRoom()){
						if (!g.getInBattle()) {
							g.getAtRoom().informRoom(
									String.format(
											"%s跨過門上破碎玻璃窗的空隙，鑽出了宿舍管理中心。\n",
											g.list.get(0).charList.get(0)
													.getChiName()));
							IRoom north = g.getAtRoom().getExits()
									.get(exit.NORTH).getRoom();
							north.getGroupList().gList.add(g);
							g.getAtRoom().getGroupList().gList.remove(g);
							g.setAtRoom(north);
							CommandServer.informGroup(g, 
									north.displayRoomExceptGroup(g));
						} else
							CommandServer.informGroup(g, "隊伍正在戰鬥中喔!\n");
					}
					return true;
				}
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,99,2", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開西邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
		});
		
		EventUtil.mapCommandMap.put("101,94,2", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開西邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
		});
		
		EventUtil.mapCommandMap.put("101,95,2", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開東邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
		});
		
		EventUtil.mapCommandMap.put("101,99,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開西邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,96,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開東邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,95,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "嘗試推開東邊的門，但卻推不動。推測是門的另外一邊\n";
							out += "被什麼沉重的東西擋住了。\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
	}
}
