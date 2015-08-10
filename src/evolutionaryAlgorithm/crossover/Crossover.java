package evolutionaryAlgorithm.crossover;

import evolutionaryAlgorithm.Chromosom;


public abstract class Crossover {
	protected CrossoverType type;
	public abstract void  setParameters(int[] pointCrossing,Chromosom chromosom1,Chromosom chromosom2);
	public abstract Chromosom[] getChromosomes(); 
	public abstract int getNoOfCut();
}
