package module.command;

import java.util.ArrayList;

import module.character.CharList;
import module.character.Group;
import module.character.PlayerGroup;
import module.character.api.ICharacter;
import module.character.constants.CConfig.config;
import module.command.api.ICommand;
import module.command.api.IndexStringPair;
import module.command.character.Attack;
import module.command.character.Close;
import module.command.character.Drop;
import module.command.character.Equipment;
import module.command.character.Flee;
import module.command.character.Get;
import module.command.character.Lock;
import module.command.character.Open;
import module.command.character.Remove;
import module.command.character.Unlock;
import module.command.character.Wear;
import module.command.group.Inventory;
import module.command.group.Look;
import module.command.group.Mission;
import module.command.group.Move;
import module.command.group.MyTime;
import module.command.group.Talk;
import module.utility.EnDecoder;
import module.utility.EventUtil;
import module.utility.Parse;

public class CommandServer {
	// class for all creatures in the game to interact with the world, all
	// methods should be static.
	// TODO: define the class
	private static ArrayList<ICommand> cmdList;
	private static ArrayList<ICommand> groupCmdList;

	public static void initialize() {
		// add all the available commands into the map
		cmdList = new ArrayList<ICommand>();
		groupCmdList = new ArrayList<ICommand>();

		cmdList.add(new Attack());
		cmdList.add(new Flee());
		cmdList.add(new Get());
		cmdList.add(new Drop());
		cmdList.add(new Equipment());
		cmdList.add(new Wear());
		cmdList.add(new Remove());
		cmdList.add(new Open());
		cmdList.add(new Close());
		cmdList.add(new Lock());
		cmdList.add(new Unlock());

		groupCmdList.add(new Move());
		groupCmdList.add(new Look());
		groupCmdList.add(new MyTime());
		groupCmdList.add(new Talk());
		groupCmdList.add(new Inventory());
		groupCmdList.add(new Mission());
	}

	public static void readCommand(Group g, String[] msg) {
		// execute room special command
		if (EventUtil.doRoomCommand(g, msg)) return;

		if (msg[0].equals("help")) {
			if (msg.length == 1) {
				// show the top help page
				informGroup(g, "Top help page.\n");
				informGroup(g, showTopHelpPage());
				return;
			}
			String output = null;
			try {
				ICommand target = searchCommand(msg[1], cmdList);
				if (target == null)
					target = searchCommand(msg[1], groupCmdList);
				output = target.getHelp();
				if (output == null)
					output = "尚未實作該指令的簡介。\n";
			} catch (IndexOutOfBoundsException e) {
				output = "你想查詢什麼指令?\n";
			} catch (NullPointerException e) {
				output = "沒有針對這個指令的介紹.\n";
			} finally {
				informGroup(g, output);
			}
			return;
		}

		// check group command first
		try {
			ICommand targetCmd = searchCommand(msg[0], groupCmdList);
			targetCmd.action(g.list.get(0).charList.get(0), msg);
			return;
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		} catch (NullPointerException e) {
			// do nothing
		}

		// then deal with character command, if no char name assigned,
		// the first group character will be chosen.
		ICharacter targetChar = judgePlayerCharacterMove(g, msg);
		if (targetChar == null) {
			targetChar = g.list.get(0).charList.get(0);
			String[] temp = new String[msg.length + 1];
			temp[0] = targetChar.getEngName();
			for (int i = 0; i < msg.length; i++)
				temp[i + 1] = msg[i];
			msg = temp;
		}
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
				ICommand targetCmd = searchCommand(msg[1], cmdList);
				boolean movedInBattle = targetCmd.action(targetChar, msg);
				if (movedInBattle && g.getInBattle()) {
					// character has done its action, update the battle
					// timer
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
		}
	}

	private static ICommand searchCommand(String msg, ArrayList<ICommand> list) {
		for (ICommand cmd : list) {
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
			System.out.print(g.getChiName() + "看見: " + msg);
		}
	}

	private static ICharacter judgePlayerCharacterMove(Group g, String[] input) {
		try {
			int index = Integer.parseInt(input[0]);
			int count = 1;
			for (CharList cList : g.list) {
				for (ICharacter c : cList.charList) {
					if (count == index)
						return c;
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

	private static String showTopHelpPage() {
		StringBuffer buf = new StringBuffer();
		buf.append("團體指令：\n");

		buf.append("north\tsouth\teast\twest\tup\ndown\t");
		int index = 1;
		while (index < groupCmdList.size()) {
			buf.append(groupCmdList.get(index).getName()[0] + "\t");
			index++;
			if (index % 5 == 0)
				buf.append("\n");
		}
		if (index % 5 != 0)
			buf.append("\n");

		buf.append("個人指令：\n");

		index = 0;
		while (index < cmdList.size()) {
			buf.append(cmdList.get(index).getName()[0] + "\t");
			index++;
			if (index % 5 == 0)
				buf.append("\n");
		}
		if (index % 5 != 0)
			buf.append("\n");

		return buf.toString();
	}
}
