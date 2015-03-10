package module.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import module.command.CommandServer;
import module.time.GlobalTime;

public class PlayerServer extends Thread{
	private static boolean serverRun = true;
	private static boolean isExists = false;
	private Socket connectionSocket = null;
	public static List<EachPlayerServer> pList;
	private static GlobalTime systemTime = null;
	private static Random rand = null;
	private int port;
	
	public PlayerServer(){
		if (isExists) return;
		isExists = true;
		
		pList = Collections.synchronizedList(new ArrayList<EachPlayerServer>());
		CommandServer.initialize();
		
		systemTime = new GlobalTime();
		systemTime.startTimer();
		
		rand = new Random();
	}
	
	public void setServerRun(boolean input){
		serverRun = input;
	}
	
	public void setPort(int inPort){
		port = inPort;
	}
	
	public void run(){
		ServerSocket listenSocket = null;
		EachPlayerServer reference;
		
		try {
			listenSocket = new ServerSocket(port);
			while (serverRun){
				connectionSocket = listenSocket.accept();
				reference = new EachPlayerServer(connectionSocket);
				reference.start();
				pList.add(reference);
			}
		} catch (IOException e){
			System.out.println("Can't bind to port - already in use.");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				listenSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean getServerRun() {return serverRun;}
	
	public static GlobalTime getSystemTime(){return systemTime;}
	
	public static Random getRandom(){return rand;}
}
