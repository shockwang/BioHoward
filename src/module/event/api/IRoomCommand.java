package module.event.api;

import module.character.api.ICharacter;

public interface IRoomCommand {
	boolean roomSpecialCommand(ICharacter c, String[] msg);
}
