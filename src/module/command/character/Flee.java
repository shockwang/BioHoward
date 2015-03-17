package module.command.character;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.command.api.ICommand;
import module.utility.NpcActionUtil;

public class Flee implements ICommand{
	private String[] name;
	
	public Flee(){
		name = new String[2];
		name[0] = "flee";
		name[1] = "fl";
	}
	
	@Override
	public String[] getName() {
		return name;
	}

	@Override
	public boolean action(ICharacter c, String[] command) {
		Group g = c.getMyGroup();
		
		if (!g.getInBattle()){
			CommandServer.informGroup(g, "�A�èS���b�԰����A����n�k�]?\n");
			return false;
		} else {
			// TODO: implement flee mechanism
			int prob = 60;
			if (prob > 50){
				if (g.getAtRoom().getExits().size() == 0){
					g.getAtRoom().informRoom(
							String.format("%s���\���@�ӪŻطǳưk���԰��A���o�o�{�ж��S���X�f�A�L�k�k��!\n", c.getChiName()));
					return true;
				}
				g.getAtRoom().informRoom(c.getChiName() + "�}���٪o�A�@�ȵҪ����\�k�X�F�Գ��C\n");
				g.getBattleTask().removeBattleGroup(g);
				NpcActionUtil.randomMove(g);
			} else {
				g.getAtRoom().informRoom(c.getChiName() + "���հk�]�A���������ѤF!\n");
			}
		}
		return true;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
