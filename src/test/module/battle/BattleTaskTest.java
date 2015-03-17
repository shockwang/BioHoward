package test.module.battle;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import module.battle.BattleTask;
import module.character.AbstractCharacter;
import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.character.constants.CStatus.status;
import module.client.ClientUser;
import module.command.CommandServer;
import module.item.AbstractItem;
import module.item.BaseEquipment;
import module.item.api.IEquipment.EquipType;
import module.map.api.IRoom;
import module.mission.TestMission;
import module.mission.TestMission.State;
import module.mission.api.IMission;
import module.server.PlayerServer;

import org.junit.Test;

import test.module.map.MapTest;

public class BattleTaskTest {
	private BattleTask task;
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	
	
	private class CharForTest extends AbstractCharacter{
		String desc = null;
		
		public CharForTest(String chiName, String engName) {
			super(chiName, engName);
		}

		@Override
		public boolean battleAction(GroupList enemyGroup) {
			String[] command = {this.getEngName(), "at", "tao"};
			CommandServer.readCommand(this.getMyGroup(), command);
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
		
	}
	
	private class ItemForTest extends AbstractItem{
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
			public String onTalk(PlayerGroup g) {
				TestMission testM = null;
				for (IMission m : PlayerServer.getMissionSet()){
					if (m instanceof TestMission){
						testM = (TestMission) m;
						break;
					}
				}
				StringBuffer buf = new StringBuffer();
				if (testM == null){
					CommandServer.informGroup(g, "�p�����G�ڤT�Ӥ�e�ɤF�p���@���s�ڤp���A�L�@���S���٧ڡA�A�i�H���ڨ��^�Ӷ�? (y/n)\n");
					String msg = null;
					while (true){
						try {
							msg = g.getInFromClient().readLine();
							if (msg.equals("n")){
								buf.append("�p���q�q�ӡG���N��F!");
								break;
							} else if (msg.equals("y")){
								buf.append("�p���ܰ��������G�Ӧn�F! �N�浹�A�o!");
								PlayerServer.getMissionSet().add(new TestMission());
								break;
							} else 
								CommandServer.informGroup(g, "�p�����G�ڤT�Ӥ�e�ɤF�p���@���s�ڤp���A�L�@���S���٧ڡA�A�i�H���ڨ��^�Ӷ�? (y/n)\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				return buf.toString();
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
		
		Group g2 = new PlayerGroup(new CharForTest("�ǩe", "tao"){
			@Override
			public boolean battleAction(GroupList gList){
				return false;
			}
		});
		g2.addChar(new CharForTest("���d", "kao"){
			@Override
			public boolean battleAction(GroupList gList){
				return false;
			}
		});
		for (CharList cList : g2.list){
			for (ICharacter c : cList.charList){
				c.setMyGroup(g2);
			}
		}
		
		Group g3 = new Group(new CharForTest("�p��", "mei"){
			@Override
			public String onTalk(PlayerGroup g){
				TestMission testM = null;
				for (IMission m : PlayerServer.getMissionSet()){
					if (m instanceof TestMission){
						testM = (TestMission) m;
					}
				}
				
				if (testM != null){
					TestMission.State state = (TestMission.State) testM.getState();
					if (state == TestMission.State.TALK_WITH_MING){
						testM.setState(TestMission.State.TALK_WITH_MEI);
						return "�ڡA�ڽT��@���ѰO�٥L�A���U�A�����L�o!";
					}
				}
				return "�p�����G�A�n��~";
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
		
		// setup player group inventory
		playerG.getInventory().addItem(new ItemForTest("�M�l", "cup", "�N�O�M�l�C"));
		playerG.getInventory().addItem(new ItemForTest("����", "tooth", "�N�O�����C"));
		playerG.getInventory().addItem(new ItemForTest("�M�l", "cup", "�N�O�M�l�C"));
		playerG.getInventory().addItem(new ItemForTest("�v�w�w", "anan", "���w�w"));
		playerG.getInventory().addItem(new ItemForTest("�Ӧw�w", "anan2", "���w�w"));
		playerG.getInventory().addItem(new ItemForTest("���w�w", "anan3", "���w�w"));
		playerG.getInventory().addItem(new ItemForTest("�p�w�w", "anan4", "���w�w"));
		
		// equipment test
		BaseEquipment testEquip = new BaseEquipment("���", "hand protect", EquipType.GLOVES);
		testEquip.setDescription("�N�O��ҡC");
		testEquip.setPrice(100);
		testEquip.getAttribute().put(attribute.HP, 10);
		testEquip.getStatus().put(status.WEAPON_ATTACK, 5);
		playerG.getInventory().addItem(testEquip);
		BaseEquipment testEquip2 = new BaseEquipment("���H�]�O�q��M", "opg", EquipType.GLOVES);
		testEquip2.setDescription("�ש_�����C");
		testEquip2.setPrice(100000);
		testEquip2.getStatus().put(status.WEAPON_ATTACK, 100000);
		playerG.getInventory().addItem(testEquip2);
		
		//task = new BattleTask(g1, g2);
		
		// add for map test, but use some code of BattleTaskTest
		MapTest map = new MapTest();
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
		start.getGroupList().gList.add(g3);
		// add end
		
		// add for GlobalTime test
		PlayerServer.getSystemTime().addGroup(g1);
		PlayerServer.getSystemTime().addGroup(g2);
		PlayerServer.getSystemTime().addGroup(g3);
		// add end
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
