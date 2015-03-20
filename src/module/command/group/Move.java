package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;
import module.utility.MoveUtil;
import module.utility.NpcActionUtil;

public class Move implements ICommand {
	private String[] name;

	public Move() {
		name = new String[12];
		name[0] = "north";
		name[1] = "n";
		name[2] = "south";
		name[3] = "s";
		name[4] = "east";
		name[5] = "e";
		name[6] = "west";
		name[7] = "w";
		name[8] = "up";
		name[9] = "u";
		name[10] = "down";
		name[11] = "d";
	}

	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		exit go = MoveUtil.getWay(command[0]);
		exit from = MoveUtil.getOppositeWay(go);

		Group g = c.getMyGroup();

		synchronized (g.getAtRoom()) {
			if (g.getInBattle()) {
				CommandServer.informGroup(g, "����b�԰�����!\n");
			} else {
				// implement the go-north mechanism.
				if (g.getAtRoom().getExits().get(go) == null)
					CommandServer.informGroup(g, "�o�Ӥ�V�S������!\n");
				else if (g.getAtRoom().getExits().get(go).getDoor() == null
						|| g.getAtRoom().getExits().get(go).getDoor()
								.getDoorStatus() == doorStatus.OPENED) {
					IRoom here = g.getAtRoom();
					IRoom nRoom = here.getExits().get(go).getRoom();
					here.getGroupList().gList.remove(g);
					here.informRoom(g.getChiName() + "��" + go.chineseName
							+ "�����}�F�C\n");
					nRoom.getGroupList().gList.add(g);
					g.setAtRoom(nRoom);
					nRoom.informRoomExceptGroup(g, g.getChiName() + "�q"
							+ from.chineseName + "��L�ӤF�C\n");
					CommandServer.informGroup(g,
							nRoom.displayRoomExceptGroup(g));
					NpcActionUtil.checkAutoAttackPlayerGroup(g.getAtRoom());
				} else
					CommandServer.informGroup(g, "���䪺���O���۪��C\n");
			}
			return false;
		}
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
}
