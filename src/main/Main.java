package main;

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
		String graphName="DSJC1000-5";
		Config conf= new Config();
		
		 long overallStartTimer,startTimer, stopTimer,time;
        /*-----------LOAD GRAPH WITH FILE------------*/
        overallStartTimer=System.currentTimeMillis();
        startTimer=System.currentTimeMillis();
        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Loading graph's time with File---------> "+ time + " MS");
        
        /*-----------Init----------*/
        startTimer=System.currentTimeMillis();
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,50,
        						ReproductionType.RANK,
        						CrossoverType.MULTI_POINT,10,
        						SuccessionType.HAMMING_REPLACEMENT,
        						StopConditionType.STAGNACY_CONDITION, 50,
        						conf );
        stopTimer=System.currentTimeMillis();
        
        time=stopTimer-startTimer;
        System.out.println("Init  time---------> "+ time + " MS");
        
        alg.run();
        alg.getStatistic();
        stopTimer=System.currentTimeMillis();
        time=stopTimer-overallStartTimer;
        System.out.println("Overall  time---------> "+ time + " MS");
		
        LineChart avgFit = new LineChart("Œrednie przystosowanie, best chromosome",alg.getStatistic());
        avgFit.pack();
        RefineryUtilities.centerFrameOnScreen(avgFit);
        avgFit.setVisible(true);
        
        
	}

}
