package module.item.instance.chapter0;

import module.item.AbstractItem;

public class Cake extends AbstractItem {
	
	public Cake(){
		this("85��C�J�|", "cake");
		this.setDescription("�@��85��C�R���_�q�J�|�A�ݰ_�ӫܥi�f!");
	}
	
	public Cake(String chiName, String engName) {
		super(chiName, engName);
	}

}
