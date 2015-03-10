package module.server;

import java.io.DataOutputStream;
import java.net.Socket;

public class Player {
	private Socket connectionSocket = null;
	private String name = null;
	private DataOutputStream outToClient = null;
	
	public Player(String in, Socket in2, DataOutputStream in3){
		name = in;
		connectionSocket = in2;
		outToClient = in3;
	}
	
	public Socket getConnectionSocket(){ return connectionSocket;}
	
	public String getName(){ return name;}
	
	public DataOutputStream getOutToClient(){return outToClient;}
}
