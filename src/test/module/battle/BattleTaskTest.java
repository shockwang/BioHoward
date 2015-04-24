package test.module.battle;

import static org.junit.Assert.assertTrue;
import module.battle.BattleTask;
import module.character.BaseHumanCharacter;
import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.character.instance.chapter0.Roommate;
import module.client.ClientUser;
import module.event.map.instance.chapter0.YiDormitoryEvent;
import module.item.AbstractItem;
import module.item.BaseEquipment;
import module.item.api.IEquipment.EquipType;
import module.map.api.IRoom;
import module.mission.TestMission;
import module.mission.api.IMission;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;
import module.utility.MapUtil;
import module.utility.NpcBattleActionUtil;

import org.junit.Test;

public class BattleTaskTest {
	private BattleTask task;
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	
	
	public class CharForTest extends BaseHumanCharacter{
		String desc = null;
		
		public CharForTest(String chiName, String engName) {
			super(chiName, engName);
			int ddd = PlayerServer.getRandom().nextInt(10);
			ddd *= 100;
			this.statusMap.put(status.SPEED, 2000 + ddd);
		}

		@Override
		public boolean battleAction(GroupList enemyGroup) {
			NpcBattleActionUtil.randomAttack(this, enemyGroup);
			return true;
		}

		@Override
		public void setDesc(String description) {
			this.desc = description;
		}

		@Override
		public String getDesc() {
			return desc;
		}
		
		@Override
		public void looting(){
			boolean drop = PlayerServer.getRandom().nextBoolean();
			if (drop) this.getMyGroup().getInventory().addItem(
					new ItemForTest("����", "tooth", "�N�O�����C"));
		}
		
	}
	
	public class ItemForTest extends AbstractItem{
		public ItemForTest(String chiName, String engName, String description) {
			super(chiName, engName);
			this.setDescription(description);
		}
	}
	
	@Test
	public void npcVsNpcTest(){
		// code from ServerClientTest
		singletonServer = new PlayerServer();
		singletonServer.setPort(12312);
		singletonServer.start();
		
		oneUser = new ClientUser();
		assertTrue(oneUser.connectToServer("localhost", 12312));
		oneUser.start();
		// code end
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		Group g1 = new Group(new CharForTest("�p��", "min"){
			
			@Override
			public void onTalk(PlayerGroup g) {
				TestMission testM = null;
				testM = (TestMission) PlayerServer.getMissionMap().get(TestMission.class.toString());
				StringBuffer buf = new StringBuffer();
				if (testM == null){
					g.getAtRoom().informRoom("�p�����G�ڤT�Ӥ�e�ɤF�p���@���s�ڤp���A�L�@���S���٧ڡA�A�i�H���ڨ��^�Ӷ�? (y/n)\n");
					String msg = null;
					while (true){
						msg = IOUtil.readLineFromClientSocket(g.getInFromClient());
						if (msg.equals("n")){
							buf.append("�p���q�q�ӡG���N��F!");
							break;
						} else if (msg.equals("y")){
							buf.append("�p���ܰ��������G�Ӧn�F! �N�浹�A�o!");
							IMission missionToAdd = new TestMission();
							PlayerServer.getMissionMap().put(TestMission.class.toString(), missionToAdd);
							break;
						} else 
							g.getAtRoom().informRoom("�p�����G�ڤT�Ӥ�e�ɤF�p���@���s�ڤp���A�L�@���S���٧ڡA�A�i�H���ڨ��^�Ӷ�? (y/n)\n");
					}
				} else {
					TestMission.State state = (TestMission.State) testM.getState();
					switch (state){
					case TALK_WITH_MING:
						buf.append("�p�����ۻ��G�N���U�A�F!");
						break;
					case TALK_WITH_MEI:
						buf.append("�p���G�ڡA���§A! �ڤ@���Q���s�^���s�ڤp���������O!");
						testM.setState(TestMission.State.DONE);
						break;
					case DONE:
						buf.append("�p���M�ߪ��ݵۤp���A�S�Ųz�A�C");
					}
				}
				g.getAtRoom().informRoom(buf.toString() + "\n");
			}
		});
		g1.addChar(new CharForTest("�p��", "hua"));
		for (CharList cList : g1.list){
			for (ICharacter c : cList.charList){
				c.setMyGroup(g1);
			}
		}
		g1.findChar("min", 0).addAttribute(attribute.HP, 50);
		g1.findChar("min").setDesc("�N�O�p���C");
		g1.findChar("hua", 0).addAttribute(attribute.HP, 50);
		g1.findChar("hua").setDesc("�N�O�p�ءC");
		
		Group g2 = new PlayerGroup(new CharForTest("�N��", "enf"){
			@Override
			public boolean battleAction(GroupList gList){
				return false;
			}
		});
		for (CharList cList : g2.list){
			for (ICharacter c : cList.charList){
				c.setMyGroup(g2);
				c.setHostile(false);
			}
		}
		
		Group g3 = new Group(new CharForTest("�p��", "mei"){
			@Override
			public void onTalk(PlayerGroup g){
				String result = null;
				
				TestMission testM = (TestMission) PlayerServer.getMissionMap().get(
						TestMission.class.toString());
				
				if (testM != null){
					TestMission.State state = (TestMission.State) testM.getState();
					if (state == TestMission.State.TALK_WITH_MING){
						testM.setState(TestMission.State.TALK_WITH_MEI);
						result = "�ڡA�ڽT��@���ѰO�٥L�A���U�A�����L�o!";
					}
					else result = "�p�����G�A�n��~";
				}
				else result = "�p�����G�A�n��~";
				g.getAtRoom().informRoom(result + "\n");
			}
		});
		g3.findChar("mei").addAttribute(attribute.HP, 30);
		g3.findChar("mei").setMyGroup(g3);
		g3.findChar("mei").setDesc("�N�O�p���C");
		
		PlayerServer.pList.get(0).setGroup((PlayerGroup) g2);
		PlayerGroup playerG = (PlayerGroup) g2;
		playerG.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		playerG.setInFromClient(PlayerServer.pList.get(0).getInFromClient());
		//playerG.getConfigData().put(config.REALTIMEBATTLE, true);
		
		// equipment test
		BaseEquipment testEquip = new BaseEquipment("�����ҥ�", "physics book", EquipType.WEAPON);
		testEquip.setDescription("�S�p�S���C");
		testEquip.setPrice(100);
		testEquip.getStatus().put(status.WEAPON_ATTACK, 10);
		playerG.getInventory().addItem(testEquip);
		
		//task = new BattleTask(g1, g2);
		
		// add for map test, but use some code of BattleTaskTest
		/*MapTest map = new MapTest();
		map.initialize();
		IRoom start = map.getStart();
		g1.setAtRoom(start);
		g1.setInitialRoom(start);
		g2.setAtRoom(start);
		g2.setInitialRoom(start);
		g3.setAtRoom(start);
		g3.setInitialRoom(start);
		start.getGroupList().gList.add(g1);
		start.getGroupList().gList.add(g2);
		start.getGroupList().gList.add(g3);*/
		// add end
		
		// MoveUtil test
		MapUtil.parseMapFromJSON("map/chapter0/YiDormitory.map");
		MapUtil.parseDoorFromJSON("map/chapter0/YiDormitory.door");
		YiDormitoryEvent.initialize();
		IRoom start = MapUtil.roomMap.get("102,100,3");
		g2.setAtRoom(start);
		g2.setInitialRoom(start);
		start.getGroupList().gList.add(g2);
		
		// add roommate
		Group ggg = new Group(new Roommate());
		for (CharList cList : ggg.list){
			for (ICharacter c : cList.charList)
				c.setMyGroup(ggg);
		}
		// setup player group inventory
		ggg.getInventory().addItem(new ItemForTest("�q�N306���Ъ��_��", "key 306", "�N�O�_��"));
		ggg.setIsRespawn(false);
		ggg.setAtRoom(start);
		ggg.setInitialRoom(start);
		start.getGroupList().gList.add(ggg);
		
		// add for GlobalTime test
		//PlayerServer.getSystemTime().addGroup(g1);
		PlayerServer.getSystemTime().addGroup(g2);
		PlayerServer.getSystemTime().addGroup(ggg);
		//PlayerServer.getSystemTime().addGroup(g3);
		// add end
		
		// start event test
		g2.setInEvent(true);
		EventUtil.doRoomEvent(g2);
		
		try {
			while (true) {
				Thread.sleep(1000000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
