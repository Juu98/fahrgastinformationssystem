package fahrplan;

import java.util.ArrayList;
import java.util.List;

//"Rohe" Fahrplandaten
public class FahrplanData {
	private List<Zuglauf> zuglaeufe;
	
	public FahrplanData(){
		zuglaeufe=new ArrayList<Zuglauf>();
	}
	
	public void addZuglauf(Zuglauf zuglauf){
		zuglaeufe.add(zuglauf);
	}
	
	public List<Zuglauf> getZuglaeufe(){
		return zuglaeufe;
	}
}
