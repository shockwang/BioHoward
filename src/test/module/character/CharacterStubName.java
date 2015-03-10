package test.module.character;

public class CharacterStubName extends CharacterStub{
	private String ChiName;
	private String EngName;
	
	@Override
	public String getEngName(){ return EngName; }
	@Override
	public String getChiName(){ return ChiName; }
	
	public CharacterStubName(String ChiName, String EngName){
		this.ChiName = ChiName;
		this.EngName = EngName;
	}
}
