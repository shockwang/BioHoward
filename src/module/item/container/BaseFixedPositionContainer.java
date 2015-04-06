package module.item.container;

public class BaseFixedPositionContainer extends BaseContainer{

	public BaseFixedPositionContainer(String chiName, String engName) {
		super(chiName, engName);
		this.type = Type.FIXED_POSITION;
	}
	
	@Override
	public boolean isExpired(){
		return false; // always not expire
	}
}
