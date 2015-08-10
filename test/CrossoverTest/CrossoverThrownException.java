package CrossoverTest;

import org.junit.BeforeClass;
import org.junit.Test;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.crossover.Crossover;
import evolutionaryAlgorithm.crossover.CrossoverFactory;
import evolutionaryAlgorithm.crossover.CrossoverType;

public class CrossoverThrownException {
	private static CrossoverFactory factory; 
	private static Crossover onePointcrossover;
	
    @BeforeClass
    public static void setUp(){
 
    	factory=new CrossoverFactory();
    	onePointcrossover= factory.produceCrossover(CrossoverType.ONE_POINT);
    }
    
	@Test(expected=IndexOutOfBoundsException.class)
	public  void whenSizeOfChromosomeIsdifferentThenThrownException(){
		int[] pointCrossingTable={3};
		onePointcrossover.setParameters(pointCrossingTable, new Chromosom("1001111111"), new Chromosom("1000"));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void whenPointOfCrossingIsIndexOutThenThrownException(){
		int[] pointCrossingTable={10};
		onePointcrossover.setParameters(pointCrossingTable, new Chromosom("1111"), new Chromosom("1000"));
	}
}
