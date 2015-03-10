package test.module.display;

import module.display.api.IDisplay;

public class DisplayStub implements IDisplay{

	@Override
	public void display(String message) {
		System.out.print(message);
	}

}
