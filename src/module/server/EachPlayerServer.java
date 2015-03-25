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
			//CommandServer.readCommand(playerGroup, "look".split(" "));

			while (PlayerServer.getServerRun()) {
				input = inFromClient.readLine();
				temp = input.split(" ");
				if (temp.length == 0)
					continue;
				
				CommandServer.readCommand(playerGroup, temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
