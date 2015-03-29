package module.character.constants;

public class CConfig {
	public static enum config{
		REALTIMEBATTLE("即時戰鬥"),
		TUTORIAL_ON("開啟教學模式");
		
		public String chineseName;
		
		config(String name){
			this.chineseName = name;
		}
	}
}
