package CrossoverTest;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.crossover.Crossover;
import evolutionaryAlgorithm.crossover.CrossoverFactory;
import evolutionaryAlgorithm.crossover.CrossoverType;



@RunWith(Parameterized.class)
public class MultiCrossoverTest {
	private final int[] pointCrossingTable;
	private final String firstParentCode;
	private final String secondParentCode;
	private final String firstExpectedOffspringCode;
	private final String secondExpectedOffspringCode;

	private static CrossoverFactory factory; 
	private static Crossover crossover;
	
	@Parameterized.Parameters
    public static Iterable<Object[]> data() {
    	int[][] randomNumber = {
    			{3,7,9,13,14},
    			{2,7},
    			{1,2,3}
    	};
    	return Arrays.asList(new Object[][] { 
			{ randomNumber[0], "0101100000000000", "0000001100100000" ,"0100001000100000","0001100100000000"}, 
			{ randomNumber[1], "0100110010101100", "0010011101000101" ,"0110011010101100","0000110101000101" },
			{ randomNumber[2],	"11110000","00110101","10110101","01110000"}
		});
    	
    }
    
    @BeforeClass
    public static void setUp(){
 
    	factory=new CrossoverFactory();
    	crossover= factory.produceCrossover(CrossoverType.MULTI_POINT);
    }
    
    public  MultiCrossoverTest(int[] pointCrossing,String parent1,String parent2, String offspring1,String offspring2) {
		this.pointCrossingTable=pointCrossing;
		this.firstParentCode=parent1;
		this.secondParentCode=parent2;
		this.firstExpectedOffspringCode=offspring1;
		this.secondExpectedOffspringCode=offspring2;
	}
    
	@Test
	public final void shouldOnePointCrossoverTest(){
		
		Chromosom parent1= new Chromosom(this.firstParentCode);
		Chromosom parent2= new Chromosom(this.secondParentCode);
		int[] pointCrossingTable= this.pointCrossingTable;
		
		crossover.setParameters(pointCrossingTable, parent1, parent2);
		Chromosom[] resultChromosome=crossover.getChromosomes();
		System.out.println("1. Expect: "+firstExpectedOffspringCode +" \n is      : "+ resultChromosome[0].getChromosomeCode());
		System.out.println("2. Expect: "+secondExpectedOffspringCode +"\n is   "+ " is: "+ resultChromosome[1].getChromosomeCode());
		Assert.assertEquals(firstExpectedOffspringCode, resultChromosome[0].getChromosomeCode());
		Assert.assertEquals(secondExpectedOffspringCode,resultChromosome[1].getChromosomeCode());
	}
    
}
