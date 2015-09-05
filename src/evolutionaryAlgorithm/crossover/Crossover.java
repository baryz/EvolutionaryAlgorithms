package evolutionaryAlgorithm.crossover;

import evolutionaryAlgorithm.Chromosom;


public abstract class Crossover {
	protected CrossoverType type;
	protected int noOfCut;
	protected int[] positionOfCrossing;
	protected Chromosom firstParent;
	protected Chromosom secondParent;
	
	public  void  setParameters(int[] pointCrossing,Chromosom chromosom1,Chromosom chromosom2){
		if(chromosom1.getSize()!=chromosom2.getSize()) throw new IndexOutOfBoundsException();
		if(pointCrossing[0]>(chromosom1.getSize()-1)) throw new IndexOutOfBoundsException();
		
		positionOfCrossing=pointCrossing;
		firstParent=chromosom1;
		secondParent=chromosom2;
	}
	
	public void setNoOfCut(int noOfCut){
		this.noOfCut=noOfCut;
	}
	
	public  int getNoOfCut(){
		return this.noOfCut=noOfCut;
	}
	public abstract Chromosom[] getChromosomes(); 
	
	
}
