package module.map;

import java.io.Serializable;

public class Position implements Serializable{
	public int x;
	public int y;
	public int z;
	
	public Position(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString(){
		return String.format("%d,%d,%d", x, y, z);
	}
}
