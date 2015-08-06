package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import graph.Graph;

public class EvolutionaryAlgorithm {
	
	
	private Chromosom bestChromosom;
    private static final double initial_select_prob=0.4;
    private static final double initial_mutate_prob=0.7;
    
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
        
        try {
            /*------Mutate Init pop --------*/
            startTimer=System.currentTimeMillis();
            alg.mutateInitPopulation();
            stopTimer=System.currentTimeMillis();
            time=stopTimer-startTimer;
            System.out.println("Mutate Init pop  time ------> "+ time + " MS");
        
            
            /*-------Local Optimization------*/
            startTimer=System.currentTimeMillis();
            alg.localOptimization();
            stopTimer=System.currentTimeMillis();
            time=stopTimer-startTimer;
            System.out.println("Local Optimization InitPop  time----->: "+ time + " MS");
        }catch(CloneNotSupportedException ex){
            ex.printStackTrace();
        }
        
        
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
	
	
	 public void mutateInitPopulation() throws CloneNotSupportedException{
	        
	        Random doubleGenerator=new Random();
	        double doubleRandom;
	        int countMutatedChrom=0;
	        int countMutatedGen=0;
	        int sizeChromosom=basePopulation.getChromosom(0).getSize();
	        for(int i=0;i<basePopulation.getSizePopulation();i++){
	            doubleRandom = doubleGenerator.nextDouble();
	            //System.out.println("Cz mutuje chromosom: "+ i +  " draw: " + doubleRandom) ;
	            if(doubleRandom<initial_select_prob){
	                countMutatedChrom++;
	                for(int j=0;j<sizeChromosom;j++){
	                    doubleRandom=doubleGenerator.nextDouble();
	                    //System.out.println("Czy mutuje gen "+ j +" draw"+ doubleRandom);
	                    if(doubleRandom<initial_mutate_prob){
	                        countMutatedGen++;
	                        basePopulation.getChromosom(i).setNegateGen(j);
	                    }
	                }
	            }
	        }
	        double percentMutate=((double)countMutatedChrom)/(double)basePopulation.getSizePopulation();
	        double percentMutateGen=(double)countMutatedGen/(double)(basePopulation.getSizePopulation()*sizeChromosom);
	        System.out.println("Zmutowane chromosomy:" + countMutatedChrom+" Zmutowane geny: " + countMutatedGen);
	        System.out.println("Osobniki: " +percentMutate+ " Geny: "+ percentMutateGen );
	        

	 }
	 
	    public void localOptimization()throws CloneNotSupportedException{
	        
	         //inputChrom.print();
	        //graph.printEdge();
	        Graph subGraph = null;
	        //subGraph.printEdge();
	         //subGraph.printDegreeVertex();
	        //Chromosom test=new Chromosom("0000100111001100");
	       // Chromosom test1=new Chromosom("0110010010011011");
	        //subGraph.setTest();
	        //subGraph.loadChromosom(inputChrom);
	        for(int i=0;i<basePopulation.getSizePopulation();i++){
	            //population.getChromosom(i).print();
	            //System.out.println("Ocena: "+ basePopulation.getChromosom(i).getFitnes());
	            subGraph= (Graph)graph.clone();
	            //Chromosom test = new Chromosom("1111101010111010");
	            //Chromosom test1 = new Chromosom("0000001100100000");
	            //subGraph.setTest();
	            //subGraph.printEdge();
	            
	            subGraph.loadChromosom(basePopulation.getChromosom(i));
	            //subGraph.printDegreeVertex();
	            //System.out.println("IsClique:"+subGraph.isClique());
	            //System.out.println("Po zal chromosomu!");
	            //subGraph.printEdge();
	            
	           /*System.out.println("Chromosom przed ekstrakcj¹:");
	            System.out.println("Ocena: " +basePopulation.getChromosom(i).getFitnes());
	            */
	           //population.getChromosom(i).print();
	           //subGraph.printDegreeVertex();
	            //subGraph.printEdge();
	            //System.out.println("Jest Klik¹: "+ subGraph.isClique());
	            
	            
	            subGraph.extractionClique();
	            
	            
	            boolean[] booleanArrayVertex=subGraph.getBoolArrayVertex();
	            boolean[] check = new boolean[booleanArrayVertex.length];
	            if(Arrays.equals(booleanArrayVertex, check)){
	                System.out.println("ERROR !!!!!!!!!!");
	              
	            }
	            basePopulation.getChromosom(i).update(booleanArrayVertex);
	            
	            /*
	            System.out.println("Po ekstrakcji !!!");
	            System.out.println("Ocena: " +basePopulation.getChromosom(i).getFitnes());
	            */
	            //population.getChromosom(i).print();
	            //subGraph.printDegreeVertex();
	            
	                // <--------------RESEARCH MAX CLIQUE WITOUT IMPROVEMENT CLIQUE--------------->
	            subGraph.improvementClique(graph,basePopulation.getChromosom(i));
	            /*
	            System.out.println("Po Rozszerzeniu!!!");
	            System.out.println("Ocena: " +basePopulation.getChromosom(i).getFitnes());
	            */
	            //population.getChromosom(i).print();
	            //subGraph.printDegreeVertex();
	            //subGraph.printEdge();
	            //System.out.println("Jest Klik¹: "+ subGraph.isClique());
	            
	        }
	    }
}
