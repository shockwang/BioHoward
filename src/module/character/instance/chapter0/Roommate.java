package module.character.instance.chapter0;

import java.io.BufferedReader;

import module.battle.BattleTask;
import module.battle.chapter0.FirstTutorialBattle;
import module.character.AbstractCharacter;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CConfig.config;
import module.utility.EventUtil;
import module.utility.NpcBattleActionUtil;

public class Roommate extends AbstractCharacter{
	private String description = null;
	
	public Roommate(){
		this("�Ǥ�", "howard's roommate");
	}
	
	public Roommate(String chiName, String engName) {
		super(chiName, engName);
		// TODO assign attribute here
		this.description= "�N�O�Ǥ͡C";
		this.addAttribute(attribute.HP, 50);
	}

	@Override
	public void setDesc(String description) {
		this.description = description;
	}

	@Override
	public String getDesc() {
		return this.description;
	}

	@Override
	public boolean battleAction(GroupList enemyGroup) {
		NpcBattleActionUtil.randomAttack(this, enemyGroup);
		return true;
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		StringBuffer buf = new StringBuffer();
		BufferedReader in = g.getInFromClient();
		buf.append("�N�إs�ǤͲĤ@���S�����C");
		EventUtil.informReset(g, buf, in);
		buf.append("�N�إs�ǤͲĤG���S�����C");
		EventUtil.informReset(g, buf, in);
		buf.append("�N�ة�Ǥ͡A�Ǥͧq�q�q�q~~");
		g.getAtRoom().informRoom(buf.toString() + "\n");
		buf.setLength(0);
		if (g.getConfigData().get(config.TUTORIAL_ON)){
			buf.append("�԰������G\n");
			buf.append("�C�������԰��Ĩ��}�窺�ɶ����԰��覡�C�i�J�԰���A�������A\n");
			buf.append("�C������|�X�{�Ө���b�԰������ɶ����A���ɶ����W�[��100%��\n");
			buf.append("�K�i��ʡC�b�������~���ɶ���Ө���U���O�h�|�o��\"�Ө���|��\n");
			buf.append("�ǳƦn\"���^���C");
			EventUtil.informReset(g, buf, in);
			buf.append("���~�A�C�����԰��Ҧ��i���\"�Y�ɾ԰�/�D�Y�ɾ԰�\"��ءC�b�Y�ɾ԰�\n");
			buf.append("���A�Y�K�ڤ訤�⪺�ɶ����W�[��100%�A��L�H����ʨ��¤��|����A�|�~��\n");
			buf.append("�W�[�ɶ����C�������쪱�a��Ө���i��ާ@��A�Ө��⪺�ɶ����~�|���s�}�l\n");
			buf.append("�p��C�]�N�O���A���r�t�פ����֪��H�i��|�]���Q�ĤH�h���n�X�U��...�зV��\n");
			buf.append("��ܡC");
			EventUtil.informReset(g, buf, in);
			buf.append("�Ϥ��A�D�Y�ɾ԰��h�O�b�ڤ訤��ɶ����W��100%�ɡA�P�@���԰���������ҷ|\n");
			buf.append("���U�ʧ@�A���ݸӨ����ʧ�������A�~��W�[�U�۪��ɶ����C�M�ӻݭn�`�N�G�Y�K\n");
			buf.append("�P�@���԰������ɶ����Ȱ��F�A�C�������ɶ����¦b�B��A���[�J�ӳ��԰�������]\n");
			buf.append("����ۥѦ�ʡC");
			EventUtil.informReset(g, buf, in);
			// TODO: add modify config method & explanation
			buf.append("�����]�w���w�]�Ȭ�\"�D�Y�ɾ԰�\"�A�A�i�H�z�Lblabla�ק復�C");
			EventUtil.informReset(g, buf, in);
			BattleTask task = new FirstTutorialBattle(this.getMyGroup(), g);
		}
	}
	
	@Override
	public void normalAction() {
		// do nothing
	}
}