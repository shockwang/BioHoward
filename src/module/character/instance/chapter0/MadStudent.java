package module.character.instance.chapter0;

import module.battle.BattleTask;
import module.character.BaseHumanCharacter;
import module.character.PlayerGroup;
import module.character.constants.CStatus.status;

public class MadStudent extends BaseHumanCharacter{
	
	public MadStudent(){
		this("狂暴的學生", "mad student");
		StringBuffer buf = new StringBuffer();
		buf.append("大學裡隨處可見的學生。由於失去理智的緣故，衣著凌亂不堪，身上也\n");
		buf.append("全是灰塵，顯然不停地在尋找可以攻擊的目標。你看到他充滿惡意的向\n");
		buf.append("你撲過來，只好集中注意準備應戰。");
		this.setDesc(buf.toString());
		this.setStatus(status.STRENGTH, 28);
		this.setHostile(true);
	}
	
	public MadStudent(String chiName, String engName) {
		super(chiName, engName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onTalk(PlayerGroup g){
		g.getAtRoom().informRoom(String.format("%s大吼一聲，朝%s撲了過來!\n", 
				this.getChiName(), g.list.get(0).charList.get(0).getChiName()));
		new BattleTask(this.getMyGroup(), g);
	}
}
