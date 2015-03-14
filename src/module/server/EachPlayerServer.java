package module.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import module.character.PlayerGroup;
import module.command.CommandServer;

public class EachPlayerServer extends Thread {
	private Socket connectionSocket = null;
	private BufferedReader inFromClient = null;
	private DataOutputStream outToClient = null;
	private PlayerGroup playerGroup = null;

	public EachPlayerServer(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void setGroup(PlayerGroup in) {
		this.playerGroup = in;
	}

	public PlayerGroup getGroup() {
		return this.playerGroup;
	}

	public DataOutputStream getOutToClient() {
		return this.outToClient;
	}
	
	public BufferedReader getInFromClient(){
		return this.inFromClient;
	}

	public void run() {
		
		String input;
		String[] temp;
		String out;

		try {
			inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream(), "UTF-8"));
			outToClient = new DataOutputStream(connectionSocket
					.getOutputStream());
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			CommandServer.informGroup(playerGroup, "status:" + playerGroup.showGroupStatus());
			CommandServer.readCommand(playerGroup, "look".split(" "));

			while (PlayerServer.getServerRun()) {
				input = inFromClient.readLine();
				temp = input.split(" ");
				if (temp.length == 0)
					continue;
				// shock add for CommandServerTest
				CommandServer.readCommand(playerGroup, temp);
				// shock add end
				
				// shock add for BattleTaskTest
				/*if (playerGroup.getInBattle()) {
					ICharacter target = judgePlayerCharacterMove(temp);
					if (target != null) {
						EnDecoder.sendUTF8Packet(outToClient, target.getChiName() + "°Ê§@¤F!\n");
						if (playerGroup.getConfigData().get(
								config.REALTIMEBATTLE) == false) {
							synchronized (playerGroup.getBattleTask()) {
								playerGroup.getBattleTask().notify();
							}
						}
						playerGroup.getBattleTask().resetBattleTime(target);
					}
				}*/
				// shock add end
				/*
				 * temp = input.split(" "); if (temp.length == 0) out =
				 * "screen:"; else if (temp[0].equals("status")){ out =
				 * "status:" + Parse.mergeString(temp, 1, ' '); } else out =
				 * "screen:" + input; outToClient.writeBytes(out + "\n");
				 */
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
