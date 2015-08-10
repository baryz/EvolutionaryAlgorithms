package evolutionaryAlgorithm.crossover;

public interface ICrossoverFactory {
	Crossover produceCrossover(CrossoverType type);
}
