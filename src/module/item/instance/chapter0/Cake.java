package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.item.useable.AbstractUsableItem;
import module.utility.BattleUtil;

public class Cake extends AbstractUsableItem {

	public Cake() {
		this("85��C�J�|", "cake");
		this.setDescription("�@���b85��C�R���_�q�J�|�A�ݰ_�ӫܥi�f!");
		this.setPrice(85);
	}

	public Cake(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean onUse(ICharacter src) {
		Group g = src.getMyGroup();

		if (super.onUse(src)) {
			StringBuffer buf = new StringBuffer();
			buf.append(String.format("%s�@�f�Y�U%s�A", src.getChiName(), this.getChiName()));
			if (BattleUtil.characterAttributeChange(src, attribute.HP, 20))
				buf.append("�^�_�F20�I��O�C\n");
			else buf.append("������Ƴ��S���o�͡C\n");
			
			g.getAtRoom().informRoom(buf.toString());
			g.getInventory().removeItem(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();

		if (super.onUse(src)) {
			if (src == target) return this.onUse(src);
			else {
				StringBuffer buf = new StringBuffer();
				buf.append(String.format("%s��%s�Y�U�F%s�A", src.getChiName(), 
						target.getChiName(), this.getChiName()));
				if (BattleUtil.characterAttributeChange(target, attribute.HP, 20))
					buf.append("�ϥL�^�_�F20�I��O�C\n");
				else buf.append("������Ƴ��S���o�͡C\n");
				
				g.getAtRoom().informRoom(buf.toString());
				g.getInventory().removeItem(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public String useEffect() {
		return "�^�_20�I��O�C";
	}

}
