package module.event.map.instance.chapter0;

import java.io.BufferedReader;

import module.battle.BattleTask;
import module.battle.chapter0.DormKeeperBattle;
import module.battle.chapter0.ShadowBattle;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.constants.CConfig.config;
import module.character.instance.chapter0.DarkShadow;
import module.character.instance.chapter0.MadStudent;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.item.instance.chapter0.Soap;
import module.map.api.IRoom;
import module.mission.api.IMission;
import module.mission.chapter0.ContainerTutorialMission;
import module.mission.chapter0.FirstTimeSeeKeeperMission;
import module.mission.chapter0.MainMission;
import module.mission.chapter0.SoapMission;
import module.mission.chapter0.TwoDoorsMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;
import module.utility.MapUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("102,100,3", new AbstractEvent(){
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
					if (mm == null ||
							mm.getState() == MainMission.State.AFTER_OPENING ||
							mm.getState() == MainMission.State.AFTER_FIRST_BATTLE)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (mm == null) {
					g.setInEvent(true);
					mm = new MainMission();
					PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
					mm.setState(MainMission.State.AFTER_OPENING);
					
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						EventUtil.executeEventMessage(pg, "after_wake_up");
						
						buf.append("�N�ؤ߷Q�G�i�}�����ݬݩж��a.\n");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "look_tutorial");
							buf.append("�п�J\"look\"��\"l\"���[��A�{�b�ҳB���ж����A�C");
							g.getAtRoom().informRoom(buf.toString() + "\n");
							buf.setLength(0);
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look") && !input.equals("l")){
								buf.append("�п�J\"look\"��\"l\"���[��A�{�b�ҳB���ж����A�C");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
						} 
						String[] msg = {"look"};
						CommandServer.readCommand(pg, msg);
						EventUtil.informCheckReset(pg, buf, in);
						
						// talk to roommate
						EventUtil.executeEventMessage(pg, "after_see_room");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "talk_tutorial");
							g.getAtRoom().informRoom("�п�J\"talk roommate\"�ӻP�Ǥͥ�͡C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("talk roommate")){
								buf.append("�п�J\"talk roommate\"�ӻP�Ǥͥ�͡C");
								g.getAtRoom().informRoom(buf.toString() + "\n");
								buf.setLength(0);
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					String[] msg2 = {"talk", "roommate"};
					CommandServer.readCommand(pg, msg2);
				} else if (mm.getState() == MainMission.State.AFTER_FIRST_BATTLE){
					g.setInEvent(true);
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						EventUtil.executeEventMessage(pg, "after_beat_roommate");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							EventUtil.executeEventMessage(pg, "get_tutorial");
							g.getAtRoom().informRoom("�п�J\"get key\"�Ӿ߰_�J���_�͡C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("get key")){
								g.getAtRoom().informRoom("�п�J\"get key\"�Ӿ߰_�J���_�͡C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg = {"get", "key"};
						CommandServer.readCommand(pg, msg);
						EventUtil.executeEventMessage(pg, "after_get_key");
						
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"unlock\"���O�i�H���A�Ѷ}������A�e���O�A���W�a�ۦX�A\n");
							buf.append("���_�͡C�۹諸�A\"lock\"���O�h�O�i�H���A���@�����C���O�榡��\n");
							buf.append("\"<�ڤ訤��W��> <lock/unlock> <���W��>\"�A�䤤���W�٫��w�A�Q��\n");
							buf.append("���Ӥ�V�}���C�Y�����w�ڤ訤��W�١A�h�۰ʿ�ܶ���Ĥ@�H�i��ʧ@�C\n");
							buf.append("�Բӻ����i��J\"help lock\"�H��\"help unlock\"�Ӭd�ߡC");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("�����G�C���������@��\"�F/��/�n/�_/�W/�U\"���ءA�b���O�W���O����\n");
							buf.append("��\"east/west/south/north/up/down\"�C��J���W�ٮɥi�H�u��J\n");
							buf.append("²�g�A���O��\"e/w/s/n/u/d\"�C");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"unlock w\"�ӸѶ}�ж�������C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("unlock w")){
								g.getAtRoom().informRoom("�п�J\"unlock w\"�ӸѶ}�ж�������C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg2 = {"unlock", "w"};
						CommandServer.readCommand(pg, msg2);
						buf.append("�N�ءG\"���}���X�h�ݬݧa�C\"");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"open\"���O�i�H���A���}�@�����۪����A�e���O���S���W��C\n");
							buf.append("�۹諸�A\"close\"���O�h�i�H���A���W�@���}�۪����C���O�榡��\n");
							buf.append("\"<�ڤ訤��W��> <open/close> <���W��>\"�C�Y�����w�ڤ訤��\n");
							buf.append("�A�h�۰ʿ�ܶ���Ĥ@�H�i��ʰ��C�Բӻ����i��J\"help open\"\n");
							buf.append("�H��\"help close\"�Ӭd�ߡC");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"open w\"�ӥ��}�ж������C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("open w")){
								g.getAtRoom().informRoom("�п�J\"open w\"�ӥ��}�ж������C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg3 = {"open", "w"};
						CommandServer.readCommand(pg, msg3);
						buf.append("�X���e�ݤ@�U�~�����p�a�C");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"look\"���O���F�[��ۤv�Ҧb��m����T�~�A�٥i�H�Ψ��[��\n");
							buf.append("�P��۳s����m���p�C���O�榡��\"<look> <���W��>\"�C�Y�Ӥ�즳\n");
							buf.append("�ж��۳s�A�h�A�|�ݨ�өж������p�F�Y�Ӥ�즳���۪����A�h�A�|�ݨ�\n");
							buf.append("�Ӯ������ԭz�C");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"look w\"���[����~�����p�C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("look w")){
								g.getAtRoom().informRoom("�п�J\"look w\"���[����~�����p�C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg4 = {"look", "w"};
						CommandServer.readCommand(pg, msg4);
						buf.append("�N�ءG\"�~���]�i�H�ݨ�_�Ǫ��H�A���٬O��������W�𮧤@�U�C\"");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)) {
							g.getAtRoom().informRoom("�п�J\"close w\"�����W�J�٩ж������C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("close w")){
								g.getAtRoom().informRoom("�п�J\"close w\"�����W�J�٩ж������C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg5 = {"close", "w"};
						CommandServer.readCommand(pg, msg5);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G�H�۹C�������ɶ��y�u�A���⪺��O�|������_�C�ҥH���⪬�A\n");
							buf.append("�C�U�ɥi�H�Ҽ{��Ӧw�����a���q�q�ݤW�X������! �C�������ɶ�����\n");
							buf.append("��{�ꤤ��\"2�����{��ɶ� = 1�����C���ɶ�\"�C�A�i�H��J\"time\"\n");
							buf.append("���O�ӱo���C�������ɶ��C");
							EventUtil.informCheckReset(pg, buf, in);
						}
						buf.append("�N�صo�{�J�٪��O��M���G�F�A�Ҧ��q���Ϋ~�]���}���_�ӡC��M��\n");
						buf.append("�u�O�J�ٵo�Ͱ��D�A�γ\��ӷs�˳��X���p�F�C\"�ݨӤ���@���ݦb\n");
						buf.append("�o��...\" �N���q�Q�ۡA\"�y�@�𮧤���N�X�o�h�����a�A�����n\n");
						buf.append("�Q��k�w�����}�o�ӱJ�١C\"");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"mission\"��\"m\"���O������A�d�ݦۤv�ثe���W�����ǥ���\n");
							buf.append("�H�α��U�ӥi�H�Ĩ����Ǧ�ʡC�b�C���i��ɦp�G�ѰO�ۤv���U�ӭn������A\n");
							buf.append("�o�|�O�@�ӫܦn�Ϊ����O��!");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"mission\"��\"m\"�Ӭd�ݷ�e���ȡC\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("mission") && !input.equals("m")){
								g.getAtRoom().informRoom("�п�J\"mission\"��\"m\"�Ӭd�ݷ�e���ȡC\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
							mm.setState(MainMission.State.START_SEARCHING);
							String[] msg7 = {"mission"};
							CommandServer.readCommand(pg, msg7);
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("�����G����ʮɪ�����J���W�٧Y�i�A���N�p�P���e���L�����ˡA\n");
							buf.append("�@��\"�F/��/�n/�_/�W/�U\"���إi�઺���p�A�������O���O��\"east/\n");
							buf.append("west/south/north/up/down\"�A��²�g��\"e/w/s/n/u/d\"�C");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("�����G��A��J\"look\"�[��Ҧb�ж�����T�ɡA�ж��ԭz�U���|���\n");
							buf.append("�X�f��T�A�Τ��A���]�_�ӡC�ȱo�`�N���O�A�X�f��T�u�|��ܷ�U�i�H\n");
							buf.append("�����樫����V�A��L�S��ܪ���V�i��u�O�����_�ӤF�A���@�w�N�O\n");
							buf.append("�S���X�f��! �]�����`�N�i�H��J���P�����ӱ����ݬݦ��S���ۤv��\n");
							buf.append("�S�o�{���q�D!");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("�����G�H�W�O�i�楻�C���|�Ψ쪺�򥻫��O�C�{�b�A��z�@�U�G\n");
							buf.append("\"<look/l>\"�i�H�Ψ��[��Ҧb�ж�����T�C\n");
							buf.append("\"<look/l> <���N��>\"�i�H�Ψ��[��Y��V����T�C\n");
							buf.append("\"<talk/ta> <����W��>\"�i�H���A�P�Y�H��ܡC\n");
							buf.append("\"<attack/at> <����W��>\"�i�H���A�����Y�쨤��öi�J�԰��C\n");
							buf.append("\"<inventory/i>\"�i�H���A�d�ݨ��W�����~�C");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("\"<equipment/eq>\"�i�H���A�d�ݶ�����⪺�˳Ʊ��p�C\n");
							buf.append("\"<get/g> <���~�W��>\"�i�H���A�߰_�a�W���Y��S�w���~�C\n");
							buf.append("\"<drop/dr> <���~�W��>\"�i�H���A��U���W���Y��S�w���~�C\n");
							buf.append("\"<wear/wea> <�˳ƦW��>\"�i�H�������W�Y��˳ơC\n");
							buf.append("\"<open/o> <���W��>\"�i�H������}�ҬY��V�����C\n");
							buf.append("\"<close/cl> <���W��>\"�i�H�����������Y��V�����C");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("\"<unlock/un> <���W��>\"�i�H������Ѷ}�Y��V������C\n");
							buf.append("\"<lock/loc> <���W��>\"�i�H��������W�Y��V�����C\n");
							buf.append("\"<e/w/s/n/u/d>\"�i�H�����\"�F/��/�n/�_/�W/�U\"���ʡC\n");
							buf.append("\"<time/t>\"�i�H���A�d�ݹC�������ɶ��C\n");
							buf.append("\"<mission/m>\"�i�H���A�d�ݥثe���W�����ǥ��ȡA�H�α��U�Ӫ���V�C");
							EventUtil.informCheckReset(pg, buf, in);
							buf.append("�H�W�Ҧ������O�ҥi��J\"<help> <���O�W��>\"�Ӭd�߸ԲӪ�\n");
							buf.append("�����A��i�H������J\"help\"�Ӭd�ݥi�H�ϥΪ����O�����ǡC\n");
							buf.append("�C���������O�����Ȯɧi�@�q���A���۴N��������r�C���a���A\n");
							buf.append("���P���a!");
							EventUtil.informCheckReset(pg, buf, in);
						}
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					mm.setState(MainMission.State.START_SEARCHING);
					pg.setInEvent(false);
				}
			}
		});
		
		EventUtil.mapEventMap.put("100,92,1", new AbstractEvent(){
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
							.get(TwoDoorsMission.class.toString());
					if (tdm == null ||
							tdm.south == false)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerGroup pg = (PlayerGroup) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("�N�بӨ�J�٪������A�ݨ����䦳�@�D�۰ʪ��C�M�ӡA�����ѩ��_�q\n");
					buf.append("���t�G�A�ɭP�۰ʪ��L�k�}�ҡA�����}�Ҥ�V�]���w�Q�w������A��\n");
					buf.append("�ӥd���F�C");
					EventUtil.informCheckReset(pg, buf, in);
					buf.append("�N�جݵۦb�~���r�ު��ǥ͡A�ߤ��t�Q�G\"�گ�z�Ѭ�����n�����\n");
					buf.append("�_�ӤF...�p�G�q�o�̧�������}�A�~�����s�å뵴�ﰨ�W�]��L�ӡA\n");
					buf.append("�Q�q�o�̥X�h��M�ӦM�I�C\"");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.north == false){
						buf.append("�N�ءG\"�h�_�䪺���ݬݦ��S�����|�C\"");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informGroup(pg, "���L�@���C\n");
				}
				tdm.south = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					EventUtil.executeEventMessage(pg, "after_two_door");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,103,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
							.get(TwoDoorsMission.class.toString());
					if (tdm == null ||
							tdm.north == false)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerGroup pg = (PlayerGroup) g;
				
				TwoDoorsMission tdm = (TwoDoorsMission) PlayerServer.getMissionMap()
						.get(TwoDoorsMission.class.toString());
				if (tdm == null) {
					tdm = new TwoDoorsMission();
					PlayerServer.getMissionMap().put(TwoDoorsMission.class.toString(), tdm);
				} 
				
				BufferedReader in = pg.getInFromClient();
				StringBuffer buf = new StringBuffer();
				try {
					buf.append("\"�z��...\" �N�����ۦ��������V�r�ݡC\"�������ᦱ�ܧΡA\n");
					buf.append("��n�������]�h�줣���ܡA�o�ڥi�S��k�˶}��...\"");
					EventUtil.informCheckReset(pg, buf, in);
					if (tdm.south == false){
						buf.append("�N�ءG\"�h�n�䪺���ݬݦ��S�����|�C\"");
						g.getAtRoom().informRoom(buf.toString() + "\n");
					}
				} catch (SkipEventException e){
					CommandServer.informGroup(pg, "���L�@���C\n");
				}
				tdm.north = true;
				if (tdm.south && tdm.north){
					tdm.setState(TwoDoorsMission.State.DONE);
					EventUtil.executeEventMessage(pg, "after_two_door");
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(
							MainMission.class.toString());
					mm.setState(MainMission.State.FOUND_DOORS_BLOCKED);
				}
				g.setInEvent(false);
			}
			
		});
		
		EventUtil.mapEventMap.put("101,92,1", new AbstractEvent(){
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.FOUND_DOORS_BLOCKED)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap().get(MainMission.class.toString());
				if (mm.getState() == MainMission.State.FOUND_DOORS_BLOCKED){
					g.setInEvent(true);
					mm.setState(MainMission.State.BEFORE_BREAK_MANAGE_DOOR);
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						buf.append("�N�ءG�n��N�O�J�ٺ޲z�ǤF�A���L�ݨӤ]�O��۪��C\n");
						buf.append("��~���W�������A��ӨI���B��w�@�I���F�����ӥi�H�}�a�a!");
						EventUtil.informCheckReset(pg, buf, in);
						String[] msg = {"look", "s"};
						CommandServer.readCommand(pg, msg);
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�N�ءG�u�n�h���ݦ��S������i�Ϊ��u��F�C\n");
						g.getAtRoom().informRoom(buf.toString());
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("101,91,2", new AbstractEvent() {

			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					IMission m = PlayerServer.getMissionMap().get(ContainerTutorialMission.class.toString());
					if (m == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				if (pg.getConfigData().get(config.TUTORIAL_ON)){
					g.setInEvent(true);
					PlayerServer.getMissionMap().put(ContainerTutorialMission.class.toString(), 
							new ContainerTutorialMission());
					try {
						StringBuffer buf = new StringBuffer();
						BufferedReader in = pg.getInFromClient();
						buf.append("�N�ءG�O�J�٪��B�c�C�A���M���`�S����b�ΡA���{�b�o�ظرi���ɨ�\n");
						buf.append("�A�٬O�ݤ@�U�̭����S������i�H�Ϊ��F��a�C");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�����G�C������\"�e��\"�]�O���~���@�ءA�t�O�b�󥦯���ΨӲ���\n");
						buf.append("��L���~�C�e���b��ܮɷ|���K��ܥ������A��\"�}/��\"�C�A�@��\n");
						buf.append("�i�H�Ϊ��~���O�s�����A���P�ɤ]�����\"open/close/lock/unlock\"\n");
						buf.append("�ӹ復�i��ާ@�C");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("�{�b�յۿ�J\"open refrigerator\"�ӥ��}�B�c�G\n");
						String input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("open refrigerator")){
							g.getAtRoom().informRoom("�{�b�յۿ�J\"open refrigerator\"�ӥ��}�B�c�G\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg = {"open", "refrigerator"};
						CommandServer.readCommand(pg, msg);
						buf.append("�N�ءG�Ӭݬݸ̭����S������i�Ϊ��F��a!\n");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�����G�A�i�H�ϥ�\"look\"���O�Ӭd�ݤ@�Ӯe���������~�A�e���O�o�Ӯe��\n");
						buf.append("�O�}�۪��C���O�榡��\"<look> <in> <�e���W��>\"�C");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("�п�J\"look in refrigerator\"�Ӭd�ݦB�c�������Ǫ��~�G\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("look in refrigerator")){
							g.getAtRoom().informRoom("�п�J\"look in refrigerator\"�Ӭd�ݦB�c�������Ǫ��~�G\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg2 = {"look", "in", "refrigerator"};
						CommandServer.readCommand(pg, msg2);
						buf.append("�N�ءG���n�f���I���F�A�o�M�G�Ĭݰ_��...��F�A�ڽ䥦�S�a!");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�����G�A�i�H�ϥ�\"get\"���O�N���~�q�e�������X�ӡA���O�榡��\n");
						buf.append("\"<get> <���~�W��> <�e���W��>\"�A�t�~�]����ϥ�\"put\"���O\n");
						buf.append("�N���W�����~��J�e�����C���O�榡��\"<put> <���~�W��> <�e���W��>\"�C\n");
						buf.append("�Բӻ����i�H��J\"help get\"�H��\"help put\"�Ӭd�ߡC");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("�п�J\"get juice refrigerator\"�ӱq�B�c�����X�G�ġG\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("get juice refrigerator")){
							g.getAtRoom().informRoom("�п�J\"get juice refrigerator\"�ӱq�B�c�����X�G�ġG\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg3 = {"get", "juice", "refrigerator"};
						CommandServer.readCommand(pg, msg3);
						buf.append("�����G�A�i�H��J\"use\"���O�ӨϥάY�ǥi�ϥΪ����~�A���O�榡��\n");
						buf.append("\"<use> <���~�W��>\"��\"<use> <���~�W��> <�ؼЦW��>\"�C\n");
						buf.append("���~���ϥήĪG�����J\"<look> <���~�W��>\"���[�ݨ�C");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("�п�J\"use juice\"�ӳܪG�ġG\n");
						input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("use juice")){
							g.getAtRoom().informRoom("�п�J\"use juice\"�ӳܪG�ġG\n");
							input = IOUtil.readLineFromClientSocket(in);
						}
						String[] msg4 = {"use", "juice"};
						CommandServer.readCommand(pg, msg4);
						EventUtil.executeEventMessage(pg, "use_item_tutorial");
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					g.setInEvent(false);
				}
			}
			
		});
		
		EventUtil.mapEventMap.put("100,91,1", new AbstractEvent() {
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					FirstTimeSeeKeeperMission ftskm = (FirstTimeSeeKeeperMission) PlayerServer
							.getMissionMap().get(FirstTimeSeeKeeperMission.class.toString());
					if (ftskm == null) return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerServer.getMissionMap().put(FirstTimeSeeKeeperMission.class.toString(), 
						new FirstTimeSeeKeeperMission());
				EventUtil.executeEventMessage((PlayerGroup) g, "first time see keeper");
				Group gg = g.getAtRoom().getGroupList().findGroup("keeper");
				new DormKeeperBattle(gg, g);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("100,101,3", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_DEFEAT_MANAGER)
						return true;
				}
				return false;
			}

			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerGroup) g, "find_hydrualic_cut");
				((MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString())).setState(
								MainMission.State.AFTER_FOUND_CUTTER);
				g.setInEvent(false);
			}
		});
		
		EventUtil.mapEventMap.put("104,110,1", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					MainMission mm = (MainMission) PlayerServer.getMissionMap()
							.get(MainMission.class.toString());
					if (mm.getState() == MainMission.State.AFTER_EXIT_DORMITORY)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				EventUtil.executeEventMessage((PlayerGroup) g, "see_shadow");
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				mm.setState(MainMission.State.FIGHT_WITH_SHADOW);
				
				// create shadow group
				Group enemyG = new Group(new DarkShadow());
				enemyG.setIsRespawn(false);
				MapUtil.initializeGroupAtMap(enemyG, g.getAtRoom());
				new ShadowBattle(enemyG, g);
				g.setInEvent(false);
			}
			
		});
		
		EventUtil.mapEventMap.put("103,103,1", new AbstractEvent() {
			
			@Override
			public boolean isTriggered(Group g){
				if (super.isTriggered(g)){
					if (PlayerServer.getMissionMap().get(SoapMission.class.toString()) == null)
						return true;
				}
				return false;
			}
			
			@Override
			public void doEvent(Group g) {
				g.setInEvent(true);
				PlayerServer.getMissionMap().put(SoapMission.class.toString(), 
						new SoapMission());
				IRoom here = g.getAtRoom();
				here.getItemList().addItem(new Soap());
				EventUtil.executeEventMessage((PlayerGroup) g, "soap_event");
				Group enemyG = new Group(new MadStudent());
				enemyG.setIsRespawn(false);
				MapUtil.initializeGroupAtMap(enemyG, here);
				new BattleTask(enemyG, g);
				g.setInEvent(false);
			}
			
		});
	}
}
