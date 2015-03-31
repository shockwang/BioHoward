package module.event.map.instance.chapter0;

import java.io.BufferedReader;

import module.character.Group;
import module.character.PlayerGroup;
import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.event.map.SkipEventException;
import module.mission.chapter0.MainMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("101,100,3", new AbstractEvent(){
			@Override
			public void doEvent(Group g) {
				CommandServer.informGroup(g, "�Ať��@�n�z����~\n");
			}
		});
		
		EventUtil.mapEventMap.put("102,100,3", new AbstractEvent(){

			@Override
			public void doEvent(Group g) {
				PlayerGroup pg = (PlayerGroup) g;
				
				MainMission mm = (MainMission) PlayerServer.getMissionMap()
						.get(MainMission.class.toString());
				if (mm == null) {
					mm = new MainMission();
					PlayerServer.getMissionMap().put(MainMission.class.toString(), mm);
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						buf.append("�}�Y�@���ԭz ...");
						EventUtil.informCheckReset(g, buf, in);
						
					} catch (SkipEventException e){
						CommandServer.informGroup(pg, "���L�@���C\n");
					}
					mm.setState(MainMission.State.AFTER_OPENING);
					
					try {
						BufferedReader in = pg.getInFromClient();
						StringBuffer buf = new StringBuffer();
						buf.append("�N�ءG��c...�n�_�Ǫ��ڡC");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�N�ؤ߷Q�G�i�}�����ݬݩж��a.\n");
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"look\"���O��Ψ��[��A�{�b�ҳB���ж����A�A�]�A�b������L����Ϊ�\n");
							buf.append("�����b�ӳ������a�W���~������ݨ�C��i²�g��\"l\"�C�Բӻ����i��J\n");
							buf.append("\"help look\"�Ӭd�ߡC");
							EventUtil.informCheckReset(pg, buf, in);
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
						buf.append("�N�جݨ�Ǥͤ@�ʤ��ʪ��A�U�h�s�L�C");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G�b�D�԰�������ϥ�\"talk\"���O�өM�P�@�ж����������͡A�榡���G\n");
							buf.append("\"<talk> <����W��>\"�άO\"<talk> <����W��> <����W��>\"\n");
							buf.append("�Ǥͥثe�ëD�s�b�h�H����A�]���ϥ�\"talk roommate\"�ӻP�L��͡C\n");
							buf.append("�i��J\"help talk\"�Ө��o�惡���O���Բӻ����C");
							EventUtil.informCheckReset(pg, buf, in);
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
					BufferedReader in = pg.getInFromClient();
					StringBuffer buf = new StringBuffer();
					try {
						buf.append("�N�ءG�I~�ש�F���F�A���o�ǮլO���^��?");
						EventUtil.informCheckReset(pg, buf, in);
						buf.append("�N�جݦa�W���_�͡A�ǳƾ߰_�ӡC");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"get\"���O�i�H���A�߰_�����b�a�W���F��A���O�榡���G\n");
							buf.append("\"<�ڤ訤��W��> <get> <���~�W��>\"�C�P���۹諸�A\"drop\"���O\n");
							buf.append("�i�H���A��U���W�����~�A���O�榡��\"<�ڤ訤��W��> <drop> <���~�W��>\"�C\n");
							buf.append("�Y�����w�ڤ訤��A�h�۰ʿ�ܶ�����Ĥ@�H�i��ʧ@�C�t�~�A�b�԰���get/drop\n");
							buf.append("�Ҫ�O�@�^�X����ʡC�Բӻ����i��J\"help get\"�H��\"help drop\"�Ӭd�ߡC");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"get key\"�Ӿ߰_�J���_�͡C\n");
							String input = IOUtil.readLineFromClientSocket(in);
							while (!input.equals("get key")){
								g.getAtRoom().informRoom("�п�J\"get key\"�Ӿ߰_�J���_�͡C\n");
								input = IOUtil.readLineFromClientSocket(in);
							}
						}
						String[] msg = {"get", "key"};
						CommandServer.readCommand(pg, msg);
						buf.append("�N�ءG���}���X�h�ݬݦn�F�C");
						EventUtil.informCheckReset(pg, buf, in);
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
						buf.append("�N�ءG���}���X�h�ݬݧa�C");
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
						buf.append("�N�ءG����n�����I�M�I�A��������W�𮧤@�U�a�C");
						EventUtil.informCheckReset(pg, buf, in);
						g.getAtRoom().informRoom("�п�J\"close w\"�����W�J�٩ж������C\n");
						String input = IOUtil.readLineFromClientSocket(in);
						while (!input.equals("close w")){
							g.getAtRoom().informRoom("�п�J\"close w\"�����W�J�٩ж������C\n");
							input = IOUtil.readLineFromClientSocket(in);
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
						buf.append("�N�ءF�n~��~�𮧰��F����N�X�o�����a!");
						EventUtil.informCheckReset(pg, buf, in);
						if (pg.getConfigData().get(config.TUTORIAL_ON)){
							buf.append("�����G\"mission\"��\"m\"���O������A�d�ݦۤv�ثe���W�����ǥ���\n");
							buf.append("�H�α��U�ӥi�H�Ĩ����Ǧ�ʡC�b�C���i��ɦp�G�ѰO�ۤv���U�ӭn������A\n");
							buf.append("�o�|�O�@�ӫܦn�Ϊ����O��!");
							EventUtil.informCheckReset(pg, buf, in);
							g.getAtRoom().informRoom("�п�J\"mission\"��\"m\"�Ӭd�ݷ�e���ȡC\n");
							input = IOUtil.readLineFromClientSocket(in);
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
					String[] msg6 = {"look"};
					CommandServer.readCommand(pg, msg6);
				} else if (mm.getState() == MainMission.State.START_SEARCHING){
					// TODO: define actions after battle
					g.getAtRoom().informRoom("start searching~~~\n");
				}
			}
		});
	}
}
