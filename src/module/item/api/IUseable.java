package module.item.api;

import module.character.api.ICharacter;

public interface IUseable {
	boolean onUse(ICharacter src);
	boolean onUse(ICharacter src, ICharacter target);
	
	// item display use effect
	String useEffect();
}
