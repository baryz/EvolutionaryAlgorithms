package evolutionaryAlgorithm.reproduction;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import config.Config;
import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.EvolutionaryAlgorithm;
import evolutionaryAlgorithm.Population;
import evolutionaryAlgorithm.crossover.CrossoverType;
import evolutionaryAlgorithm.stopCondition.StopConditionType;
import evolutionaryAlgorithm.succession.SuccessionType;
import graph.Graph;
import main.Main;




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
	
	

	public static void main( String args[] ){
		String graphName="keller4";
		Config conf= new Config();
		
	
        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,10,ReproductionType.TOURNAMENT,
        						CrossoverType.MULTI_POINT,20,
        						SuccessionType.SIMPLY,
        						StopConditionType.STAGNACY_CONDITION, 20,
        						conf,
        						10);   //quantity tournament
        
        
        alg.run();
        
	}
}
