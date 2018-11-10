package module.event;

import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.event.api.IEvent;

public abstract class AbstractEvent implements IEvent{

	@Override
	public boolean isTriggered(ICharacter c) {
		if (c instanceof PlayerCharacter) return true;
		return false;
	}
}
