package fis;

import java.util.ArrayList;
import java.util.List;

public class Zuglauf {
	private String id;
	private int trainNumber;
	private Zuggattung gattung;
	private List<Halt> halte=new ArrayList<Halt>();
	
	public Zuglauf(String id,int trainNumber, Zuggattung gattung, List<Halt> halte){
		this.id=id;
		this.trainNumber=trainNumber;
		this.gattung=gattung;
		this.halte=halte;
	}
	
	public String getId(){
		return id;
	}
	public int getTrainNumber(){
		return trainNumber;
	}
	
	
	
}
