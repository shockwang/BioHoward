package test.module.battle;

import static org.junit.Assert.assertTrue;
import module.battle.BattleTask;
import module.character.BaseCharacter;
import module.character.CharList;
import module.character.Group;
import module.character.GroupList;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CAttribute.attribute;
import module.client.ClientUser;
import module.command.CommandServer;
import module.map.api.IRoom;
import module.server.PlayerServer;

import org.junit.Test;

import test.module.map.MapTest;

public class BattleTaskTest {
	private BattleTask task;
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	
	
	private class CharForTest extends BaseCharacter{
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
		
		Group g1 = new Group(new CharForTest("小明", "min"));
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
			}
		}
		
		Group g3 = new Group(new CharForTest("小美", "mei"));
		g3.findChar("mei").addAttribute(attribute.HP, 30);
		g3.findChar("mei").setMyGroup(g3);
		g3.findChar("mei").setDesc("就是小美。");
		
		PlayerServer.pList.get(0).setGroup((PlayerGroup) g2);
		PlayerGroup playerG = (PlayerGroup) g2;
		playerG.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		//playerG.getConfigData().put(config.REALTIMEBATTLE, true);
		
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
