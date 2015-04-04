package module.event.map.instance.chapter0;

import module.character.Group;
import module.command.CommandServer;
import module.event.api.IRoomCommand;
import module.map.api.IRoom;
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
													+ "�|�_�������A�ΤO�a���������{�U�h! �J��!\n�u�����W�������H�F�@�a�C\n");
							mm.setState(MainMission.State.AFTER_BREAK_MANAGE_DOOR);
						} else {
							g.getAtRoom().informRoom(
									g.list.get(0).charList.get(0).getChiName()
											+ "���W�èS���a�۾A�����u��C\n");
						}
					} else if (((MainMission.State) mm.getState()).index >= MainMission.State.AFTER_BREAK_MANAGE_DOOR.index) {
						g.getAtRoom().informRoom("���W�������w�g�Q���H�F�C\n");
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
								CommandServer.informGroup(g, "����b�԰�����!\n");
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
											"%s��L���W�}�H���������ŻءA�p�X�F�J�ٺ޲z���ߡC\n",
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
							CommandServer.informGroup(g, "����b�԰�����!\n");
					}
					return true;
				}
				return false;
			}
			
		});
	}
}