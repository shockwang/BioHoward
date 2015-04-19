package module.item.container.instance.chapter0;

import module.item.container.BaseFixedPositionContainer;
import module.item.instance.chapter0.Cake;
import module.item.instance.chapter0.Juice;

public class Refrigerator extends BaseFixedPositionContainer {
	
	public Refrigerator(){
		this("冰箱", "refrigerator");
		StringBuffer buf = new StringBuffer();
		buf.append("義齋宿舍的冰箱，學生們把各種食物飲料擺在裡面，會不會定期清理則是另外\n");
		buf.append("一回事。除了可能變成插電的垃圾桶之外，也可能發生自己的東西被別人吃掉\n");
		buf.append("的窘況。現在反正是非常時期，偷拿一點食物也是可以被原諒的吧?");
		this.setDescription(buf.toString());
		this.list.addItem(new Cake());
		this.list.addItem(new Juice());
	}
	
	public Refrigerator(String chiName, String engName) {
		super(chiName, engName);
	}

}
