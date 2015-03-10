package module.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.text.Document;

import module.client.gui.ClientGUI;
import module.utility.EnDecoder;
import module.utility.Parse;

public class ClientUser extends Thread {
	private static boolean isExists = false;
	private ClientGUI gui;
	private Socket connect = null;
	private BufferedReader inFromServer = null;
	private boolean clientRun = true;

	public ClientUser() {
		if (isExists)
			return;
		isExists = true;

		gui = new ClientGUI();
	}

	public boolean connectToServer(String ip, int port) {
		InetAddress serverIP = null;
		Socket temp = null;

		try { // parse IP
			serverIP = InetAddress.getByName(ip);
			temp = new Socket(serverIP, port);
			gui.setOutToServer(new DataOutputStream(temp.getOutputStream()));
			if (connect != null)
				connect.close();
			connect = temp;
		} catch (UnknownHostException e) {
			System.out.println("Client: unknown host exception");
			return false;
		} catch (SocketException e) {
			System.out.println("Client: socket exception");
			return false;
		} catch (IOException e) {
			System.out.println("Client: IO exception");
			return false;
		}

		return true;
	}

	public void run() {
		try {
			inFromServer = new BufferedReader(new InputStreamReader(
					connect.getInputStream(), "UTF-8"));
			String input = null;
			String[] temp;

			while (clientRun) {
				input = inFromServer.readLine();
				input = EnDecoder.decodeChangeLine(input);
				temp = input.split(":");
				if (temp[0].equals("status")) { // messages to refresh the
												// status
					String out = Parse.mergeString(temp, 1, ':');
					gui.getStatusPanel().setText("");
					gui.getStatusPanel().setText(out);
				} else { // message to display on the screen
					synchronized (ClientGUI.class) {
						gui.getScreenPanel().append(input + "\n");
						Document doc = gui.getScreenPanel().getDocument();
						gui.getScreenPanel().setCaretPosition(doc.getLength());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} finally {
			try {
				inFromServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// shock add for BattleTaskTest
	public ClientGUI getGUI() {
		return gui;
	}
	// shock add end
}
