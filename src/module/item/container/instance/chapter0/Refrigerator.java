package module.item.container.instance.chapter0;

import module.item.container.BaseFixedPositionContainer;
import module.item.instance.chapter0.Cake;
import module.item.instance.chapter0.Juice;

public class Refrigerator extends BaseFixedPositionContainer {
	
	public Refrigerator(){
		this("�B�c", "refrigerator");
		StringBuffer buf = new StringBuffer();
		buf.append("�q�N�J�٪��B�c�A�ǥ̧ͭ�U�ح��������\�b�̭��A�|���|�w���M�z�h�O�t�~\n");
		buf.append("�@�^�ơC���F�i���ܦ����q���U�����~�A�]�i��o�ͦۤv���F��Q�O�H�Y��\n");
		buf.append("���~�p�C�{�b�ϥ��O�D�`�ɴ��A�����@�I�����]�O�i�H�Q��̪��a?");
		this.setDescription(buf.toString());
		this.list.addItem(new Cake());
		this.list.addItem(new Juice());
	}
	
	public Refrigerator(String chiName, String engName) {
		super(chiName, engName);
	}

}
