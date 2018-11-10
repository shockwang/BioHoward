package module.item.api;

import java.io.Serializable;

import module.map.api.IRoom;

public interface IItem extends Serializable{
	// name & description
	void setChiName(String name);
	String getChiName();
	void setEngName(String name);
	String getEngName();
	void setDescription(String description);
	String getDescription();
	
	// item price
	void setPrice(int price);
	int getPrice();
	
	// display information
	String display();
	
	// item level
	void setLevel(int level);
	int getLevel();
	
	// weight
	void setWeight(int weight);
	int getWeight();
	
	// check if stackable
	boolean isStackable();
	
	// expire mechanism on the ground
	void setTTL(int ttl);
	void updateTTL(int ttl);
	boolean isExpired();
	void setAtRoom(IRoom r);
	IRoom getAtRoom();
}
