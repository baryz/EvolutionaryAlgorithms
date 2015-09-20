package evolutionaryAlgorithm.stopCondition;

public interface IStopConditionFactory {
	StopCondition produceStopCondition(StopConditionType type, int limit);
}
