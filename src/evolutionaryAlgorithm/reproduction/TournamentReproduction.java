package evolutionaryAlgorithm.reproduction;


import java.util.ArrayList;
import java.util.Random;
import evolutionaryAlgorithm.Population;




public class TournamentReproduction extends Reproduction {
	
	private int quantityOfTour;
	private final int default_quantity = 2;
	
	public TournamentReproduction(int[] quantityTournament) {
		this.quantityOfTour = quantityTournament.length > 0 ? quantityTournament[0] : default_quantity;
	}

	@Override
	public ArrayList<Integer> getPairForCrossing(Population inPop) {
		
		ArrayList<Integer> resultList = new ArrayList<>();
		ArrayList<Integer> bestFitnesInTourIndexList = new ArrayList<>();
		Random randEng = new Random();
		
		for(int i=0; i<inPop.getSizePopulation(); i++){
			int maxFitnesValue = 0;
			bestFitnesInTourIndexList.clear();
			for(int j=0; j<quantityOfTour; j++){
				int randomIndex = randEng.nextInt(inPop.getSizePopulation());
				int fitnesRandomIndex = inPop.getChromosom(randomIndex).getFitnes();
				if(  fitnesRandomIndex > maxFitnesValue){
					bestFitnesInTourIndexList.clear();
					 maxFitnesValue =  fitnesRandomIndex;
					 bestFitnesInTourIndexList.add(randomIndex);
				}else if ( fitnesRandomIndex == maxFitnesValue){
					bestFitnesInTourIndexList.add(randomIndex);
				}
			}
			
			if( bestFitnesInTourIndexList.size() == 1){
				resultList.add(bestFitnesInTourIndexList.get(0));
			}else{
				int randomInt = randEng.nextInt(bestFitnesInTourIndexList.size());
				resultList.add(bestFitnesInTourIndexList.get(randomInt));
			}
		}
		
		
		
		return resultList;
	}

}
