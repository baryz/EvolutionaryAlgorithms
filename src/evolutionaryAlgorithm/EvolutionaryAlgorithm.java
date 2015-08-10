package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import evolutionaryAlgorithm.crossover.*;

import graph.Graph;

public class EvolutionaryAlgorithm {
	
	
	private Chromosom bestChromosom;
    private static final double initial_select_prob=0.4;
    private static final double initial_mutate_prob=0.7;
    
	private Graph graph;
	private Population basePopulation;
	private Population tempPopulation;
	private CrossoverType crossingType;
	private Crossover crossover;
	
	public ArrayList<Integer> pairForCrossing;
	
    
	public EvolutionaryAlgorithm(Graph inGraph,CrossoverType type){
        graph=inGraph;
        basePopulation=new Population(graph.getNoVertex());
        tempPopulation=new Population(graph.getNoVertex());
        bestChromosom= new Chromosom(graph.getNoVertex());
       
        pairForCrossing= new ArrayList<>();
        crossingType=type;
	    CrossoverFactory factory = new CrossoverFactory();
	    crossover=factory.produceCrossover(crossingType);
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
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,CrossoverType.ONE_POINT);
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
            
            time=stopTimer-overallStartTimer;
            alg.reproduction();
            System.out.println("Overall  time----->: "+ time + " MS");
            
           
            
        }catch(CloneNotSupportedException ex){
            ex.printStackTrace();
        }catch(Exception ex){
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
        
        if(basePopulation.getSizePopulation()%2==1){
            int randInt=randomVertexGen.nextInt(graph.getNoVertex()-1);
            Chromosom randChromosom = (Chromosom) basePopulation.getChromosom(randInt).clone();
            basePopulation.addChromosom(randChromosom);
        }
        
        System.out.println("BEST "+ bestChromosom.getFitnes());
        System.out.println("AVG: "+basePopulation.getAvgFitness());
        
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
	    
	    public void reproduction() throws CloneNotSupportedException,Exception{
	        
	        pairForCrossing.clear();
	        Reproduction repro = new Reproduction();
	        //repro.check();
	        pairForCrossing=repro.rank(this.basePopulation);
	        
	        /*for(int x:pairForCrossing){
	            System.out.println("X: "+ x +" chromosom: \n"+ basePopulation.getChromosom(x).toString());
	        }*/
	        
	      
	      
	        int[][] tableOfPointsCrossing =getRandomCrossingPoint();
	        
	        
	        
	        for(int i=0; i<tableOfPointsCrossing.length;i++){
	    	    Chromosom[] chromosomeAfterCrossing= new Chromosom[2];
	        	crossover.setParameters(tableOfPointsCrossing[i],
	    	    		basePopulation.getChromosom(pairForCrossing.get((2*i))), 
	    	    		basePopulation.getChromosom(pairForCrossing.get(2*i+1)));
	    	    chromosomeAfterCrossing= crossover.getChromosomes();
	    	    tempPopulation.addChromosom(chromosomeAfterCrossing[0].clone());
	    	    tempPopulation.addChromosom(chromosomeAfterCrossing[1].clone());
	        }
	        
	       
	        /* for(int x:pairForCrossing){
	            System.out.println("X: "+ x +" chromosom: \n"+ basePopulation.getChromosom(x).toString());
	        }
	        for(int i=0;i<tempPopulation.getSizePopulation();i++){
	        	System.out.println(i+".CHromosom: \n"+ tempPopulation.getChromosom(i));
	        } */
	        
	        
	    }
	    
	    private int[][] getRandomCrossingPoint() throws Exception{
	    	
	    	if( basePopulation.getSizePopulation() % 2==1) throw new Exception("odd size of Population");
	    	
	    	int size = basePopulation.getSizePopulation()/2;
	    	int noCut =crossover.getNoOfCut();
	    	
	    	Random random= new Random();
	    	int[][] result = new int[size][noCut];
	    	
	    	for(int i=0;i<size;i++){
	    		for(int j=0;j<noCut;j++){
	    			result[i][j]= random.nextInt(graph.getNoVertex()-1);
	    		}
	    	}
	    	
	    	return result;
	    }
}
