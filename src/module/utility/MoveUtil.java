package module.utility;

import module.map.constants.CExit.exit;

public class MoveUtil {
	public static exit getWay(String input) {
		if (input.equals("east") || input.equals("e"))
			return exit.EAST;
		if (input.equals("west") || input.equals("w"))
			return exit.WEST;
		if (input.equals("south") || input.equals("s"))
			return exit.SOUTH;
		if (input.equals("north") || input.equals("n"))
			return exit.NORTH;
		if (input.equals("up") || input.equals("u"))
			return exit.UP;
		if (input.equals("down") || input.equals("d"))
			return exit.DOWN;
		return null;
	}

	public static exit getOppositeWay(exit way) {
		if (way == exit.EAST)
			return exit.WEST;
		if (way == exit.WEST)
			return exit.EAST;
		if (way == exit.SOUTH)
			return exit.NORTH;
		if (way == exit.NORTH)
			return exit.SOUTH;
		if (way == exit.UP)
			return exit.DOWN;
		if (way == exit.DOWN)
			return exit.UP;
		return null;
	}

}
