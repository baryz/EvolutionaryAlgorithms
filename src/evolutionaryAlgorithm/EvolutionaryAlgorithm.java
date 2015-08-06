package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import graph.Graph;

public class EvolutionaryAlgorithm {
	
	
	private Chromosom bestChromosom;
	private Graph graph;
	private Population basePopulation;
	
	
	 public ArrayList<Integer> pairForCrossing;
    
	public EvolutionaryAlgorithm(Graph inGraph){
        graph=inGraph;
        basePopulation=new Population(graph.getNoVertex());
        bestChromosom= new Chromosom(graph.getNoVertex());
     }
	
	public static void main(String [] args){
		
		
        long overallStartTimer,startTimer, stopTimer,time;
        /*-----------LOAD WITH FILE------------*/
        overallStartTimer=System.currentTimeMillis();
        startTimer=System.currentTimeMillis();
        Graph graphEx=new Graph("resourceGraph/test16.clq");
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Loading graph's time with File---------> "+ time + " MS");
        
        /*-----------Init----------*/
        startTimer=System.currentTimeMillis();
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx);
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Init  time---------> "+ time + " MS");
        
        /*-----------Create Init Population----------*/
        startTimer=System.currentTimeMillis();
        try {
            alg.generateInitPopulation();
        }catch(CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Create Init Pop  time ------> "+ time + " MS");
        
	}
	
	public void generateInitPopulation() throws CloneNotSupportedException {
        //int bestResult=0;
		Chromosom xChromosom =null;
        Random  randomVertexGen= new Random();
        for(int j=0;j<graph.getNoVertex();j++){
            int randomVi=randomVertexGen.nextInt((graph.getNoVertex()-1));
            ArrayList<Integer> setA= new ArrayList<>();
            setA.add(randomVi);
             //System.out.println("Size:"+graph.getNoVertex());
            //System.out.println("Wylosowa³em Vi= "+randomVi);
            ArrayList<Integer> adjacencyList = graph.getAdjacencyArrayList(randomVi);

            int randomIndexVj=0;
            while(adjacencyList.size()>0){
                boolean isInClique=false;
                int randomVj=adjacencyList.get(0);
                if(adjacencyList.size()==1){
                    isInClique = graph.checkClique(setA, randomVj);
                }else{
                    randomIndexVj=randomVertexGen.nextInt((adjacencyList.size()-1));
                    randomVj=adjacencyList.get(randomIndexVj);
                    isInClique=graph.checkClique(setA,randomVj);
                }
                //System.out.println("Wylosowa³em Vj:"+randomVj);
                if(isInClique){
                    //System.out.println("Dodaje do A:"+randomVj);
                    setA.add(randomVj);
                }
                adjacencyList.remove(randomIndexVj);


            }
             
             xChromosom=new Chromosom(setA,graph.getNoVertex());
             //xChromosom.print();
             //System.out.println(j+".Ocena osobnika:"+xChromosom.getFitnes());
             basePopulation.addChromosom(xChromosom);
             if(xChromosom.getFitnes()>bestChromosom.getFitnes()){
                 bestChromosom=(Chromosom)xChromosom.clone();
                 //bestResult=xChromosom.getFitnes();
                 //System.out.println("BEST "+ xChromosom.getFitnes());
             }

       	}
	}
}
