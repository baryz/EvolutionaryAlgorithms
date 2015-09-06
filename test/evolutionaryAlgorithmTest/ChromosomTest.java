package evolutionaryAlgorithmTest;

import java.util.Arrays;


import org.junit.runners.Parameterized;

import evolutionaryAlgorithm.Chromosom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.BeforeClass;

@RunWith(Parameterized.class)
public class ChromosomTest {
	private final int expectedHammingDistance;
	private final Chromosom chromosomX;
	private final Chromosom chromosomY;
	

	@Parameterized.Parameters
    public static Iterable<Object[]> data() {
    	return Arrays.asList(new Object[][] { 
			{3,new Chromosom("10011100"),new Chromosom("10010001")}, 
			{10,new Chromosom("100001100111011111010111011011101"),new Chromosom("101001100111000011101111011010000")},
			{0,new Chromosom("001100111111"),new Chromosom("001100111111")}
			
		});
    	
    }
    public  ChromosomTest(int expectedHamDist,Chromosom chromX, Chromosom chromY) {
		this.expectedHammingDistance=expectedHamDist;
    	this.chromosomX=chromX;
		this.chromosomY=chromY;
		
	}
    

    
	@Test
	public final void shouldHammingDistanceTest(){
		
		int result=chromosomX.getHammingDistance(chromosomY);
		int resultY=chromosomY.getHammingDistance(chromosomX);
		System.out.println("Expected: " + expectedHammingDistance + " result: " + result);
		Assert.assertEquals(expectedHammingDistance, result);
		Assert.assertEquals(expectedHammingDistance, resultY);
	}
}
