package CrossoverTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.crossover.Crossover;
import evolutionaryAlgorithm.crossover.CrossoverFactory;
import evolutionaryAlgorithm.crossover.CrossoverType;

import java.util.Arrays;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OnePointCrossoverTest {
	private final Integer pointCrossing;
	private final String firstParentCode;
	private final String secondParentCode;
	private final String firstExpectedOffspringCode;
	private final String secondExpectedOffspringCode;

	private static CrossoverFactory factory; 
	private static Crossover crossover;
	
	
	
	@Parameterized.Parameters
    public static Iterable<Object[]> data() {
    	return Arrays.asList(new Object[][] { 
			{ 1, "1001", "0000" ,"1000","0001"}, 
			{ 2, "1111", "0000" ,"1100","0011" }, 
			{ 5, "1100110011", "0000001111" ,"1100101111","0000010011" }, 
			{ 0, "101010101111111111111111", "101111111110101010110000" ,"101111111110101010110000","101010101111111111111111" },
			{ 3, "1010", "1111", "1011","1110"},
			{10, "101010111100111110000","100100100110110010101","101010111110110010101","100100100100111110000"}
		});
    	
    }
	
    @BeforeClass
    public static void setUp(){
 
    	factory=new CrossoverFactory();
    	crossover= factory.produceCrossover(CrossoverType.ONE_POINT);
    }
    
    public  OnePointCrossoverTest(Integer pointCrossing,String parent1,String parent2, String offspring1,String offspring2) {
		this.pointCrossing=pointCrossing;
		this.firstParentCode=parent1;
		this.secondParentCode=parent2;
		this.firstExpectedOffspringCode=offspring1;
		this.secondExpectedOffspringCode=offspring2;
	}
    
	@Test
	public final void shouldOnePointCrossoverTest(){
		
		Chromosom parent1= new Chromosom(this.firstParentCode);
		Chromosom parent2= new Chromosom(this.secondParentCode);
		int[] pointCrossingTable= {this.pointCrossing};
		
		crossover.setParameters(pointCrossingTable, parent1, parent2);
		Chromosom[] resultChromosome=crossover.getChromosomes();
		//System.out.println("1. Expect: "+firstExpectedOffspringCode +" \n is      : "+ resultChromosome[0].getChromosomeCode());
		//System.out.println("2. Expect: "+secondExpectedOffspringCode +"\n is   "+ " is: "+ resultChromosome[1].getChromosomeCode());
		Assert.assertEquals(firstExpectedOffspringCode, resultChromosome[0].getChromosomeCode());
		Assert.assertEquals(secondExpectedOffspringCode,resultChromosome[1].getChromosomeCode());
	}
	

}
