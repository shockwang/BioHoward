package module.command.api;

import module.character.api.ICharacter;

public interface Command {
	String[] getName();
	boolean action(ICharacter c, String[] command); // true = really moved, false = do nothing
	String getHelp();
}
