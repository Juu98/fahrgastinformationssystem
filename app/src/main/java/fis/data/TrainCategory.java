package fis.data;

/**
 * Speichert eine bestimmte Zugkategorie
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
	 * @param description Beschreibung
	 * @param trainUsage Nutzung der Züge dieser Kategorie
	 */
	public TrainCategory(String id, String name, String description, String trainUsage){
		if(id==null || name==null || description==null || trainUsage==null) throw new NullPointerException();
		
		this.id=id;
		this.name=name;
		this.description=description;
		this.trainUsage=trainUsage;
	}
	
	/**
	 * Getter für id
	 * @return ID dieser Kategorie
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Getter für name
	 * @return Name dieser Zugkategorie
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Getter für description
	 * @return Beschreibung dieser Zugkategorie
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Getter für trainUsage
	 * @return Nutzung der Züge dieser Kategorie
	 */
	public String getTrainUsage(){
		return trainUsage;
	}
	
	/**
	 * Liefert menschenlesbare Reptäsentation der {@link TrainCategory}
	 * @return String der Form [RE] RegionalExpress
	 */
	@Override
	public String toString(){
		return String.format("[%s] %s", this.name, this.description);
	}
}
