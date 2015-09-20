package evolutionaryAlgorithm.stopCondition;

public class StopConditionFactory implements IStopConditionFactory{
	
	@Override
	public StopCondition produceStopCondition(StopConditionType type, int limit){
		StopCondition resultStopCond = null;
		
		switch(type){
			case STAGNACY_CONDITION:{
				resultStopCond = new StagnacyCondition( limit);
				break;
			}
			case NO_ITERATE:{
				resultStopCond = new NoIterateCondition( limit);
				break;
			}
			case MIN_RESULT:
				resultStopCond = new MinResultCondition( limit);
				break;

		}
		
		return resultStopCond;
	}

}
