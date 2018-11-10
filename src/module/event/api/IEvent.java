package module.event.api;

import module.character.api.ICharacter;

public interface IEvent {
	boolean isTriggered(ICharacter c);
	void doEvent(ICharacter c);
}
