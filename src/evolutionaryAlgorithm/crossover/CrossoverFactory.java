package evolutionaryAlgorithm.crossover;

public class CrossoverFactory implements ICrossoverFactory{
		@Override
		public Crossover produceCrossover(CrossoverType type){
		
		Crossover crossoverResult=null;
		
		switch(type){
			case ONE_POINT: {
			crossoverResult = new OnePointCrossover();
				break;
			}
			case MULTI_POINT:{
				crossoverResult = new MultiCrossover();
				break;
			}
		}
		
		return crossoverResult;
	}

}
