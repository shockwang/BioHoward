package module.item.instance.chapter0;

import module.character.Group;
import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.useable.AbstractUsableItem;

public class FireExtinguisher extends AbstractUsableItem{
	private int count;
	
	public FireExtinguisher(){
		this("滅火器", "fire extinguisher");
		this.setPrice(1000);
		this.setDescription("每間宿舍必備的滅火器，還蠻重的，平常該不會有人拿來重訓?");
		count = 0;
	}
	
	public FireExtinguisher(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public boolean onUse(ICharacter src) {
		Group g = src.getMyGroup();
		
		if (super.onUse(src)){
			if (count >= 10)
				CommandServer.informGroup(g, this.getChiName() + "的內容物已經耗盡，再也噴不出東西來了。\n");
			else {
				g.getAtRoom().informRoom(String.format("%s舉起%s亂噴，把泡沫灑的到處都是!\n", 
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
		
		if (super.onUse(src)){
			if (count >= 10)
				CommandServer.informGroup(g, this.getChiName() + "的內容物已經耗盡，再也噴不出東西來了。\n");
			else {
				if (src == target) 
					g.getAtRoom().informRoom(String.format("%s舉起%s對著自己的臉狂噴，他有病嗎?\n", 
							src.getChiName(), this.getChiName()));
				else {
					g.getAtRoom().informRoom(String.format("%s舉起%s對著%s的臉上噴去，滿臉的泡沫使%s暫時失明了!\n",
							src.getChiName(), this.getChiName(), target.getChiName(), target.getChiName()));
					// TODO: implement blind mechanism, also trigger battle event
				}
				count++;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isExpired(){
		// special item, not gonna to expire
		return false;
	}

	@Override
	public String useEffect() {
		return "噴得別人滿臉都是白白的泡沫A_A";
	}
}
