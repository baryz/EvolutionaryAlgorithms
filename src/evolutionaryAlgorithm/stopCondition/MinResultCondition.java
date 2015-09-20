package evolutionaryAlgorithm.stopCondition;



public class MinResultCondition extends StopCondition {

	private int minAcceptResult;
	private int currentBestFitness;
	public MinResultCondition(int minResult) {
		type = StopConditionType.MIN_RESULT;
		minAcceptResult = minResult;
	}	
	
	public void setParameter(int bestChromosomFitnes){
		currentBestFitness = bestChromosomFitnes;
	}
	@Override
	public boolean isContinue() {
		 
		return currentBestFitness < minAcceptResult;
	}

}
