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
import evolutionaryAlgorithm.reproduction.Reproduction;
import evolutionaryAlgorithm.reproduction.ReproductionFactory;
import evolutionaryAlgorithm.reproduction.ReproductionType;
import evolutionaryAlgorithm.succession.SuccessionType;
import graph.Graph;

public class EvolutionaryAlgorithm {
	
	
	private Chromosom bestChromosom;
    private final double initial_select_prob=0.4;
    private final double initial_mutate_prob=0.7;
    private static final double offspring_selection_prob=0.2;
    private static final double offspring_mutate_prob=0.5;
    
    private static final int size_of_elite = 2;
    private static final double part_replacement_factor=0.7;
	private Graph graph;
	private Population basePopulation;
	private Population tempPopulation;

	private Crossover crossover;
	private Reproduction reproduction;
	private SuccessionType successionType;
	
	private Statistic stats;
	private final Config config;
	
	public ArrayList<Integer> pairForCrossing;
	
	
	
	public EvolutionaryAlgorithm(Graph inGraph,ReproductionType reproType,CrossoverType crossType,int noOfCut,SuccessionType succesType,Config conf){
        graph=inGraph;
        basePopulation=new Population(graph.getNoVertex());
        tempPopulation=new Population(graph.getNoVertex());
      
        
        bestChromosom= new Chromosom(graph.getNoVertex());
        pairForCrossing= new ArrayList<>();
        CrossoverFactory factory = new CrossoverFactory();
	    crossover=factory.produceCrossover(crossType);
	    crossover.setNoOfCut(noOfCut);
	    successionType=succesType;
	    ReproductionFactory reproductionFactory= new ReproductionFactory();
	    reproduction=reproductionFactory.produceReproduction(reproType);
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
        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,ReproductionType.ROULLETEWHEEL,
        						CrossoverType.MULTI_POINT,5,
        						SuccessionType.HAMMING_REPLACEMENT,
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
        	loadPopulationWithFile(config.getInitPopulationDirPath()+graph.getName()+config.getPopulationExtension());
        	//createInitPopulationAfterGeneticOp();

            
            stats = new Statistic(basePopulation);
            stats.printInitPopulationData();
            //savePopulationToFile(basePopulation,config.getInitPopulationDirPath()+graph.getName()+config.getPopulationExtension());
            
            
            for(int i=0;i<20;i++){
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
   	           stopTimer=System.currentTimeMillis();
   	           time=stopTimer-startIterationTimer;
   	           System.out.println("IT TIME:  "+ time + " ms");
   	           
   	           stats.insertData(basePopulation, tempPopulation);
   	           stats.printIterationData(i);
            }
	    }catch(CloneNotSupportedException ex){
	        ex.printStackTrace();
	    }catch(Exception ex){
	        ex.printStackTrace();
    }
	}
	
	public void createInitPopulationAfterGeneticOp() throws CloneNotSupportedException{
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
	public void generateInitPopulation() throws CloneNotSupportedException {
        
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
	 
	 public void mutateTempPopulation() throws CloneNotSupportedException {
	        
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
	 
	    public void localOptimization() throws CloneNotSupportedException{
	        
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
	    
	    public void localOptimizationAfterGeneticOperation() throws CloneNotSupportedException{
	        
	      
	        Graph subGraph = null;
	      
	        for(int i=0;i<tempPopulation.getSizePopulation();i++){
	           
	            subGraph= (Graph)graph.clone();
	            subGraph.loadChromosom(tempPopulation.getChromosom(i));
	            subGraph.extractionClique();
	            
	            
	            boolean[] booleanArrayVertex=subGraph.getBoolArrayVertex();
	            /*
	            boolean[] check = new boolean[booleanArrayVertex.length];
	            if(Arrays.equals(booleanArrayVertex, check)){
	                System.out.println("ERROR !!!!!!!!!!");
	                
	            } */
	            tempPopulation.getChromosom(i).update(booleanArrayVertex);
	            
	            subGraph.improvementClique(graph,tempPopulation.getChromosom(i));
	       

	            
	        }
	    }
	    
	    public void reproduction() throws CloneNotSupportedException,Exception{
	        
	        pairForCrossing.clear();
	        tempPopulation.clear();
	        pairForCrossing=reproduction.getPairForCrossing(basePopulation);
	        //repro.check();
	        //pairForCrossing=repro.rank(this.basePopulation);
	        
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
	    
	    public void prepareNextPopulation() throws CloneNotSupportedException{
	    	
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

		private void elitarySuccesion() {
			
	    	Chromosom[] basePopulationSortTable=basePopulation.sortByFitness();
	    	Chromosom[] tempPopulationSortTable=tempPopulation.sortByFitness();
	    	
	    	Population newPopulation = new Population(basePopulation.getSizePopulation());
	    	for(int i=0;i<size_of_elite;i++){
	    		newPopulation.addChromosom(basePopulationSortTable[i]);
	    	}
	    	
	    	for(int i=0;i<basePopulation.getSizePopulation()-size_of_elite;i++){
	    		newPopulation.addChromosom(tempPopulationSortTable[i]);
	    		
	    	}
	    	
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
	    		writer.append("s AVG FITNESS: " +pop.getAvgFitness() + "  BEST FITNESS: " + pop.getBestChromosome().getFitnes());
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
	    			if(sCurrentLine.charAt(0)=='s'){
	    				continue;
	    			}
	    			
	    			Chromosom tmpChrom= new Chromosom(sCurrentLine);
	    			basePopulation.addChromosom(tmpChrom);
	    		}
	    		reader.close();
	    	}catch(IOException ex){
	    		System.out.println("Error loading file");
	    	}
	    }
	    
	    
}
