package evolutionaryAlgorithm.stopCondition;

public abstract class StopCondition {
	protected StopConditionType type;
	public StopConditionType getType(){
		return type;
	}
	public abstract boolean isContinue();
}
