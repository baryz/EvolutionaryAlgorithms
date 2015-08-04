package evolutionaryAlgorithm;

import java.util.ArrayList;

import graph.Graph;

public class EvolutionaryAlgorithm {
	
	private Graph graph;
	private Population basePopulation;
	
	
	 public ArrayList<Integer> pairForCrossing;
    
	public EvolutionaryAlgorithm(Graph inGraph){
        graph=inGraph;
     }
	
	public static void main(String [] args){
		
		
        long overallStartTimer,startTimer, stopTimer,time;
        /*-----------LOAD WITH FILE------------*/
        overallStartTimer=System.currentTimeMillis();
        startTimer=System.currentTimeMillis();
        Graph graphEx=new Graph("resourceGraph/keller6.clq");
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Loading graph's time with File---------> "+ time + " MS");
        
        /*-----------Init----------*/
        startTimer=System.currentTimeMillis();
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx);
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Init  time---------> "+ time + " MS");
        
	}
}
