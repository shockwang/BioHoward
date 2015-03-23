package module.event.api;

import module.character.Group;

public interface IEvent {
	boolean isTriggered(Group g);
	void doEvent(Group g);
}
