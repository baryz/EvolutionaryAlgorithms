package evolutionaryAlgorithm.reproduction;

import java.util.ArrayList;

import evolutionaryAlgorithm.Population;

public abstract class Reproduction {
	public abstract ArrayList<Integer> getPairForCrossing(Population inPop);
}
