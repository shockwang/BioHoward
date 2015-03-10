package module.time;

import java.util.Timer;
import java.util.TimerTask;

import module.character.Group;
import module.character.GroupList;

public class GlobalTime extends TimerTask{
	private Timer timer = null;
	public static boolean isExists = false;
	private GroupList groupList = null;
	private int year, month, day, hour, minute;
	
	public GlobalTime(){
		if (isExists) return;
		isExists = true;
		
		groupList = new GroupList();
		year = 2008;
		month = 9;
		day = 9;
		hour = 9;
		minute = 9;
	}
	
	public void addGroup(Group g){
		this.groupList.gList.add(g);
	}
	
	public void removeGroup(Group g){
		this.groupList.gList.remove(g);
	}
	
	public void startTimer(){
		timer = new Timer();
		timer.schedule(this, 0, 3000);
	}
	
	public String getTime(){
		return String.format("西元%d年%d月%d日%d時%d分", year, month, day, hour, minute);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		updateTime();
		updateObject();
	}
	
	private void updateTime(){
		minute++;
		if (minute >= 60){
			minute = 0;
			hour++;
		}
		if (hour > 24) {
			hour = 24 - hour;
			day++;
		}
		
		int dayMax = 0;
		switch (month){
		case 1: dayMax = 31; break;
		case 2: dayMax = 28; break;
		case 3: dayMax = 31; break;
		case 4: dayMax = 30; break;
		case 5: dayMax = 31; break;
		case 6: dayMax = 30; break;
		case 7: dayMax = 31; break;
		case 8: dayMax = 31; break;
		case 9: dayMax = 30; break;
		case 10: dayMax = 31; break;
		case 11: dayMax = 30; break;
		case 12: dayMax = 31; break;
		default: month = 9; day = 9; dayMax = 31; break;
		}
		if (day > dayMax){
			day = dayMax - day;
			month++;
		}
		if (month > 12){
			month = month - 12;
			year++;
		}
	}
	
	private void updateObject(){
		try {
			for (Group g : groupList.gList) {
				if (!g.getInBattle()) g.updateTime();
			}
		} catch (NullPointerException e){
			// no content, just return
			e.printStackTrace();
		}
	}
}
