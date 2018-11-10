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
		this("�o����", "hydraulic cut");
		String desc = "�@��̾a�ڴ��d�G����z���_��w���骺�o���šC�A�ܦn�_�������\n";
		desc += "�ͩж��̷|���o�تF��? �����w�L�a�O�}���q�u���a�C";
		this.setDescription(desc);
		this.setPrice(699);
	}
	
	public HydraulicCut(String chiName, String engName) {
		super(chiName, engName);
	}

	@Override
	public String useEffect() {
		return "�γ\�i�H�Ŷ}����ܮz���K���C";
	}

	@Override
	protected boolean useAction(ICharacter src, ICharacter target) {
		CommandServer.informCharacter(src, 
				"�o�ӹD�㤣�A�X���ӧ����O�H��C\n");
		return false;
	}

	@Override
	protected boolean useAction(ICharacter src) {
		IRoom targetRoom = MapUtil.roomMap.get("102,100,1");
		if (src.getAtRoom() == targetRoom){
			targetRoom.informRoom(src.getChiName() + "���оާ@�۪o���šA�ש�b�ᦱ���K���W�}�X�F�@�D�X�f�C\n");
			CommandServer.informCharacter(src, 
					src.getChiName() + "���F���Y�W�����G\"�I~�`��i�H�X�h�F�A�����}�o���a��a!\"\n");
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
					src.getChiName() + "���U�|�g�A�o�䤣��A�X���a��U��C\n");
			return false;
		}
	}
	
	@Override
	public boolean isExpired(){
		// always not expire
		return false;
	}
}
