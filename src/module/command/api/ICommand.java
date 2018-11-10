package module.command.api;

import module.character.api.ICharacter;

public interface ICommand {
	String[] getName();
	boolean action(ICharacter c, String[] command); // true = really moved, false = do nothing
	String getHelp();
	int getEnergyCost(); // used when in battle
}
