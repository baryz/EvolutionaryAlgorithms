package evolutionaryAlgorithm.stopCondition;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.Statistic;

public class StagnacyCondition extends StopCondition {
	
	private int stopLimit;
	private int globalBestFitnes;
	private Statistic currentStat;
	
	public StagnacyCondition(int NoOfIterateWithoutImprovement) {
		type = StopConditionType.STAGNACY_CONDITION;
		stopLimit=NoOfIterateWithoutImprovement;
	}
	
	public void setParameter(int currentBest, Statistic currentStatistic){
		globalBestFitnes = currentBest;
		currentStat = currentStatistic;
	}
	
	@Override
	public boolean isContinue() {
		
		int noOfIterateWhenFindCurBestChrom=0;
		int noIterateWithoutImprovement=0;
		int[] fitTable = currentStat.getBestChromBaseList();
		for(int i=0; i < currentStat.getNoOfPopulation(); i++){
			if( globalBestFitnes == fitTable[i]){
				noOfIterateWhenFindCurBestChrom=i;
				break;
			}
		}
		noIterateWithoutImprovement =  (currentStat.getNoOfPopulation() - 1) - noOfIterateWhenFindCurBestChrom;
		//System.out.println("BEST CHROM FIT: " + globalBestChromosom.getFitnes());
		//System.out.println("currentStat.getNoOfPopulation() : " + currentStat.getNoOfPopulation());
		//System.out.println("noOfIterateWhenFindCurBestChrom : " + noOfIterateWhenFindCurBestChrom);
		System.out.println("noIterateWithoutImprovement: " + noIterateWithoutImprovement + " stopCOnd: "+ (noIterateWithoutImprovement<stopLimit) );
		return noIterateWithoutImprovement<stopLimit;
	}
}
