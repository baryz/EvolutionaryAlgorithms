package evolutionaryAlgorithm.crossover;

import evolutionaryAlgorithm.Chromosom;

public class OnePointCrossover extends Crossover {

	
	
	public  OnePointCrossover() {
		
		
	}

	
	@Override
	public Chromosom[] getChromosomes(){
		Chromosom[] result= new Chromosom[2];
		for(int i=0;i<result.length;i++) result[i] = new Chromosom(firstParent.getSize());
		
		for(int i=0;i<positionOfCrossing[0];i++){
			result[0].getGen(i).setValue(firstParent.getGen(i).getValue());
			result[0].getGen(i).setLabel(firstParent.getGen(i).getLabel());
			result[1].getGen(i).setValue(secondParent.getGen(i).getValue());
			result[1].getGen(i).setLabel(secondParent.getGen(i).getLabel());
		}
		for(int i=positionOfCrossing[0];i<firstParent.getSize();i++){
			result[0].getGen(i).setValue(secondParent.getGen(i).getValue());
			result[0].getGen(i).setLabel(secondParent.getGen(i).getLabel());
			result[1].getGen(i).setValue(firstParent.getGen(i).getValue());
			result[1].getGen(i).setLabel(firstParent.getGen(i).getLabel());
		}
		
		
		
		return result;
	}
	


}
