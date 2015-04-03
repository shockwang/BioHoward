package module.event.map.instance.chapter0;

import module.character.Group;
import module.event.api.IRoomCommand;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;

public class YiDormitoryRoomCommand {
	public static void initialize(){
		EventUtil.mapCommandMap.put("101,92,1", new IRoomCommand(){
			
			@Override
			public boolean roomSpecialCommand(Group g, String[] msg) {
				if (msg.length == 0 || !msg[0].equals("crash")) return false;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
				if (mm.getState() == MainMission.State.BEFORE_BREAK_MANAGE_DOOR){
					if (g.getInventory().findItem("fire") != null){
						g.getAtRoom().informRoom(g.list.get(0).charList.get(0).getChiName()
								+ "舉起滅火器，用力地往玻璃門砸下去! 匡噹!\n只見門上的玻璃碎了一地。\n");
						mm.setState(MainMission.State.AFTER_BREAK_MANAGE_DOOR);
					} else {
						g.getAtRoom().informRoom(g.list.get(0).charList.get(0).getChiName()
								+ "身上並沒有帶著適當的工具。\n");
					}
					return true;
				} else if (((MainMission.State) mm.getState()).index 
						>= MainMission.State.AFTER_BREAK_MANAGE_DOOR.index){
					g.getAtRoom().informRoom("門上的玻璃已經被打碎了。\n");
					return true;
				}
				return false;
			}
		});
	}
}
