package fahrplan;

import java.util.ArrayList;
import java.util.List;

public class Zuglauf {
	int zugNr;
	Zuggattung gattung;
	List<Halt> halte=new ArrayList<Halt>();
	
	public Zuglauf(int zugNr, Zuggattung gattung, List<Halt> halte){
		this.zugNr=zugNr;
		this.gattung=gattung;
		this.halte=halte;
	}
	
	
}
