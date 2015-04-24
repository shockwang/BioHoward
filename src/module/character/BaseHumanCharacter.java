package module.character;

public class BaseHumanCharacter extends AbstractCharacter{

	public BaseHumanCharacter(String chiName, String engName) {
		super(chiName, engName);
		this.bodyPartList = new String[] {
				"�Y��", "����", "�k��", "�ݳ�", "����", "�y�W", "�ḣ�@",
				"����", "�k��", "�I��", "���L", "�k�L"
		};
	}

	@Override
	public String getBareHandAttackMessage() {
		return "��������";
	}

}
