package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.character.constants.CSpecialStatus.specialStatus;
import module.command.CommandServer;
import module.item.useable.AbstractUsableItem;

public class FireExtinguisher extends AbstractUsableItem {
	private int count;

	public FireExtinguisher() {
		this("������", "fire extinguisher");
		this.setPrice(1000);
		this.setDescription("�C���J�٥��ƪ��������A���Z�����A���`�Ӥ��|���H���ӭ��V?");
		count = 0;
	}

	public FireExtinguisher(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean onUse(ICharacter src) {
		Group g = src.getMyGroup();

		if (super.onUse(src)) {
			/*
			 * StringBuffer buf = new StringBuffer();
			 * buf.append(String.format("%s�|�_%s��b�ݫe�A����L�V��ɡA�ŬX����%s�p�P�ħJ�����C\n",
			 * src.getChiName(), this.getChiName(), this.getChiName()));
			 * buf.append("�L���������A���Y�L�K�A�M�`������������Q�L�����j�L�A�`���t���ۡC\n");
			 * buf.append("�A���T�Q�n�n����ť���`���ʤH����...�󥿡A�O���˧A���{�ѥL�C\n");
			 * buf.append("Jazz for you Soul!\n");
			 * g.getAtRoom().informRoom(buf.toString());
			 */
			if (count >= 10)
				CommandServer.informGroup(g, this.getChiName()
						+ "�����e���w�g�ӺɡA�A�]�Q���X�F��ӤF�C\n");
			else {
				g.getAtRoom().informRoom(
						String.format("%s�|�_%s�üQ�A��w�j�x����B���O!\n",
								src.getChiName(), this.getChiName()));
				count++;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onUse(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();

		if (super.onUse(src)) {
			if (count >= 10)
				CommandServer.informGroup(g, this.getChiName()
						+ "�����e���w�g�ӺɡA�A�]�Q���X�F��ӤF�C\n");
			else {
				if (src == target) {
					g.getAtRoom().informRoom(
							String.format("%s�|�_%s��ۦۤv���y�g�Q�A�L���f��?\n���y���w�j��%s�Ȯɥ����F!\n",
									src.getChiName(), this.getChiName(), src.getChiName()));
					src.setSpecialStatus(specialStatus.BLIND, 10);
				}
				else {
					g.getAtRoom().informRoom(
							String.format("%s�|�_%s���%s���y�W�Q�h�A���y���w�j��%s�Ȯɥ����F!\n",
									src.getChiName(), this.getChiName(),
									target.getChiName(), target.getChiName()));
					// TODO: implement blind mechanism, also trigger battle
					// event
					target.setSpecialStatus(specialStatus.BLIND, 10);
				}
				count++;
				return true;
			}
		}
		return false;
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
}