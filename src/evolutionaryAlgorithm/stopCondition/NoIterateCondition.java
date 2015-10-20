package evolutionaryAlgorithm.stopCondition;

import evolutionaryAlgorithm.Statistic;

public class NoIterateCondition extends StopCondition {

	private int noIterate;
	private Statistic currentStat;
	
	public  NoIterateCondition(int noIterate) {
		type = StopConditionType.NO_ITERATE;
		this.noIterate = noIterate;
	}
	
	public void setParameter( Statistic currentStatistic){
		currentStat = currentStatistic;
	}
	
	@Override
	public int getParam(){
		return noIterate;
	}
	
	@Override
	public boolean isContinue() {
		int currentNoOfIterate = currentStat.getNoOfPopulation()-1; 
		return currentNoOfIterate < noIterate;
	}
	

}
