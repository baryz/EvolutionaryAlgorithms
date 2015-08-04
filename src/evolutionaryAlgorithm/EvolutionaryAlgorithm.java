package evolutionaryAlgorithm;

import graph.Graph;

public class EvolutionaryAlgorithm {
	
	public static void main(String [] args){
		
		
        long overallStartTimer,startTimer, stopTimer,time;
        /*-----------LOAD WITH FILE------------*/
        overallStartTimer=System.currentTimeMillis();
        startTimer=System.currentTimeMillis();
        Graph graphEx=new Graph("resourceGraph/keller6.clq");
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Loading graph's time with File---------> "+ time + " MS");
        
	}
}
