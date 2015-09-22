package evolutionaryAlgorithm.reproduction;

public interface IReproductionFactory {
	Reproduction produceReproduction (ReproductionType type,int... quantityTournament);
}
