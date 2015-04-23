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
		this("滅火器", "fire extinguisher");
		this.setPrice(1000);
		this.setDescription("每間宿舍必備的滅火器，還蠻重的，用來砸人肯定很痛。");
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
		return "噴得別人滿臉都是白白的泡沫A_A";
	}

	@Override
	protected boolean useAction(ICharacter src, ICharacter target) {
		Group g = src.getMyGroup();
		
		if (count >= 10)
			CommandServer.informGroup(g, this.getChiName()
					+ "的內容物已經耗盡，再也噴不出東西來了。\n");
		else {
			if (src == target) {
				/*
				 * StringBuffer buf = new StringBuffer();
				 * buf.append(String.format("%s舉起%s放在胸前，身體微向後傾，溫柔環抱%s如同薩克斯風。\n",
				 * src.getChiName(), this.getChiName(), this.getChiName()));
				 * buf.append("他輕閉雙眼，眉頭微皺，專注的把滅火器的噴嘴當做吹嘴，深情演奏著。\n");
				 * buf.append("你不禁想要駐足聆聽那深情動人的音...更正，是假裝你不認識他。\n");
				 * buf.append("Jazz for you Soul!\n");
				 * g.getAtRoom().informRoom(buf.toString());
				 */
				g.getAtRoom().informRoom(
						String.format("%s舉起%s對著自己的臉狂噴，他有病嗎?\n",
								src.getChiName(), this.getChiName(), src.getChiName()));
				CSpecialStatus.setSpecialStatus(src, specialStatus.BLIND, 10);
			}
			else {
				g.getAtRoom().informRoom(
						String.format("%s舉起%s對著%s的臉上噴去，使他的臉上蓋滿了白白的泡沫!\n",
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
					+ "的內容物已經耗盡，再也噴不出東西來了。\n");
		else {
			g.getAtRoom().informRoom(src.getChiName() + "舉起滅火器亂噴，把泡沫灑的到處都是!\n");
			count++;
			return true;
		}
		return false;
	}
}
