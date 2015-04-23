package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CSpecialStatus;
import module.character.constants.CSpecialStatus.specialStatus;
import module.command.CommandServer;
import module.item.useable.AbstractHarmfulItem;

public class FireExtinguisher extends AbstractHarmfulItem {
	private int count;

	public FireExtinguisher() {
		this("������", "fire extinguisher");
		this.setPrice(1000);
		this.setDescription("�C���J�٥��ƪ��������A���Z�����A�Ψӯ{�H�֩w�ܵh�C");
		count = 0;
	}

	public FireExtinguisher(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean isExpired() {
		// special item, not gonna to expire
		return false;
	}

	@Override
	public String useEffect() {
		return "�Q�o�O�H���y���O�եժ��w�jA_A";
	}

	@Override
	protected boolean useAction(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();
		
		if (count >= 10)
			CommandServer.informGroup(g, this.getChiName()
					+ "�����e���w�g�ӺɡA�A�]�Q���X�F��ӤF�C\n");
		else {
			if (src == target) {
				/*
				 * StringBuffer buf = new StringBuffer();
				 * buf.append(String.format("%s�|�_%s��b�ݫe�A����L�V��ɡA�ŬX����%s�p�P�ħJ�����C\n",
				 * src.getChiName(), this.getChiName(), this.getChiName()));
				 * buf.append("�L���������A���Y�L�K�A�M�`������������Q�L���j�L�A�`���t���ۡC\n");
				 * buf.append("�A���T�Q�n�n����ť���`���ʤH����...�󥿡A�O���˧A���{�ѥL�C\n");
				 * buf.append("Jazz for you Soul!\n");
				 * g.getAtRoom().informRoom(buf.toString());
				 */
				g.getAtRoom().informRoom(
						String.format("%s�|�_%s��ۦۤv���y�g�Q�A�L���f��?\n",
								src.getChiName(), this.getChiName(), src.getChiName()));
				CSpecialStatus.setSpecialStatus(src, specialStatus.BLIND, 10);
			}
			else {
				g.getAtRoom().informRoom(
						String.format("%s�|�_%s���%s���y�W�Q�h�A�ϥL���y�W�\���F�եժ��w�j!\n",
								src.getChiName(), this.getChiName(),
								target.getChiName(), target.getChiName()));
				CSpecialStatus.setSpecialStatus(target, specialStatus.BLIND, 10);
			}
			count++;
			return true;
		}
		return false;
	}

	@Override
	protected boolean useAction(ICharacter src) {
		Group g = src.getMyGroup();
		
		if (count >= 10)
			CommandServer.informGroup(g, this.getChiName()
					+ "�����e���w�g�ӺɡA�A�]�Q���X�F��ӤF�C\n");
		else {
			g.getAtRoom().informRoom(src.getChiName() + "�|�_�������üQ�A��w�j�x����B���O!\n");
			count++;
			return true;
		}
		return false;
	}
}
