package module.command;

import java.util.ArrayList;

import module.character.PlayerCharacter;
import module.character.api.ICharacter;
import module.command.api.ICommand;
import module.command.character.Attack;
import module.command.character.Close;
import module.command.character.Drop;
import module.command.character.Equipment;
import module.command.character.Flee;
import module.command.character.Get;
import module.command.character.Inventory;
import module.command.character.Lock;
import module.command.character.Look;
import module.command.character.Mission;
import module.command.character.Move;
import module.command.character.MyTime;
import module.command.character.Open;
import module.command.character.Put;
import module.command.character.Quit;
import module.command.character.Remove;
import module.command.character.Say;
import module.command.character.Talk;
import module.command.character.Unlock;
import module.command.character.Use;
import module.command.character.Wear;
import module.utility.EnDecoder;
import module.utility.EventUtil;

public class CommandServer {
	// class for all creatures in the game to interact with the world, all
	// methods should be static.
	private static ArrayList<ICommand> cmdList;

	public static void initialize() {
		// add all the available commands into the map
		cmdList = new ArrayList<ICommand>();

		//cmdList.add(new Attack());
		//cmdList.add(new Flee());
		cmdList.add(new Get());
		cmdList.add(new Drop());
		cmdList.add(new Equipment());
		cmdList.add(new Wear());
		cmdList.add(new Remove());
		cmdList.add(new Open());
		cmdList.add(new Close());
		cmdList.add(new Lock());
		cmdList.add(new Unlock());
		cmdList.add(new Put());
		//cmdList.add(new Use());

		cmdList.add(new Move());
		cmdList.add(new Look());
		cmdList.add(new MyTime());
		cmdList.add(new Talk());
		cmdList.add(new Inventory());
		cmdList.add(new Mission());
		cmdList.add(new Quit());
		
		cmdList.add(new Say());
	}

	public static void readCommand(ICharacter c, String[] msg) {
		// execute room special command
		if (EventUtil.doRoomCommand(c, msg)) return;

		if (msg[0].equals("help")) {
			if (msg.length == 1) {
				// show the top help page
				informCharacter(c, "Top help page.\n");
				informCharacter(c, showTopHelpPage());
				return;
			}
			String output = null;
			try {
				ICommand target = searchCommand(msg[1], cmdList);
				output = target.getHelp();
				if (output == null)
					output = "尚未實作該指令的簡介。\n";
			} catch (IndexOutOfBoundsException e) {
				output = "你想查詢什麼指令?\n";
			} catch (NullPointerException e) {
				output = "沒有針對這個指令的介紹.\n";
			} finally {
				informCharacter(c, output);
			}
			return;
		}

		// check command
		try {
			ICommand targetCmd = searchCommand(msg[0], cmdList);
			boolean act = targetCmd.action(c, msg);
			if (act && c.getInBattle()) {
				// TODO: add energy cost
			}
			return;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			// do nothing
		} catch (NullPointerException e) {
			e.printStackTrace();
			// do nothing
		}
		
		informCharacter(c, String.format("你想做什麼?\n"));
		return;
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

	public static void informCharacter(ICharacter c, String msg) {
		String replacedMsg = msg.replace(c.getChiName(), "你");
		if (c instanceof PlayerCharacter) {
			PlayerCharacter pc = (PlayerCharacter) c;
			EnDecoder.sendUTF8Packet(pc.getOutToClient(), replacedMsg);
		} else {
			// inform the NPC group about something
			// do nothing first
			// TODO: remove npc see mechanism, maybe add debug information?
			System.out.print(c.getEngName() + "看見: " + replacedMsg);
		}
	}
	
	public static void informCharacterNoChange(ICharacter c, String msg) {
		if (c instanceof PlayerCharacter) {
			PlayerCharacter pc = (PlayerCharacter) c;
			EnDecoder.sendUTF8Packet(pc.getOutToClient(), msg);
		} else {
			// inform the NPC group about something
			// do nothing first
			// TODO: remove npc see mechanism, maybe add debug information?
			System.out.print(c.getEngName() + "看見: " + msg);
		}
	}

	private static String showTopHelpPage() {
		StringBuffer buf = new StringBuffer();
		buf.append("指令列表：\n");

		buf.append("north\tsouth\teast\twest\tup\ndown\t");
		int index = 1;
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
