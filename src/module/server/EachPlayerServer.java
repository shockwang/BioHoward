package module.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import module.character.PlayerGroup;
import module.command.CommandServer;
import module.utility.IOUtil;

public class EachPlayerServer extends Thread {
	private Socket connectionSocket = null;
	private BufferedReader inFromClient = null;
	private DataOutputStream outToClient = null;
	private PlayerGroup playerGroup = null;
	
	public Thread thisThread = null;

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
			
			thisThread = Thread.currentThread();
			playerGroup.thisServer = this;
			CommandServer.informGroup(playerGroup, "status:" + playerGroup.showGroupStatus());
			//CommandServer.readCommand(playerGroup, "look".split(" "));
			
			// keep the last input msg
			String[] lastInput = {""};
			
			while (PlayerServer.getServerRun()) {
				if (playerGroup.getInEvent()) continue;
				
				// rollback the message read if group start the event
				input = IOUtil.readLineFromClientSocket(inFromClient);
				if (playerGroup.getInEvent()){
					// notify another thread wait on inFromClient if any
					synchronized (inFromClient){
						inFromClient.notify();
					}
					continue;
				} 
				temp = input.split(" ");
				if (temp.length == 0)
					continue;
				else if (input.equals(""))
					continue;
				
				if (temp[0].equals("!"))
					temp = lastInput;
				
				lastInput = temp;
				CommandServer.readCommand(playerGroup, temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
