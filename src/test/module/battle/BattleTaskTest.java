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
import module.map.Position;
import module.map.api.IRoom;
import module.mission.TestMission;
import module.mission.TestMission.State;
import module.mission.api.IMission;
import module.server.PlayerServer;
import module.utility.MapUtil;
import module.utility.NpcBattleActionUtil;

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
					new ItemForTest("牙齒", "tooth", "就是牙齒。"));
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
		
		Group g1 = new Group(new CharForTest("小明", "min"){
			
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
					g.getAtRoom().informRoom("小明說：我三個月前借了小美一本龍族小說，他一直沒有還我，你可以幫我取回來嗎? (y/n)\n");
					String msg = null;
					while (true){
						try {
							msg = g.getInFromClient().readLine();
							if (msg.equals("n")){
								buf.append("小明聳聳肩：那就算了!");
								break;
							} else if (msg.equals("y")){
								buf.append("小明很高興的說：太好了! 就交給你囉!");
								PlayerServer.getMissionSet().add(new TestMission());
								break;
							} else 
								g.getAtRoom().informRoom("小明說：我三個月前借了小美一本龍族小說，他一直沒有還我，你可以幫我取回來嗎? (y/n)\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					TestMission.State state = (TestMission.State) testM.getState();
					switch (state){
					case TALK_WITH_MING:
						buf.append("小明笑著說：就拜託你了!");
						break;
					case TALK_WITH_MEI:
						buf.append("小明：啊，謝謝你! 我一直想重新回味龍族小說的奧秘呢!");
						testM.setState(TestMission.State.DONE);
						break;
					case DONE:
						buf.append("小明專心的看著小說，沒空理你。");
					}
				}
				return buf.toString();
			}
		});
		g1.addChar(new CharForTest("小華", "hua"));
		for (CharList cList : g1.list){
			for (ICharacter c : cList.charList){
				c.setMyGroup(g1);
			}
		}
		g1.findChar("min", 0).addAttribute(attribute.HP, 50);
		g1.findChar("min").setDesc("就是小明。");
		g1.findChar("hua", 0).addAttribute(attribute.HP, 50);
		g1.findChar("hua").setDesc("就是小華。");
		
		Group g2 = new PlayerGroup(new CharForTest("掏委", "tao"){
			@Override
			public boolean battleAction(GroupList gList){
				return false;
			}
		});
		g2.addChar(new CharForTest("高粱", "kao"){
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
		
		Group g3 = new Group(new CharForTest("小美", "mei"){
			@Override
			public String onTalk(PlayerGroup g){
				String result = null;
				
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
						result = "啊，我確實一直忘記還他，拜託你拿給他囉!";
					}
					else result = "小美說：你好啊~";
				}
				else result = "小美說：你好啊~";
				return result;
			}
		});
		g3.findChar("mei").addAttribute(attribute.HP, 30);
		g3.findChar("mei").setMyGroup(g3);
		g3.findChar("mei").setDesc("就是小美。");
		
		PlayerServer.pList.get(0).setGroup((PlayerGroup) g2);
		PlayerGroup playerG = (PlayerGroup) g2;
		playerG.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		playerG.setInFromClient(PlayerServer.pList.get(0).getInFromClient());
		//playerG.getConfigData().put(config.REALTIMEBATTLE, true);
		
		// setup player group inventory
		playerG.getInventory().addItem(new ItemForTest("杯子", "cup", "就是杯子。"));
		playerG.getInventory().addItem(new ItemForTest("牙齒", "tooth", "就是牙齒。"));
		playerG.getInventory().addItem(new ItemForTest("杯子", "cup", "就是杯子。"));
		playerG.getInventory().addItem(new ItemForTest("率安安", "anan", "蠢安安"));
		playerG.getInventory().addItem(new ItemForTest("帥安安", "anan2", "蠢安安"));
		playerG.getInventory().addItem(new ItemForTest("蠢安安", "anan3", "蠢安安"));
		playerG.getInventory().addItem(new ItemForTest("小安安", "anan4", "蠢安安"));
		
		// equipment test
		BaseEquipment testEquip = new BaseEquipment("手甲", "hand protect", EquipType.GLOVES);
		testEquip.setDescription("就是手甲。");
		testEquip.setPrice(100);
		testEquip.getAttribute().put(attribute.HP, 10);
		testEquip.getStatus().put(status.WEAPON_ATTACK, 5);
		playerG.getInventory().addItem(testEquip);
		BaseEquipment testEquip2 = new BaseEquipment("食人魔力量手套", "opg", EquipType.GLOVES);
		testEquip2.setDescription("修奇戴的。");
		testEquip2.setPrice(100000);
		testEquip2.getStatus().put(status.WEAPON_ATTACK, 100000);
		playerG.getInventory().addItem(testEquip2);
		
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
		MapUtil.parseMapFromJSON("jsonTest.txt");
		MapUtil.parseDoorFromJSON("jsonDoorTest.txt");
		IRoom start = MapUtil.searchRoomByPosition(new Position(100,100,0));
		g2.setAtRoom(start);
		g2.setInitialRoom(start);
		start.getGroupList().gList.add(g2);
		
		// add for GlobalTime test
		//PlayerServer.getSystemTime().addGroup(g1);
		PlayerServer.getSystemTime().addGroup(g2);
		//PlayerServer.getSystemTime().addGroup(g3);
		// add end
		
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
