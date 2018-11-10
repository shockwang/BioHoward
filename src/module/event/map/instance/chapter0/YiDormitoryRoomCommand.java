package module.event.map.instance.chapter0;

import module.character.ICharacter;
import module.character.PlayerCharacter;
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length == 0)
					return false;

				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (msg[0].equals("crash")) {
					if (mm.getState() == MainMission.State.BEFORE_BREAK_MANAGE_DOOR) {
						if (g.getInventory().findItem("fire") != null) {
							g.getAtRoom()
									.informRoom(
											g.list.get(0).list.get(0)
													.getChiName()
													+ "�|�_�������A�ΤO�a���������{�U�h! �J��!\n�u�����W�������H�F�@�a�C\n");
							mm.setState(MainMission.State.AFTER_BREAK_MANAGE_DOOR);
						} else {
							g.getAtRoom().informRoom(
									g.list.get(0).list.get(0).getChiName()
											+ "���W�èS���a�۾A���u��C\n");
						}
					} else if (((MainMission.State) mm.getState()).index >= MainMission.State.AFTER_BREAK_MANAGE_DOOR.index) {
						g.getAtRoom().informRoom("���W�������w�g�Q���H�F�C\n");
					} else {
						g.getAtRoom().informRoom("���h�J�٤j���ݬݧa�C\n");
					}
					return true;
				} else if (msg[0].equals("climb")) {
					synchronized (g.getAtRoom()) {
						MainMission.State state = (MainMission.State) mm
								.getState();
						if (state.index < MainMission.State.AFTER_BREAK_MANAGE_DOOR.index) {
							g.getAtRoom().informRoom("���W�������٨S�}�O�A�A�Q�q�����i�h?\n");
						} else {
							if (!g.getInBattle()) {
								g.getAtRoom().informRoom(
										String.format(
												"%s��L���W�}�H���������ŻءA�p�i�F�J�ٺ޲z���ߡC\n",
												g.list.get(0).list.get(0)
														.getChiName()));
								IRoom south = g.getAtRoom().getExits()
										.get(exit.SOUTH).getRoom();
								south.getGroupList().gList.add(g);
								g.getAtRoom().getGroupList().gList.remove(g);
								g.setAtRoom(south);
								CommandServer.informCharacter(g, 
										south.displayRoomExceptCharacter(g));
							} else
								CommandServer.informCharacter(g, "����b�԰�����!\n");
						}
						return true;
					}
				}
				return false;
			}
		});
		
		EventUtil.mapCommandMap.put("101,91,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length == 0) return false;
				
				if (msg[0].equals("climb")){
					synchronized (g.getAtRoom()){
						if (!g.getInBattle()) {
							g.getAtRoom().informRoom(
									String.format(
											"%s��L���W�}�H���������ŻءA�p�X�F�J�ٺ޲z���ߡC\n",
											g.list.get(0).list.get(0)
													.getChiName()));
							IRoom north = g.getAtRoom().getExits()
									.get(exit.NORTH).getRoom();
							north.getGroupList().gList.add(g);
							g.getAtRoom().getGroupList().gList.remove(g);
							g.setAtRoom(north);
							CommandServer.informCharacter(g, 
									north.displayRoomExceptCharacter(g));
						} else
							CommandServer.informCharacter(g, "����b�԰�����!\n");
					}
					return true;
				}
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,99,2", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}���䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}���䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}�F�䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("west") || msg[1].equals("w")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.WEST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}���䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}�F�䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
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
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}�F�䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,98,3", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}�F�䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("101,96,3", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (msg.length < 2) return false;
				
				if (msg[0].equals("open") || msg[0].equals("o")){
					if (msg[1].equals("east") || msg[1].equals("e")){
						IDoor targetDoor = g.getAtRoom().getExits().get(exit.EAST).getDoor();
						if (targetDoor.getDoorStatus() == doorStatus.CLOSED){
							String out = g.getChiName() + "���ձ��}�F�䪺���A���o�����ʡC�����O�����t�~�@��\n";
							out += "�Q����I�����F��צ�F�C\n";
							g.getAtRoom().informRoom(out);
							return true;
						}
					}
				}
				
				return false;
			}
			
		});
		
		EventUtil.mapCommandMap.put("104,110,1", new IRoomCommand() {

			@Override
			public boolean roomSpecialCommand(ICharacter g, String[] msg) {
				if (!(g instanceof PlayerCharacter)) return false;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
						MainMission.class.toString());
				if (mm.getState() == MainMission.State.AFTER_DEFEATED){
					CommandServer.informCharacter(g, g.list.get(0).list.get(0).getChiName()
							+ "�Q���ѤF�A���b���g���C\n");
					return true;
				}
				
				boolean match = false;
				
				if (msg.length > 1 &&
						(msg[1].equals("flee") ||
						msg[1].equals("fl")))
					match = true;
				else if (msg.length > 0 && (msg[0].equals("flee") ||
						msg[0].equals("fl"))) 
					match = true;
				
				if (match){
					CommandServer.informCharacter(g, g.list.get(0).list.get(0).getChiName()
							+ "���}�Q�¼v���F�A�ʼu���o!\n");
					return true;
				}
				
				return false;
			}
			
		});
	}
}
