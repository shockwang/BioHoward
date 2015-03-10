package module.character.api;

public class IntPair {
	private int first;
	private int second;
	
	public IntPair(int x, int y){
		this.first = x;
		this.second = y;
	}
	
	public void setMax(int x) {
		second = x;
	}
	
	public int getMax(){
		return second;
	}
	
	public void setCurrent(int x){
		first = x;
	}
	
	public int getCurrent(){
		return first;
	}
}
