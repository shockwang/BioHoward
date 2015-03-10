package module.character;

import java.io.DataOutputStream;
import java.util.HashMap;

import module.character.api.ICharacter;
import module.character.constants.CConfig.config;
import module.command.CommandServer;
import module.utility.EnDecoder;

public class PlayerGroup extends Group {
	private HashMap<config, Boolean> configData;
	private DataOutputStream outToClient = null;

	public PlayerGroup(ICharacter obj) {
		super(obj);
		initializeConfig();
	}

	private void initializeConfig() {
		configData = new HashMap<config, Boolean>();
		configData.put(config.REALTIMEBATTLE, false);
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
	}

	public HashMap<config, Boolean> getConfigData() {
		return this.configData;
	}

	public String showGroupStatus() {
		StringBuilder buffer = new StringBuilder();

		int count = 1;
		for (CharList cList : this.list) {
			for (ICharacter c : cList.charList) {
				buffer.append("\tNo." + count + " " + c.showStatus() + "\n");
				count++;
			}
		}
		return EnDecoder.encodeChangeLine(buffer.toString());
	}

	@Override
	public void updateTime() {
		// must not implement time waste instructions in this method!
		for (CharList cList : list) {
			for (ICharacter c : cList.charList) {
				c.updateTime();
			}
		}
		String out = "status:" + this.showGroupStatus();
		CommandServer.informGroup(this, out);
	}
}
