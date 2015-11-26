package fis.data;

/**
 * Stores a specific TrainCategory
 * @author Luux
 */
public class TrainCategory {
	private String id;
	private String name;
	private String description;
	private String trainUsage;
	
	/**
	 * 
	 * @param id ID
	 * @param name Name
	 * @param description Description
	 * @param trainUsage Usage of the trains in this category
	 */
	public TrainCategory(String id, String name, String description, String trainUsage){
		this.id=id;
		this.name=name;
		this.description=description;
		this.trainUsage=trainUsage;
	}
	
	/**
	 * Getter for id
	 * @return ID of this TrainCategory
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Getter for name
	 * @return Name of this TrainCategory
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter for description
	 * @return Description of this TrainCategory
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Getter for trainUsage
	 * @return Usage of the trains in this Category
	 */
	public String getTrainUsage(){
		return trainUsage;
	}
	
}
