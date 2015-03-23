package module.event;

import module.character.Group;
import module.character.PlayerGroup;
import module.event.api.IEvent;

public abstract class AbstractEvent implements IEvent{

	@Override
	public boolean isTriggered(Group g) {
		if (g instanceof PlayerGroup) return true;
		return false;
	}
}
