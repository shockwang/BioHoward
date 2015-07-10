package module.map;

import java.io.Serializable;

import module.map.constants.CExit.exit;


public class PositionDoor implements Serializable {
	public Position pos1;
	public exit way1;
	public Position pos2;
	public exit way2;
	
	public PositionDoor (Position pos1, exit way1, Position pos2, exit way2){
		this.pos1 = pos1;
		this.way1 = way1;
		this.pos2 = pos2;
		this.way2 = way2;
	}
}
