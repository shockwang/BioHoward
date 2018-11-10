package module.character;

public class BaseHumanCharacter extends AbstractCharacter{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7934028240450284691L;

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
