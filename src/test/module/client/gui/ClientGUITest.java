package test.module.client.gui;

import module.client.gui.ClientGUI;

import org.junit.Test;

public class ClientGUITest {
	@Test
	public void createTest(){
		new ClientGUI();
		new ClientGUI();
		new ClientGUI();
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e){}
	}
}
