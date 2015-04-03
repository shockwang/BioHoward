package module.event.api;

import module.character.Group;

public interface IRoomCommand {
	boolean roomSpecialCommand(Group g, String[] msg);
}
