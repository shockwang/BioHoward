package module.item.instance.chapter0;

import module.character.api.ICharacter;
import module.command.CommandServer;
import module.item.useable.AbstractHarmfulItem;
import module.map.Neighbor;
import module.map.api.IRoom;
import module.map.constants.CExit.exit;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.MapUtil;

public class HydraulicCut extends AbstractHarmfulItem{

	public HydraulicCut(){
		this("油壓剪", "hydraulic cut");
		String desc = "一把依靠巴斯卡液壓原理剪斷堅硬物體的油壓剪。你很好奇為什麼學\n";
		desc += "生房間裡會有這種東西? 說不定他家是開水電工的吧。";
		this.setDescription(desc);
		this.setPrice(699);
	}
	
	public HydraulicCut(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public String useEffect() {
		return "或許可以剪開比較脆弱的鐵窗。";
	}

	@Override
	protected boolean useAction(ICharacter src, ICharacter target) {
		CommandServer.informCharacter(src, 
				"這個道具不適合拿來攻擊別人喔。\n");
		return false;
	}

	@Override
	protected boolean useAction(ICharacter src) {
		IRoom targetRoom = MapUtil.roomMap.get("102,100,1");
		if (src.getAtRoom() == targetRoom){
			targetRoom.informRoom(src.getChiName() + "反覆操作著油壓剪，終於在扭曲的鐵窗上開出了一道出口。\n");
			CommandServer.informCharacter(src, 
					src.getChiName() + "擦了擦頭上的汗：\"呼~總算可以出去了，快離開這鬼地方吧!\"\n");
			PlayerServer.getMissionMap().get(MainMission.class.toString())
				.setState(MainMission.State.AFTER_EXIT_DORMITORY);
			
			// open a tunnel through these two rooms
			IRoom connectRoom = MapUtil.roomMap.get("103,100,1");
			targetRoom.setSingleExit(exit.EAST, new Neighbor(connectRoom, null));
			connectRoom.setSingleExit(exit.WEST, new Neighbor(targetRoom, null));
			return true;
		}
		else {
			CommandServer.informCharacter(src, 
					src.getChiName() + "環顧四週，卻找不到適合的地方下手。\n");
			return false;
		}
	}
	
	@Override
	public boolean isExpired(){
		// always not expire
		return false;
	}
}
