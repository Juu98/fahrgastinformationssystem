package fis.data;

public class TrainCategory {
	private String id;
	private String name;
	private String description;
	private String trainUsage;
	
	public TrainCategory(String id, String name, String description, String trainUsage){
		this.id=id;
		this.name=name;
		this.description=description;
		this.trainUsage=trainUsage;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getTrainUsage(){
		return trainUsage;
	}
	
}
