package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Soap extends AbstractItem{

	public Soap(){
		this("�Ψm", "soap");
		String desc = "�@�����q���Ψm�C�p�G�A�ݨ쥦�X�{�b�a�W�d�U���n�þߡA�_�h...\n";
		desc += "�K�K�K...";
		this.setDescription(desc);
		this.setPrice(19);
	}
	
	public Soap(String chiName, String engName) {
		super(chiName, engName);
	}
}
