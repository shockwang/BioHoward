package module.command.group;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.map.api.IRoom;
import module.map.constants.CDoorAttribute.doorStatus;
import module.map.constants.CExit.exit;

public class Move implements ICommand{
	private String[] name;
	
	public Move(){
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
		exit go = getWay(command[0]);
		exit from = getOppositeWay(go);
		
		Group g = c.getMyGroup();
		if (g.getInBattle()){
			CommandServer.informGroup(g, "隊伍正在戰鬥中喔!\n");
		}
		else {
			// implement the go-north mechanism.
			if (g.getAtRoom().getExits().get(go) == null)
				CommandServer.informGroup(g, "這個方向沒有路喔!\n");
			else if (g.getAtRoom().getExits().get(go).getDoor() == null ||
					g.getAtRoom().getExits().get(go).getDoor().getDoorStatus() == doorStatus.OPENED){
				IRoom here = g.getAtRoom();
				IRoom nRoom = here.getExits().get(go).getRoom();
				here.getGroupList().gList.remove(g);
				here.informRoom(g.getChiName() + "朝" + go.chineseName + "邊離開了.\n");
				nRoom.informRoom(g.getChiName() + "從" + from.chineseName + "邊過來了.\n");
				CommandServer.informGroup(g, nRoom.displayRoom());
				nRoom.getGroupList().gList.add(g);
				g.setAtRoom(nRoom);
			}
			else 
				CommandServer.informGroup(g, "那邊的門是關著的.\n");
		}
		return false;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private exit getWay(String input){
		if (input.equals("east") || input.equals("e")) return exit.EAST;
		if (input.equals("west") || input.equals("w")) return exit.WEST;
		if (input.equals("south") || input.equals("s")) return exit.SOUTH;
		if (input.equals("north") || input.equals("n")) return exit.NORTH;
		if (input.equals("up") || input.equals("u")) return exit.UP;
		if (input.equals("down") || input.equals("d")) return exit.DOWN;
		return null;
	}
	
	private exit getOppositeWay(exit way){
		if (way == exit.EAST) return exit.WEST;
		if (way == exit.WEST) return exit.EAST;
		if (way == exit.SOUTH) return exit.NORTH;
		if (way == exit.NORTH) return exit.SOUTH;
		if (way == exit.UP) return exit.DOWN;
		if (way == exit.DOWN) return exit.UP;
		return null;
	}

}
