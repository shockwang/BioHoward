package module.character;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.util.HashMap;

import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.server.EachPlayerServer;
import module.utility.EnDecoder;

public class PlayerCharacter extends BaseHumanCharacter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7836729144223571863L;
	
	private HashMap<config, Boolean> configData;
	// TODO: build a new connection when loading the file
	private volatile DataOutputStream outToClient = null;
	private volatile BufferedReader inFromClient = null;
	public volatile EachPlayerServer thisServer = null;
	
	private boolean inEvent = false;
	
	public PlayerCharacter(String chiName, String engName) {
		super(chiName, engName);
		this.setHostile(false);
		initializeConfig();
		inEvent = false;
	}

	private void initializeConfig() {
		configData = new HashMap<config, Boolean>();
		configData.put(config.REALTIMEBATTLE, false);
		configData.put(config.TUTORIAL_ON, true);
	}

	public void setOutToClient(DataOutputStream out) {
		this.outToClient = out;
	}

	public DataOutputStream getOutToClient() {
		return this.outToClient;
	}

	public void setConfigData(config newConfig, Object value) {
		// so far we only have 1 config in the list
		if (newConfig == config.REALTIMEBATTLE)
			configData.put(config.REALTIMEBATTLE, (Boolean) value);
		else if (newConfig == config.TUTORIAL_ON)
			configData.put(config.TUTORIAL_ON, (Boolean) value);
	}

	public HashMap<config, Boolean> getConfigData() {
		return this.configData;
	}

	public String showGroupStatus() {
		StringBuilder buffer = new StringBuilder();

		int count = 1;
		buffer.append("\tNo." + count + " " + this.showStatus() + "\n");
		return EnDecoder.encodeChangeLine(buffer.toString());
	}

	@Override
	public void updateTime() {
		// must not implement time waste instructions in this method!
		String out = "status:" + this.showGroupStatus();
		CommandServer.informCharacter(this, out);
	}
	
	public void setInFromClient(BufferedReader in){
		this.inFromClient = in;
	}
	
	public BufferedReader getInFromClient(){
		return this.inFromClient;
	}
	
	public void setInEvent(boolean value) {
		this.inEvent = value;
	}
	
	public boolean getInEvent() {
		return this.inEvent;
	}
}
