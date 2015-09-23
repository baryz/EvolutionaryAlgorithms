package evolutionaryAlgorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import config.Config;
import distribution.RandomWithoutDuplicate;
import evolutionaryAlgorithm.crossover.*;
import evolutionaryAlgorithm.reproduction.*;
import evolutionaryAlgorithm.stopCondition.*;
import evolutionaryAlgorithm.succession.*;
import graph.Graph;

public class EvolutionaryAlgorithm {
	
	
	private Chromosom bestChromosom;
    private final double initial_select_prob=0.4;
    private final double initial_mutate_prob=0.7;
    private double offspring_selection_prob=0.5;
    private final double step_offspring_selection_prob = 0.05;
    private double offspring_mutate_prob=0.4;
    private final double step_offspring_mutate_prob = 0.05;
    private final int change_step_for_genetic_operator = 10;
    private int noOfCutting;
    private final int step_reduce_no_of_cutting = 2;
    private static final int size_of_elite = 1;
    private static final double part_replacement_factor=0.4;
    private  final int sizePopulation;
	private Graph graph;
	private Population basePopulation;
	private Population tempPopulation;

	private Crossover crossover;
	private Reproduction reproduction;
	private SuccessionType successionType;
	
	
	private StopCondition stopCondition;
	
	
	private Statistic stats;
	private final Config config;
	
	public ArrayList<Integer> pairForCrossing;
	
	
	
	public EvolutionaryAlgorithm(Graph inGraph,int sizeOfPopulation,
			ReproductionType reproType,CrossoverType crossType,
			int noOfCut,SuccessionType succesType,
			StopConditionType stopCondType, int parStopCondition,
			Config conf,
			int... quantityOfTour){
        graph=inGraph;
        sizePopulation = sizeOfPopulation;
        basePopulation=new Population(sizePopulation);
        tempPopulation=new Population(sizePopulation);
      
        bestChromosom= new Chromosom(graph.getNoVertex());
        pairForCrossing= new ArrayList<>();
        CrossoverFactory factory = new CrossoverFactory();
	    crossover=factory.produceCrossover(crossType);
	    noOfCutting = noOfCut;
	    crossover.setNoOfCut(noOfCutting);
	    successionType=succesType;
	    ReproductionFactory reproductionFactory= new ReproductionFactory();
	    
	    if(quantityOfTour.length > 0){
	    	reproduction=reproductionFactory.produceReproduction(reproType, quantityOfTour[0]);
	    }else{
	    	reproduction=reproductionFactory.produceReproduction(reproType);
	    }
	    
	    StopConditionFactory stopCondFactory= new StopConditionFactory();
	    stopCondition = stopCondFactory.produceStopCondition(stopCondType, parStopCondition);
	    
	    config=conf;
	 }
	
	public static void main(String [] args){
		
		String graphName="keller5";
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
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,ReproductionType.ROULLETEWHEEL,
        						CrossoverType.MULTI_POINT,20,
        						SuccessionType.HAMMING_REPLACEMENT,
        						StopConditionType.STAGNACY_CONDITION, 20,
        						conf);
        stopTimer=System.currentTimeMillis();
        
        time=stopTimer-startTimer;
        System.out.println("Init  time---------> "+ time + " MS");
        
        alg.run();
        
        stopTimer=System.currentTimeMillis();
        time=stopTimer-overallStartTimer;
        System.out.println("Overall  time---------> "+ time + " MS");
    }
	
	public void run(){
		
		long startTimer, stopTimer,time,startIterationTimer;
       
		try{
        	 /*-----------Create Init Population----------*/
        	//loadPopulationWithFile(config.getInitPopulationDirPath()+graph.getName()+config.getPopulationExtension());
        	createInitPopulationAfterGeneticOp();
        	stats = new Statistic( basePopulation );
            stats.printInitPopulationData();
             /*-----------SAVE TO FILE Init Population----------*/
            //savePopulationToFile(basePopulation,config.getInitPopulationDirPath()+graph.getName()+config.getPopulationExtension());
            int i=0;
            prepareStopConditionPar();
            while(stopCondition.isContinue()){
	           	System.out.println("----------------START IT-------------------------");

            	startIterationTimer=System.currentTimeMillis();
            	  /*-------Reproduction Crossover------*/
   	            startTimer=System.currentTimeMillis();
   	            reproduction();
   	            stopTimer=System.currentTimeMillis();
   	            time=stopTimer-startTimer;
   	            //System.out.println("Reproduction  time----->: "+ time + " MS");
   	            
   	            /*-------Mutate------*/
   	            startTimer=System.currentTimeMillis();
   	            mutateTempPopulation();
   	            stopTimer=System.currentTimeMillis();
   	            time=stopTimer-startTimer;
   	            //System.out.println("Mutate after crossover  time----->: "+ time + " MS");
   	            
   	            /*-------Local Optimization after genetic operands------*/
   	            startTimer=System.currentTimeMillis();
   	            localOptimizationAfterGeneticOperation();
   	            stopTimer=System.currentTimeMillis();
   	            time=stopTimer-startTimer;
   	            //System.out.println("Local Optimization after genetic operands  time----->: "+ time + " MS");
   	           
   	           prepareNextPopulation();
   	           
   	           stats.insertData(basePopulation, tempPopulation);
   	           stats.printIterationData(i+1);
   	           updateBestGlobalChromosome();
   	           System.out.println("IRERATION "+i + "  GLOBAL BEST : " + bestChromosom.getFitnes());
   	           System.out.println("STATS: "+ Arrays.toString(stats.getBestChromBaseList()));
   	           System.out.println("--------------------END IT--------------------------");
   	           prepareStopConditionPar();
   	           prepareParameterOfGeneticOperation(i);
   	           i++;
            }
	    }catch(CloneNotSupportedException ex){
	        ex.printStackTrace();
	    }catch(Exception ex){
	        ex.printStackTrace();
	    }
	}
	
	
    public Statistic getStatistic(){
    	return stats;
    }
    
    public int[] getResult(){
    	int[] resultTable = new int[bestChromosom.getFitnes()];
    	String[] cliqueStringTable = bestChromosom.getClique().split(",");
    	for(int i=0; i<resultTable.length; i++){
    		resultTable[i] = Integer.parseInt(cliqueStringTable[i].trim());
    	}
    	
    	return resultTable;
    }
    
    
    
    private void partReplacementSuccesion() {
		Chromosom[] basePopulationSortTable=basePopulation.sortByFitness();
    	Chromosom[] tempPopulationSortTable=tempPopulation.sortByFitness();
    	
    	Population newPopulation = new Population(basePopulation.getSizePopulation());
    	int sizeOfPart=(int)Math.round(part_replacement_factor*basePopulation.getSizePopulation());
    	for(int i=0;i<sizeOfPart;i++){
    		newPopulation.addChromosom(tempPopulationSortTable[i]);
    	}
    	for(int i=0;i<basePopulation.getSizePopulation()-sizeOfPart;i++){
    		newPopulation.addChromosom(basePopulationSortTable[i]);
    		
    	}
    	
    	basePopulation=newPopulation;
	}
    
	private void createInitPopulationAfterGeneticOp() throws CloneNotSupportedException{
		long startTimer,stopTimer,time;
		generateInitPopulation();
    	mutateInitPopulation();
      
        //-------Local Optimization------
        startTimer=System.currentTimeMillis();
        localOptimization();
        stopTimer=System.currentTimeMillis();
        time=stopTimer-startTimer;
        System.out.println("Local Optimization InitPop  time----->: "+ time + " MS");
        
	}
	
	private void generateInitPopulation() throws CloneNotSupportedException {
        
		Chromosom xChromosom =null;
        Random  randomVertexGen= new Random();
        for(int j=0;j<sizePopulation;j++){
            int randomVi=randomVertexGen.nextInt((graph.getNoVertex()-1));
            ArrayList<Integer> setA= new ArrayList<>();
            setA.add(randomVi);
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
             basePopulation.addChromosom(xChromosom);
             if(xChromosom.getFitnes()>bestChromosom.getFitnes()){
                 bestChromosom=(Chromosom)xChromosom.clone();
             }

       	}
        
        if(basePopulation.getSizePopulation()%2==1){
            int randInt=randomVertexGen.nextInt(graph.getNoVertex()-1);
            Chromosom randChromosom = (Chromosom) basePopulation.getChromosom(randInt).clone();
            basePopulation.addChromosom(randChromosom);
        }

        
	}
	
	
	 private void mutateInitPopulation() throws CloneNotSupportedException{
	        
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
	 
	 private void mutateTempPopulation() throws CloneNotSupportedException {
	        
		 Random doubleGenerator=new Random();
	        double doubleRandom;
	        int countMutatedChrom=0;
	        int countMutatedGen=0;
	        int sizeChromosom=tempPopulation.getChromosom(0).getSize();
	        for(int i=0;i<tempPopulation.getSizePopulation();i++){
	            doubleRandom = doubleGenerator.nextDouble();
	            //System.out.println("Cz mutuje chromosom: "+ i +  " draw: " + doubleRandom) ;
	            if(doubleRandom<offspring_selection_prob){
	                countMutatedChrom++;
	                for(int j=0;j<sizeChromosom;j++){
	                    doubleRandom=doubleGenerator.nextDouble();
	                    //System.out.println("Czy mutuje gen "+ j +" draw"+ doubleRandom);
	                    if(doubleRandom<offspring_mutate_prob){
	                        countMutatedGen++;
	                        tempPopulation.getChromosom(i).setNegateGen(j);
	                    }
	                }
	            }
	        }
	        double percentMutate=((double)countMutatedChrom)/(double)tempPopulation.getSizePopulation();
	        double percentMutateGen=(double)countMutatedGen/(double)(tempPopulation.getSizePopulation()*sizeChromosom);
	        //System.out.println("Zmutowane chromosomy:" + countMutatedChrom+" Zmutowane geny: " + countMutatedGen);
	        //System.out.println("Osobniki: " +percentMutate+ " Geny: "+ percentMutateGen );
	 }
	 
	 private void localOptimization() throws CloneNotSupportedException{
	        Graph subGraph = null;
	        
	        for(int i=0;i<basePopulation.getSizePopulation();i++){
	        	subGraph= (Graph)graph.clone();
	            subGraph.loadChromosom(basePopulation.getChromosom(i));
	            subGraph.extractionClique();
	            
	             boolean[] booleanArrayVertex=subGraph.getBoolArrayVertex();
	            boolean[] check = new boolean[booleanArrayVertex.length];
	            if(Arrays.equals(booleanArrayVertex, check)){
	            	
	                System.out.println("ERROR !!!!!!!!!!");
	              
	            }
	            basePopulation.getChromosom(i).update(booleanArrayVertex);
	            // <--------------RESEARCH MAX CLIQUE WITOUT IMPROVEMENT CLIQUE--------------->
	            subGraph.improvementClique(graph,basePopulation.getChromosom(i));
	        }
	 }
	    
	 private void localOptimizationAfterGeneticOperation() throws CloneNotSupportedException{
		    Graph subGraph = null;
		  
		    for(int i=0;i<tempPopulation.getSizePopulation();i++){
		        subGraph= (Graph)graph.clone();
		        subGraph.loadChromosom(tempPopulation.getChromosom(i));
		        subGraph.extractionClique();
		        boolean[] booleanArrayVertex=subGraph.getBoolArrayVertex();
		        tempPopulation.getChromosom(i).update(booleanArrayVertex);
		        subGraph.improvementClique(graph,tempPopulation.getChromosom(i));
		    }
	 }
	    
	private void reproduction() throws CloneNotSupportedException,Exception{
	    
	  pairForCrossing.clear();
	  tempPopulation.clear();
	  pairForCrossing=reproduction.getPairForCrossing(basePopulation);
	    
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
	    
	    
	}
	    
    private void prepareNextPopulation() throws CloneNotSupportedException{
    	switch(successionType){
	    	case SIMPLY: {
	    		simplySuccession();
	    		break;	
	    	}
	    	case ELITARY: {
	    		elitarySuccesion();
	    		break;
	    	}
	    	case PART_REPLACEMENT:{
	    		partReplacementSuccesion();
	    		break;
	    	}
	    	case HAMMING_REPLACEMENT:{
	    		hammingReplacementSuccesion();
	    		break;
	    	}
    	}
    	
    }
	    


	private void elitarySuccesion() {
		
		Chromosom[] basePopulationSortTable=basePopulation.sortByFitness();
		Chromosom[] tempPopulationSortTable=tempPopulation.sortByFitness();
		
		Population newPopulation = new Population(basePopulation.getSizePopulation());
		for(int i=0;i<size_of_elite;i++)
			newPopulation.addChromosom(basePopulationSortTable[i]);
		for(int i=0;i<basePopulation.getSizePopulation()-size_of_elite;i++)
			newPopulation.addChromosom(tempPopulationSortTable[i]);
			
		basePopulation=newPopulation;
	}

	private void simplySuccession() throws CloneNotSupportedException{
    	basePopulation= (Population)tempPopulation.clone();
    }
	    
	private void hammingReplacementSuccesion() throws CloneNotSupportedException{
		
		ArrayList<Chromosom> reserveChromList = new ArrayList<>();
		int size = tempPopulation.getSizePopulation()/2;
		
		for(int i=0;i<size;i++){
			//Population copyBasePopulation = (Population) basePopulation.clone();
			Chromosom parent1 = basePopulation.getChromosom(pairForCrossing.get(2*i));
			Chromosom parent2 = basePopulation.getChromosom(pairForCrossing.get(2*i+1));
			Chromosom offspring1 = tempPopulation.getChromosom(2*i);
			Chromosom offspring2 = tempPopulation.getChromosom(2*i+1);
			int hammingDst1 = 0;
			int hammingDst2 = 0;
			boolean changeFlag=false;
			
			if(offspring1.getFitnes()>offspring2.getFitnes()){
				 hammingDst1 = offspring1.getHammingDistance(parent1);
				 hammingDst2 = offspring1.getHammingDistance(parent2);
				
				if(hammingDst1<hammingDst2){
					if (offspring1.getFitnes() > parent1.getFitnes() ){
						basePopulation.setChromosom(pairForCrossing.get(2*i), offspring1);
						changeFlag=true;
					}else if(offspring1.getFitnes() > parent2.getFitnes()){
						basePopulation.setChromosom(pairForCrossing.get(2*i+1), offspring1);
						changeFlag=true;
					}
				}else{
					if (offspring1.getFitnes() > parent2.getFitnes() ){
						basePopulation.setChromosom(pairForCrossing.get(2*i+1), offspring1);
						changeFlag=true;
					}else if(offspring1.getFitnes() > parent1.getFitnes()){
						basePopulation.setChromosom(pairForCrossing.get(2*i), offspring1);
						changeFlag=true;
					}
				}
				
			}else{
				 hammingDst1 = offspring2.getHammingDistance(parent1);
				 hammingDst2 = offspring2.getHammingDistance(parent2);
				if(hammingDst1<hammingDst2){
					if (offspring2.getFitnes() > parent1.getFitnes() ){
						basePopulation.setChromosom(pairForCrossing.get(2*i), offspring2);
						changeFlag=true;
					}else if(offspring2.getFitnes() > parent2.getFitnes()){
						basePopulation.setChromosom(pairForCrossing.get(2*i+1), offspring2);
						changeFlag=true;
					}
				}else{
					if (offspring2.getFitnes() > parent2.getFitnes() ){
						basePopulation.setChromosom(pairForCrossing.get(2*i+1), offspring2);
						changeFlag=true;
					}else if(offspring2.getFitnes() > parent1.getFitnes()){
						basePopulation.setChromosom(pairForCrossing.get(2*i), offspring2);
						changeFlag=true;
					}
				}
				 
			}//end if offspring1.getFitnes()>offspring2.getFitnes()
			if(changeFlag==false){
				Chromosom bestChromosom = (offspring1.getFitnes()>offspring2.getFitnes())? offspring1 : offspring2;
				reserveChromList.add(bestChromosom);
			}
		}//end for
		
		Chromosom[] sortChromosomTable = basePopulation.sortByFitness();
		for(int i=0;i<reserveChromList.size();i++){
			if(reserveChromList.get(i).getFitnes() > sortChromosomTable[sortChromosomTable.length-1].getFitnes()){
				sortChromosomTable[sortChromosomTable.length-1] = reserveChromList.get(i);
			};
			
			sortChromosomTable = basePopulation.sortByFitness();
		}
		
	}
		
	private int[][] getRandomCrossingPoint() throws Exception{
		
		if( basePopulation.getSizePopulation() % 2==1) throw new Exception("odd size of Population");
		
		int size = basePopulation.getSizePopulation()/2;
		int noCut =crossover.getNoOfCut();
		int[][] result = new int[size][noCut];
		
		for(int i=0;i<size;i++){
			result[i]= RandomWithoutDuplicate.get(noCut,graph.getNoVertex()-1);
			Arrays.sort(result[i]);
		}
		
		return result;
	}
	    
	private void savePopulationToFile(Population pop,String directoryPath){
		Path newFile = Paths.get(directoryPath);
		
		try{
			Files.deleteIfExists(newFile);
			newFile=Files.createFile(newFile);
			BufferedWriter writer = Files.newBufferedWriter(newFile, Charset.defaultCharset());
			writer.append("s initial_select_prob="+this.initial_select_prob +" initial_mutate_prob=" + this.initial_mutate_prob);
			writer.newLine();
			writer.append("s AVG FITNESS: " +pop.getAvgFitness() + "  BEST FITNESS: " + pop.getBestChromosome().getFitnes()+ 
							" SIZE POP: "+ pop.getSizePopulation());
			writer.newLine();
			
			for(Chromosom x:pop){
				writer.append(x.getChromosomeCode());
				writer.newLine();
			}
			writer.flush();
		}catch (IOException ex){
			System.out.println("Error creating file");
		}
	}
	    
	private void loadPopulationWithFile(String directoryPath){
		
		basePopulation.clear();
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new FileReader(directoryPath));
			String sCurrentLine;
			
			while ((sCurrentLine=reader.readLine())!=null){
				if(sCurrentLine.charAt(0)=='s')
					continue;
				
				Chromosom tmpChrom= new Chromosom(sCurrentLine);
				basePopulation.addChromosom(tmpChrom);
			}
			reader.close();
		}catch(IOException ex){
			System.out.println("Error loading file");
		}
	}
	    
	    
	private void updateBestGlobalChromosome() throws CloneNotSupportedException{
		
		int bestChromFitInCurrentBasePop = basePopulation.getBestChromosome().getFitnes();
		int bestGlobalChromFitInCurrentBasePop = bestChromosom.getFitnes();
		if( bestChromFitInCurrentBasePop > bestGlobalChromFitInCurrentBasePop){
			bestChromosom = (Chromosom) basePopulation.getBestChromosome().clone();
		}
		
	}
	    
	private void prepareStopConditionPar(){
		switch(stopCondition.getType()){
			case STAGNACY_CONDITION:{
				 StagnacyCondition cond = (StagnacyCondition)stopCondition;
				 cond.setParameter(bestChromosom.getFitnes(), stats);
				 break;
			}
			case NO_ITERATE:{
				NoIterateCondition cond = (NoIterateCondition)stopCondition;
	   			 cond.setParameter(stats);
				break;
			}
			case MIN_RESULT:{
				MinResultCondition cond = (MinResultCondition)stopCondition;
	   			cond.setParameter(bestChromosom.getFitnes());
				break;
			}
		}
		
	}
	    
	private void  prepareParameterOfGeneticOperation(int noOfGenetarion){
		
		if(noOfGenetarion != 0 && 
		   noOfGenetarion % change_step_for_genetic_operator == 0){
			if( ! (offspring_selection_prob <= step_offspring_selection_prob) )
				offspring_selection_prob = offspring_selection_prob - step_offspring_selection_prob ;
			if( ! (offspring_mutate_prob <= step_offspring_mutate_prob ))
				offspring_mutate_prob = offspring_mutate_prob - step_offspring_selection_prob;
			if( ! (noOfCutting <= step_reduce_no_of_cutting))
				noOfCutting -= step_reduce_no_of_cutting; 
			
			crossover.setNoOfCut(noOfCutting);
			//System.out.println("LICZBA CIEC: " + crossover.getNoOfCut() + "offspring_selection_prob: " + offspring_selection_prob + " offspring_mutate_prob: "+ offspring_mutate_prob);
			
		}
		
	}
	    
	    
}
