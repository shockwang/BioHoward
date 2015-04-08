package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.item.useable.AbstractUsableItem;
import module.utility.BattleUtil;

public class Juice extends AbstractUsableItem{
	private int count;
	
	public Juice(){
		this("�@�M�G��", "juice");
		this.setDescription("�ݰ_�ӹ��O����Ī��C��A�Ʊ��٨S�L���C");
		this.setPrice(50);
		count = 0;
	}
	
	public Juice(String chiName, String engName) {
		super(chiName, engName);
	}
	
	@Override
	public boolean onUse(ICharacter src) {
		Group g = src.getMyGroup();
		
		if (super.onUse(src)){
			StringBuffer buf = new StringBuffer();
			buf.append(src.getChiName() + "�ܤF�@�f�G�ġA");
			if (BattleUtil.characterAttributeChange(src, attribute.HP, 5))
				buf.append("�^�_�F5�I��O�C\n");
			else
				buf.append("������Ƴ��S�o�͡C\n");
			
			g.getAtRoom().informRoom(buf.toString());
			count++;
			checkEmpty(src);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();
		
		if (super.onUse(src)){
			if (src == target) return this.onUse(src);
			
			StringBuffer buf = new StringBuffer();
			buf.append(String.format("%s��%s�ܤF�@�f�G�ġA", src.getChiName(), target.getChiName()));
			if (BattleUtil.characterAttributeChange(target, attribute.HP, 5))
				buf.append("�ϥL�^�_�F5�I��O�C\n");
			else
				buf.append("������Ƴ��S�o�͡C\n");
			
			g.getAtRoom().informRoom(buf.toString());
			count++;
			checkEmpty(src);
			return true;
		}
		return false;
	}

	@Override
	public String useEffect() {
		return "�l�@�f�^�_5�I��O�C";
	}
	
	private void checkEmpty(ICharacter src){
		if (count >= 15){
			src.getMyGroup().getAtRoom().informRoom(String.format("�M�l�̪��G�ĳܧ��F�A%s�H�⥵���F���C\n", 
					src.getChiName()));
			src.getMyGroup().getInventory().removeItem(this);
		}
	}
}
