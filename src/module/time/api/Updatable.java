package module.time.api;

public interface Updatable {
	// for Group, Item, ICharacter to implements, to let the global timer be
	// able to update their behavior
	public void updateTime();
}
