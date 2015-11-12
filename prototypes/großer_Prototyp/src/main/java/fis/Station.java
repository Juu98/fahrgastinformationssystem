package fis;

public class Station {
	private String name;
	private String id;
	
	public Station(String id, String name){
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
