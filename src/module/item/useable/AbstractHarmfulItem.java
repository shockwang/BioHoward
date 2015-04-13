package module.item.useable;

import module.character.api.ICharacter;
import module.utility.BattleUtil;

public abstract class AbstractHarmfulItem extends AbstractUsableItem{

	public AbstractHarmfulItem(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean onUse(ICharacter src) {
		if (super.onUse(src)){
			if (BattleUtil.checkIfAbleToStartBattle(src, src)){
				if (useAction(src)){
					// TODO: define the whole-environment battle check?
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		if (super.onUse(src)){
			if (BattleUtil.checkIfAbleToStartBattle(src, target)){
				if (useAction(src, target)) {
					BattleUtil.handleBattleTaskBehavior(src, target);
					return true;
				}
			}
		}
		return false;
	}
	
	protected abstract boolean useAction(ICharacter src, ICharacter target);
	protected abstract boolean useAction(ICharacter src);
}
