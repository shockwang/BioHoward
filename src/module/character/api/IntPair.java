package module.character.api;

import java.io.Serializable;

public class IntPair implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2742366528973863621L;
	
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
