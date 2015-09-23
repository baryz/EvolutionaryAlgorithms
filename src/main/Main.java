package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.ui.RefineryUtilities;

import chart.LineChart;
import config.Config;
import evolutionaryAlgorithm.EvolutionaryAlgorithm;
import evolutionaryAlgorithm.crossover.CrossoverType;
import evolutionaryAlgorithm.reproduction.ReproductionType;
import evolutionaryAlgorithm.stopCondition.StopConditionType;
import evolutionaryAlgorithm.succession.SuccessionType;
import graph.Graph;

public class Main {

	public static void main(String[] args) {
		
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
		Config conf= new Config();
		HashMap<String, Object[]> summaryResult = new HashMap<>();
		
		for( String graphName: graphNameTab){
		
			System.out.println( "--------------------Graph name : "+ graphName+ "--------------------");
			 Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
			 EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,50,
						ReproductionType.ROULLETEWHEEL,
						CrossoverType.MULTI_POINT,10,
						SuccessionType.HAMMING_REPLACEMENT,
						StopConditionType.STAGNACY_CONDITION, 5,
						conf);
		     alg.run();
		     Object[] row = new Object[2];
		     row[0] = alg.getResult().length;
		     row[1] = graphEx.checkClique(alg.getResult());
		     summaryResult.put(graphName, row);
		   /*  
			LineChart avgFit = new LineChart(graphName + " Œrednie przystosowanie, best chromosome",alg.getStatistic());
	        avgFit.pack();
	        RefineryUtilities.centerFrameOnScreen(avgFit);
	        avgFit.setVisible(true);
	        */

		}
	    Iterator<String> it=summaryResult.keySet().iterator();
		while(it.hasNext()){
			String key= it.next();
			System.out.println(key +": " + summaryResult.get(key)[0] +" isClique: " + summaryResult.get(key)[1] );
		}
		
		/*
		Config conf= new Config();
		String graphName= graphNameTab[34];
		
		 long overallStartTimer,startTimer, stopTimer,time;
     
        overallStartTimer=System.currentTimeMillis();
        startTimer=System.currentTimeMillis();
        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Loading graph's time with File---------> "+ time + " MS");
        
       
        startTimer=System.currentTimeMillis();
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,50,
        						ReproductionType.ROULLETEWHEEL,
        						CrossoverType.MULTI_POINT,10,
        						SuccessionType.HAMMING_REPLACEMENT,
        						StopConditionType.STAGNACY_CONDITION, 5,
        						conf,
        						10);
        stopTimer=System.currentTimeMillis();
        
        time=stopTimer-startTimer;
        System.out.println("Init  time---------> "+ time + " MS");
        
        alg.run();
        alg.getStatistic();
        stopTimer=System.currentTimeMillis();
        time=stopTimer-overallStartTimer;
        System.out.println("Overall  time---------> "+ time + " MS");
		System.out.println("BEST CLIQUE: "+ Arrays.toString(alg.getResult()));
        System.out.println("is Clique: " + graphEx.checkClique(alg.getResult()));
		
		LineChart avgFit = new LineChart("Œrednie przystosowanie, best chromosome",alg.getStatistic());
        avgFit.pack();
        RefineryUtilities.centerFrameOnScreen(avgFit);
        avgFit.setVisible(true);
        */
        
	}

}
