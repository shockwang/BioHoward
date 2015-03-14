package module.item.api;

public interface IItem {
	// TODO: add class definition
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
}
