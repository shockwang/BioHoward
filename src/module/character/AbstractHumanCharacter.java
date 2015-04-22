package module.character;

public class AbstractHumanCharacter extends AbstractCharacter{

	public AbstractHumanCharacter(String chiName, String engName) {
		super(chiName, engName);
		this.bodyPartList = new String[] {
				"繷场", "オ", "", "场", "浮场", "羪", "福ˊ",
				"オも", "も", "璉场", "オ籐", "籐"
		};
	}

	@Override
	public String getBareHandAttackMessage() {
		return "揣ю阑";
	}

}
