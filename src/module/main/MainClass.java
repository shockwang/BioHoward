package module.main;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;

import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CConfig.config;
import module.character.instance.main.Enf;
import module.client.ClientUser;
import module.command.CommandServer;
import module.event.map.instance.chapter0.YiDormitoryEvent;
import module.event.map.instance.chapter0.YiDormitoryRoomCommand;
import module.item.BaseEquipment;
import module.item.instance.chapter0.PhysicsBook;
import module.map.api.IRoom;
import module.server.PlayerServer;
import module.utility.EventUtil;
import module.utility.IOUtil;
import module.utility.MapUtil;

public class MainClass {
	private static PlayerServer singletonServer;
	private static ClientUser oneUser;
	private static PlayerGroup pg = null;

	public static void main(String[] args) {
		// do the game initialize here

		// game initialize end
		initialize();

		BaseEquipment testEquip = new PhysicsBook();
		pg.getInventory().addItem(testEquip);

		// set player group start position
		IRoom start = MapUtil.roomMap.get("102,100,3");
		pg.setAtRoom(start);
		pg.setInitialRoom(start);
		start.getGroupList().gList.add(pg);
		
		// start game setup
		startGameSetup();

		// set player group to system time
		PlayerServer.getSystemTime().addGroup(pg);

		// execute the starting event
		EventUtil.doRoomEvent(pg);
	}

	private static void initialize() {
		singletonServer = new PlayerServer();
		singletonServer.setPort(12312);
		singletonServer.start();

		oneUser = new ClientUser();
		assertTrue(oneUser.connectToServer("localhost", 12312));
		oneUser.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ICharacter enf = new Enf();
		pg = new PlayerGroup(enf);

		PlayerServer.pList.get(0).setGroup(pg);
		pg.setOutToClient(PlayerServer.pList.get(0).getOutToClient());
		pg.setInFromClient(PlayerServer.pList.get(0).getInFromClient());

		// map initialize
		MapUtil.parseMapFromJSON("resources/map/chapter0/YiDormitory.map");
		MapUtil.parseDoorFromJSON("resources/map/chapter0/YiDormitory.door");
		YiDormitoryEvent.initialize();
		YiDormitoryRoomCommand.initialize();

		// npc initialize
		MapUtil.parseNpcFromJSON("resources/map/chapter0/YiDormitory.npc");

		// item initialize
		MapUtil.parseItemFromJSON("resources/map/chapter0/YiDormitory.item");
		
		// event message initialize
		EventUtil.parseEventFromJSON("resources/event/chapter0/YiDormitory.event");
	}

	private static void startGameSetup() {
		// do initialize here
		EventUtil.parseEventFromJSON("resources/event/main.event");
		DataOutputStream outToClient = PlayerServer.pList.get(0)
				.getOutToClient();
		BufferedReader in = PlayerServer.pList.get(0).getInFromClient();

		pg.setInEvent(true);
		EventUtil.showMessageToClient(outToClient, "starting_game");
		IOUtil.readLineFromClientSocket(in);

		String output = "是否開啟教學模式? <y/n> (建議不熟悉操作方式的玩家選y)";
		CommandServer.informGroup(pg, output + "\n");
		String input = IOUtil.readLineFromClientSocket(in);
		while (!input.equals("y") && !input.equals("n")) {
			CommandServer.informGroup(pg, output + "\n");
			input = IOUtil.readLineFromClientSocket(in);
		}
		if (input.equals("y"))
			pg.setConfigData(config.TUTORIAL_ON, true);
		else
			pg.setConfigData(config.TUTORIAL_ON, false);

		output = "是否開啟即時戰鬥? <y/n> (建議不熟悉操作方式的玩家選n)\n";
		CommandServer.informGroup(pg, output);
		input = IOUtil.readLineFromClientSocket(in);
		while (!input.equals("y") && !input.equals("n")) {
			CommandServer.informGroup(pg, output + "\n");
			input = IOUtil.readLineFromClientSocket(in);
		}

		if (input.equals("y"))
			pg.setConfigData(config.REALTIMEBATTLE, true);
		else
			pg.setConfigData(config.REALTIMEBATTLE, false);

		// execute opening
		EventUtil.executeEventMessage(pg, "opening");
	}
}
