package fis;

public class Bahnhof {
	private String name;
	private String id;
	
	public Bahnhof(String id, String name){
		this.name=name;
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
}
