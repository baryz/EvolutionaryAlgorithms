package evolutionaryAlgorithm.crossover;

import java.util.Arrays;

import evolutionaryAlgorithm.Chromosom;

public class MultiCrossover extends Crossover {
	
	@Override
	public Chromosom[] getChromosomes() {
		Chromosom[] result= new Chromosom[2];
		for(int i=0;i<result.length;i++) {
			result[i] = new Chromosom(firstParent.getSize());
		}
		
		int startCutPosition=0;
		int endCutPosition=positionOfCrossing[0];
		boolean getFirstParentFlag=true;
		//enlargment positionOfCrossig about a last position in chromosom
		positionOfCrossing=Arrays.copyOf(positionOfCrossing, positionOfCrossing.length+1);
		positionOfCrossing[positionOfCrossing.length-1]=firstParent.getSize();
		
		for(int i=0;i<positionOfCrossing.length;i++){
			for(int j=startCutPosition;j<endCutPosition;j++){
				if(getFirstParentFlag){
					result[0].getGen(j).setValue(firstParent.getGen(j).getValue());
					result[0].getGen(j).setLabel(firstParent.getGen(j).getLabel());
					result[1].getGen(j).setValue(secondParent.getGen(j).getValue());
					result[1].getGen(j).setLabel(secondParent.getGen(j).getLabel());
				}else{
					result[0].getGen(j).setValue(secondParent.getGen(j).getValue());
					result[0].getGen(j).setLabel(secondParent.getGen(j).getLabel());
					result[1].getGen(j).setValue(firstParent.getGen(j).getValue());
					result[1].getGen(j).setLabel(firstParent.getGen(j).getLabel());
				}

			}
			getFirstParentFlag=!getFirstParentFlag;
			startCutPosition=positionOfCrossing[i];
			if(i+1>positionOfCrossing.length-1) break;
			endCutPosition=positionOfCrossing[i+1];
			
			
		}
		
		
		
		return result;
	}

}
