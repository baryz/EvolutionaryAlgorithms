package evolutionaryAlgorithm.crossover;

import evolutionaryAlgorithm.Chromosom;

public class OnePointCrossover extends Crossover {

	
	private int positionOfCrossing;
	private Chromosom firstParent;
	private Chromosom secondParent;

	
	public  OnePointCrossover() {
		
		
	}
	
	@Override
	public void setParameters(int[] pointCrossing,Chromosom chromosom1,Chromosom chromosom2){
		if(chromosom1.getSize()!=chromosom2.getSize()) throw new IndexOutOfBoundsException();
		if(pointCrossing[0]>(chromosom1.getSize()-1)) throw new IndexOutOfBoundsException();
		
		positionOfCrossing=pointCrossing[0];
		firstParent=chromosom1;
		secondParent=chromosom2;
		//firstOffspring = new Chromosom(chromosom1.getSize());
		//secondOffspring = new Chromosom(chromosom2.getSize());
	}
	
	@Override
	public Chromosom[] getChromosomes(){
		Chromosom[] result= new Chromosom[2];
		for(int i=0;i<result.length;i++) result[i] = new Chromosom(firstParent.getSize());
		
		for(int i=0;i<positionOfCrossing;i++){
			result[0].getGen(i).setValue(firstParent.getGen(i).getValue());
			result[0].getGen(i).setLabel(firstParent.getGen(i).getLabel());
			result[1].getGen(i).setValue(secondParent.getGen(i).getValue());
			result[1].getGen(i).setLabel(secondParent.getGen(i).getLabel());
		}
		for(int i=positionOfCrossing;i<firstParent.getSize();i++){
			result[0].getGen(i).setValue(secondParent.getGen(i).getValue());
			result[0].getGen(i).setLabel(secondParent.getGen(i).getLabel());
			result[1].getGen(i).setValue(firstParent.getGen(i).getValue());
			result[1].getGen(i).setLabel(firstParent.getGen(i).getLabel());
		}
		
		
		
		return result;
	}
	
	@Override
	public int getNoOfCut(){
		return 1;
	}

}
