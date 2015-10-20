package evolutionaryAlgorithmTest;


import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import config.Config;
import evolutionaryAlgorithm.*;
import evolutionaryAlgorithm.crossover.CrossoverType;
import evolutionaryAlgorithm.reproduction.ReproductionType;
import evolutionaryAlgorithm.stopCondition.StopConditionType;
import evolutionaryAlgorithm.succession.SuccessionType;
import graph.Graph;

@RunWith(Parameterized.class)
public class EvolutionaryAlgorithmTest {
	private final int sizeOfPopulation;
	private final String graphName;

	
	@Parameterized.Parameters
    public static Iterable<Object[]> data() {
    	
    	String[] graphNameTab= {"brock200_1","brock200_2","brock200_3","brock200_4",
    			"brock400_1","brock400_2","brock400_3","brock400_4",
    			"brock800_1","brock800_2","brock800_3","brock800_4",
    			"c-fat200-1","c-fat200-2","c-fat200-5",
    			"c-fat500-1","c-fat500-2","c-fat500-5","c-fat500-10",
    			"hamming6-2","hamming6-4","hamming8-2","hamming8-4","hamming10-2","hamming10-4",
    			"johnson8-2-4","johnson8-4-4","johnson16-2-4","johnson32-2-4",
    			"keller4","keller5",
    			"MANN_a9","MANN_a27","MANN_a81",
    			"p_hat300-1","p_hat300-2","p_hat300-3","p_hat500-1","p_hat500-2","p_hat500-3",
    			"p_hat700-1","p_hat700-2","p_hat700-3","p_hat1000-1","p_hat1000-2","p_hat1000-3",
    			"p_hat1500-1","p_hat1500-2","p_hat1500-3",
    			"san200_0.7_1","san200_0.7_2","san200_0.9_1","san200_0.9_2","san200_0.9_3",
    			"san400_0.5_1","san400_0.7_1","san400_0.7_2","san400_0.7_3","san400_0.9_1","san1000",
    			"keller6"};
    	 Object[][] result = new Object[ graphNameTab.length ][2];
    	for(int i=0 ; i<graphNameTab.length; i++){
    		result[i][0] = graphNameTab[i];
    		result[i][1] = 10;
    	}
    	
    	return Arrays.asList(result);
    	
    }
    
    public EvolutionaryAlgorithmTest(String graphName, int sizeOfPopulation){
    	this.graphName = graphName;
    	this.sizeOfPopulation = sizeOfPopulation;
    }
    
    @Test
    public final void checkIfResultIsClique (){
    	
    	Config conf= new Config();
    	double[] mutationParamTable = {0.5, 0.2, 0.4, 0.2, 0.05, 0.05};
	
    	Graph inGraph = new Graph( conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
    	MutationParam inMutationParam = new MutationParam(mutationParamTable );
    	 EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(inGraph,this.sizeOfPopulation,
					ReproductionType.ROULLETEWHEEL,
					CrossoverType.MULTI_POINT,10,10,
					inMutationParam,10,
					SuccessionType.HAMMING_REPLACEMENT,
					StopConditionType.STAGNACY_CONDITION, 5,
					true,conf,
					10);
    	 alg.run();
    	 Assert.assertEquals(true, inGraph.checkClique(alg.getResult()));
    	 
    	 
    }
	  

}
