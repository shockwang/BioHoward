package module.event.map.instance.chapter0;

import module.character.Group;
import module.command.CommandServer;
import module.event.AbstractEvent;
import module.utility.EventUtil;

public class YiDormitoryEvent {
	public static void initialize(){
		EventUtil.mapEventMap.put("101,100,3", new AbstractEvent(){
			@Override
			public void doEvent(Group g) {
				CommandServer.informGroup(g, "你聽到一聲哇哈哈~\n");
			}
		});
	}
}
