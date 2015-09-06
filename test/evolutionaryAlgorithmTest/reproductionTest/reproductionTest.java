package evolutionaryAlgorithmTest.reproductionTest;

import java.util.ArrayList;

import org.junit.Test;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.Population;
import evolutionaryAlgorithm.reproduction.Reproduction;
import evolutionaryAlgorithm.reproduction.ReproductionFactory;
import evolutionaryAlgorithm.reproduction.ReproductionType;
import org.junit.Assert;

public class reproductionTest {

	@Test
	public final void selectParentWithRankReproduction(){
		   Population population=new Population(8);
		   Population selectParentPopulation = new Population(8);
		   
		   Chromosom[] chromTest={
				   new Chromosom("10100000"),new Chromosom("00110011"),new Chromosom("11001000"),new Chromosom("00111100"),
				   new Chromosom("11100011"),new Chromosom("11001100"),new Chromosom("11110000"), new Chromosom("11000111")
		   };
		   
		   for(Chromosom x:chromTest){
			   population.addChromosom(x);
		   }
		   
		   ReproductionFactory repFactory = new ReproductionFactory();
		   Reproduction repro =repFactory.produceReproduction(ReproductionType.RANK);
		   ArrayList<Integer> pair=repro.getPairForCrossing(population);
		   
		   for(int i=0; i<pair.size(); i++){
			   selectParentPopulation.addChromosom(population.getChromosom(pair.get(i)));
		   }
		   
		   Assert.assertEquals(selectParentPopulation.getAvgFitness() > population.getAvgFitness() , true);
	}
}
