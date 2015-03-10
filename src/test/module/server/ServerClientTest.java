package test.module.server;

import static org.junit.Assert.*;
import module.client.ClientUser;
import module.server.PlayerServer;

import org.junit.Test;

public class ServerClientTest {
	private PlayerServer singletonServer;
	private ClientUser oneUser;
	
	@Test
	public void serverClientTest(){
		singletonServer = new PlayerServer();
		singletonServer.setPort(12312);
		singletonServer.start();
		
		oneUser = new ClientUser();
		assertTrue(oneUser.connectToServer("localhost", 12312));
		oneUser.start();
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
