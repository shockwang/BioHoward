package module.command;

import java.util.ArrayList;

import module.character.CharList;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CConfig.config;
import module.command.api.Command;
import module.command.api.IndexStringPair;
import module.command.character.Attack;
import module.command.character.Flee;
import module.command.group.Look;
import module.command.group.Move;
import module.command.group.MyTime;
import module.command.group.Talk;
import module.utility.EnDecoder;
import module.utility.Parse;

public class CommandServer {
	// class for all creatures in the game to interact with the world, all
	// methods should be static.
	// TODO: define the class
	private static ArrayList<Command> cmdList;
	private static ArrayList<Command> groupCmdList;

	public static void initialize() {
		// add all the available commands into the map
		cmdList = new ArrayList<Command>();
		groupCmdList = new ArrayList<Command>();

		cmdList.add(new Attack());
		cmdList.add(new Flee());

		groupCmdList.add(new Move());
		groupCmdList.add(new Look());
		groupCmdList.add(new MyTime());
		groupCmdList.add(new Talk());
	}

	public static void readCommand(Group g, String[] msg) {
		if (msg[0].equals("help")) {
			if (msg.length == 1) {
				// show the top help page
				informGroup(g, "Top help page.\n");
				return;
			}
			String output = null;
			try {
				Command target = searchCommand(msg[1], cmdList);
				if (target == null)
					target = searchCommand(msg[1], groupCmdList);
				output = target.getHelp();
			} catch (IndexOutOfBoundsException e) {
				output = "你想查詢什麼指令?\n";
			} catch (NullPointerException e) {
				output = "沒有針對這個指令的介紹.\n";
			} finally {
				informGroup(g, output);
			}
			return;
		}

		ICharacter targetChar = judgePlayerCharacterMove(g, msg);
		if (targetChar != null) {
			// character-bonded action, use cmdList
			if (g.getInBattle()) {
				// group is in battle
				int current, max;
				synchronized (g.getBattleTask().getTimeMap()) {
					current = g.getBattleTask().getTimeMap().get(targetChar)
							.getCurrent();
					max = g.getBattleTask().getTimeMap().get(targetChar)
							.getMax();
				}
				if (current < max) {
					informGroup(g, "你選擇的對象還沒準備好.\n");
					return;
				}

			}
			try {
				Command targetCmd = searchCommand(msg[1], cmdList);
				boolean movedInBattle = targetCmd.action(targetChar, msg);
				if (movedInBattle && g.getInBattle()) {
					// character has done its action, update the battle timer
					synchronized (g.getBattleTask()) {
						if (g instanceof PlayerGroup) {
							PlayerGroup pg = (PlayerGroup) g;
							if (pg.getConfigData().get(config.REALTIMEBATTLE) == false)
								pg.getBattleTask().notify();
						}
						if (g.getBattleTask() != null)
							g.getBattleTask().resetBattleTime(targetChar);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				informGroup(g,
						String.format("你想讓%s做什麼呢?\n", targetChar.getChiName()));
			} catch (NullPointerException e) {
				e.printStackTrace();
				informGroup(g,
						String.format("你想讓%s做什麼呢?\n", targetChar.getChiName()));
			}
			return;
		} else {
			// group-bonded action, use groupCmdList
			try {
				Command targetCmd = searchCommand(msg[0], groupCmdList);
				targetCmd.action(g.list.get(0).charList.get(0), msg);
			} catch (IndexOutOfBoundsException e) {
				informGroup(g, "你想做什麼?\n");
			} catch (NullPointerException e) {
				e.printStackTrace();
				informGroup(g, "你想做什麼?\n");
			}
		}
		/*
		 * Command target = searchCommand(msg[0]); if (target == null){
		 * informGroup(g, "你想做什麼呢?\n"); return; } String in =
		 * Parse.mergeString(msg, 1, ' '); target.action(g, in);
		 */
	}

	private static Command searchCommand(String msg, ArrayList<Command> list) {
		for (Command cmd : list) {
			for (String name : cmd.getName()) {
				if (name.equals(msg))
					return cmd;
			}
		}
		return null;
	}

	public static void informGroup(Group g, String msg) {
		if (g instanceof PlayerGroup) {
			PlayerGroup g2 = (PlayerGroup) g;
			EnDecoder.sendUTF8Packet(g2.getOutToClient(), msg);
		} else {
			// inform the NPC group about something
			// do nothing first
			System.out.println(g.getChiName() + "看見: " + msg);
		}
	}

	private static ICharacter judgePlayerCharacterMove(Group g, String[] input) {
		try {
			int index = Integer.parseInt(input[0]);
			int count = 1;
			for (CharList cList : g.list){
				for (ICharacter c : cList.charList){
					if (count == index) return c;
					count++;
				}
			}
			return null;
		} catch (Exception e) {
			ICharacter target = null;
			IndexStringPair pair = Parse.parseName(input[0]);
			target = g.findChar(pair.name, pair.index);
			return target;
		}
	}
}
