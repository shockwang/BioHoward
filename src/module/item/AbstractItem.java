package module.item;

import module.item.api.IItem;

public abstract class AbstractItem implements IItem{
	private String chiName;
	private String engName;
	private String description;
	private int price;
	private int level;
	
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
	
	// display information
	
	@Override
	public String display(){
		StringBuffer buf = new StringBuffer();
		buf.append(String.format("%s  (%d$)\n", chiName, price));
		buf.append(this.description + "\n");
		return buf.toString();
	}
}
