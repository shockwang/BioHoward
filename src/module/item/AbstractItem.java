package module.item;

import module.item.api.IItem;
import module.map.api.IRoom;

public abstract class AbstractItem implements IItem{
	private String chiName;
	private String engName;
	private String description;
	private int price = 1;
	private int level = 1;
	private int weight = 1;
	private int ttl = 0;
	private IRoom atRoom = null;
	
	public AbstractItem(String chiName, String engName){
		this.chiName = chiName;
		this.engName = engName;
	}
	
	@Override
	public void setChiName(String name){
		this.chiName = name;
	}
	
	@Override
	public String getChiName(){
		return chiName;
	}
	
	@Override
	public void setEngName(String name){
		this.engName = name;
	}
	
	@Override
	public String getEngName(){
		return engName;
	}
	
	@Override
	public void setDescription(String description){
		this.description = description;
	}
	
	@Override
	public String getDescription(){
		return description;
	}
	
	// item price
	@Override
	public void setPrice(int price){
		this.price = price;
	}
	
	@Override
	public int getPrice(){
		return price;
	}
	
	@Override
	public void setLevel(int level){
		this.level = level;
	}
	
	@Override
	public int getLevel(){
		return level;
	}
	
	@Override
	public void setWeight(int weight){
		this.weight = weight;
	}
	
	@Override
	public int getWeight(){
		return weight;
	}
	
	@Override
	public void setTTL(int ttl){
		this.ttl = ttl;
	}
	
	@Override
	public void updateTTL(int ttl){
		this.ttl += ttl;
	}
	
	@Override
	public boolean isExpired(){
		if (this.ttl > 600) return true;
		return false;
	}
	
	@Override
	public void setAtRoom(IRoom r){
		this.atRoom = r;
	}
	
	@Override
	public IRoom getAtRoom(){
		return atRoom;
	}
	
	// display information
	
	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(String.format("%s  (%d$)\n", chiName, price));
		buf.append(this.description + "\n");
		return buf.toString();
	}
	
	@Override
	public boolean isStackable() {
		// default not stackable, only food/medicine is stackable
		return false;
	}
}
