package evolutionaryAlgorithm.stopCondition;

public abstract class StopCondition {
	public abstract boolean isContinue();
	
	protected StopConditionType type;
	
	public StopConditionType getType(){
		return type;
	}
	
	public int getParam(){
		return 0;
	}
}
