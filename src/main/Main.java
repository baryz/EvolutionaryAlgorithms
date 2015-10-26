package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.ui.RefineryUtilities;

import chart.LineChart;
import config.Config;
import evolutionaryAlgorithm.EvolutionaryAlgorithm;
import evolutionaryAlgorithm.MutationParam;
import evolutionaryAlgorithm.Statistic;
import evolutionaryAlgorithm.crossover.CrossoverType;
import evolutionaryAlgorithm.reproduction.ReproductionType;
import evolutionaryAlgorithm.stopCondition.StopConditionType;
import evolutionaryAlgorithm.succession.SuccessionType;
import graph.Graph;

public class Main {

	public static void main(String[] args) {

		try{
			//checkCrossover(50);
			
			checkSuccession(50);
			//checkMutation(50);
			//checkEfficiency(50);
			//checkMutation(50);
		}catch(IOException ex){
			System.out.println("Error save result to file" + "_Crossover_");
		}
		
	}
	
	public static void checkEfficiency( int noOfRunning ) throws IOException{
		String[] graphNameTab= {"san200_0.7_1",/*"san200_0.9_1","san200_0.9_2","san400_0.9_1",
				"hamming8-4","keller5","san200_0.7_2","san200_0.9_3","san400_0.5_1","san400_0.7_1","san400_0.7_2","san400_0.7_3","san1000",
				"brock200_1","brock200_2","brock200_3","brock200_4",
    			"brock400_1","brock400_2","brock400_3","brock400_4",
    			"brock800_1","brock800_2","brock800_3","brock800_4",
    			"c-fat200-1","c-fat200-2","c-fat200-5",
    			"c-fat500-1","c-fat500-2","c-fat500-5","c-fat500-10",
    			"hamming6-2","hamming6-4","hamming8-2",				"hamming10-2","hamming10-4",
    			"johnson8-2-4","johnson16-2-4","johnson8-4-4","johnson32-2-4",
    			"keller4",
    			"MANN_a9","MANN_a27",
    			"p_hat300-1","p_hat300-2","p_hat300-3","p_hat500-1","p_hat500-2","p_hat500-3",
    			"p_hat700-1","p_hat700-2","p_hat700-3",
    			
    			"p_hat1000-3","p_hat1000-1","p_hat1000-2",
    			"p_hat1500-1","p_hat1500-2","p_hat1500-3",
    			
				"MANN_a81","keller6"*/}; 
		
		Config conf= new Config(true);
    	double[] mutationParamTable = {0.5, 0.2, 0.05, 0.9, 0.0, 0.1};
    	
    	FileWriter output = null;
    	BufferedWriter writer = null;
    	double sum=0.0,sumStd = 0.0;
		
		for(int k=0; k < graphNameTab.length; k++){
    		long start = System.currentTimeMillis();
    		Statistic[] statisticTable = new Statistic[noOfRunning];
			int[] resultTable = new int[noOfRunning];
			String algParam = "";
			String graphName= graphNameTab[k];
			String dirPath = "resourceData/result/" +graphName +"_";
			
			for (int i=0; i<noOfRunning; i++){
				long overallStartTimer, stopTimer,time;
				overallStartTimer=System.currentTimeMillis();
				
				Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
				MutationParam inMutationParam = new MutationParam(mutationParamTable );
				EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,10,
						ReproductionType.ROULLETEWHEEL,
						CrossoverType.MULTI_POINT,30,10,
						inMutationParam,10,
						SuccessionType.HAMMING_REPLACEMENT,
						StopConditionType.STAGNACY_CONDITION, 5,
						true, conf,
						5);

				alg.run();
				stopTimer=System.currentTimeMillis();
				time=stopTimer-overallStartTimer;
				System.out.println("Overall  time---------> "+ time + " MS");
				Statistic tmpStat = alg.getStatistic();
				statisticTable[i] = tmpStat;
				
				int[] resutltClique = alg.getResult();
				System.out.println(graphName + " RANK, " + 
						" COUNT OF RUN: "+i+ " SIZE: "+resutltClique.length+ 
						" No IT: "+ tmpStat.getNoOfPopulation()+
						" Avg Std: " + tmpStat.getAverageStdDevBasePopulation()+
						"  BEST CLIQUE: "+ Arrays.toString(resutltClique));
				
				if( i==0 ) dirPath += tmpStat.getSizePopulation() + ".txt";
				output = new FileWriter(dirPath,true);
				writer = new BufferedWriter(output);
				if( i==0 ){
					algParam = alg.getAlgParam();
					writer.append(algParam);
					writer.newLine(); 
				}
				System.out.println("<===========END WORK ALG =========>");
				String line = i+". Count "+ tmpStat.getNoOfPopulation()+" RESULT: " + alg.getResult().length
						+ " AVgStdDev: " + tmpStat.getAverageStdDevBasePopulation();
				System.out.println(line);
				writer.append(line);
				writer.newLine();
				writer.flush();
				writer.close();
				
				resultTable[i] = resutltClique.length;
				sum+= tmpStat.getNoOfPopulation();
				sumStd += tmpStat.getAverageStdDevBasePopulation();
			}
			dirPath =  "resourceData/result/"+graphName +"_"+statisticTable[0].getSizePopulation()+".txt";
			output = new FileWriter(dirPath,true);
			writer = new BufferedWriter(output);
			String line = "AVG NO ITERATE: " + (sum/noOfRunning);
			writer.append(line);
			System.out.println(line);
			writer.newLine();
			
			line = "AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble();
			writer.append(line);
			System.out.println(line);
			writer.newLine();
			
			line = "AVG (AVD STD DEV: " + (sumStd/noOfRunning) + "\n";
			writer.append(line);
			System.out.println(line);
			
			long stop = System.currentTimeMillis();
			line = "OVERALL TIME "+noOfRunning+" IT: " + (stop-start);
			writer.append(line);
			System.out.println(line);
			writer.flush();
			writer.close();
			
    	}
    	
	}
	
	public static void checkSuccession(int noOfRunning) throws IOException{
		SuccessionType[] succesionTypeTable = {SuccessionType.PART_REPLACEMENT,
				SuccessionType.ELITARY,SuccessionType.HAMMING_REPLACEMENT,SuccessionType.SIMPLY}; 
		
		for(int k=0; k < succesionTypeTable.length; k++){
			long start = System.currentTimeMillis();
			double[] mutationParamTable = {0.5, 0.2, 0.05, 0.9, 0.00, 0.1};
			Statistic[] statisticTable = new Statistic[noOfRunning];
			int[] resultTable = new int[noOfRunning];
			String algParam = "";
			Config conf= new Config();
			String graphName= "keller5";
			
			for (int i=0; i<noOfRunning; i++){
				long overallStartTimer, stopTimer,time;
				overallStartTimer=System.currentTimeMillis();
				
				  Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
			      MutationParam inMutationParam = new MutationParam(mutationParamTable );
			      EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,
			        						ReproductionType.RANK,
			        						CrossoverType.MULTI_POINT,30,10,
			        						inMutationParam,10,
			        						succesionTypeTable[k],
			        						StopConditionType.STAGNACY_CONDITION, 50,
			        						true, conf,
			        						5);
			        if(i==(noOfRunning-1)){
			        	algParam = alg.getAlgParam();
			        }
			        alg.run();
			       
			        stopTimer=System.currentTimeMillis();
			        time=stopTimer-overallStartTimer;
			        System.out.println("Overall  time---------> "+ time + " MS");
			        int[] resutltClique = alg.getResult();
					System.out.println("TOURNAMENT, size: " + 5+ 
							" COUNT OF RUN: "+i+ " SIZE: "+resutltClique.length+ 
							"  BEST CLIQUE: "+ Arrays.toString(resutltClique));
					System.out.println("select_prob: " +mutationParamTable[2] + " mutate_prob:"+mutationParamTable[3]);
					statisticTable[i] = alg.getStatistic();
			        resultTable[i] = resutltClique.length;
			}
			double sum=0.0;
			double sumStd = 0.0;
			String dirPath = "resourceData/result/" +LocalDate.now().toString()+"_"+ succesionTypeTable[k] +"_100.txt";
			Path newFile = Paths.get(dirPath);
			BufferedWriter writer = null;
		
			Files.deleteIfExists(newFile);
			newFile=Files.createFile(newFile);
			writer = Files.newBufferedWriter(newFile, Charset.defaultCharset());
			writer.append(algParam);
			writer.newLine(); 

			
			for(int i=0 ; i<noOfRunning; i++){
				int noOfIterate = statisticTable[i].getNoOfPopulation();
				double sumDev = 0.0;
				for(int j = 0 ; j < noOfIterate ; j++){
					double stdDev = statisticTable[i].getStdDevBasePopulation(j);
					sumDev += stdDev;
				}
				double avgStdDev = sumDev/noOfIterate;
				String line = i+". Count "+ noOfIterate+" RESULT: " +resultTable[i] + " AvgStdDev: " +avgStdDev;
				System.out.println(line);
				writer.append(line);
				writer.newLine();
				
				sum+= noOfIterate;
				sumStd +=avgStdDev;
			
			}
			String line = "AVG NO ITERATE: " + (sum/noOfRunning);
			writer.append(line);
			System.out.println(line);
			writer.newLine();
			
			line = "AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble();
			writer.append(line);
			System.out.println(line);
			writer.newLine();
			
			line = "AVG (AVD STD DEV: " + (sumStd/noOfRunning) + "\n";
			writer.append(line);
			System.out.println(line);
			
			long stop = System.currentTimeMillis();
			line = "OVERALL TIME 50IT: " + (stop-start);
			writer.append(line);
			System.out.println(line);
			writer.flush();
			writer.close();
			
			
			System.out.println("select_prob: " +mutationParamTable[2] + " mutate_prob:"+mutationParamTable[3]);
			System.out.println("AVG NO ITERATE: " + (sum/noOfRunning));
			System.out.println("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
			System.out.println("AVG (AVD STD DEV: " + (sumStd/noOfRunning) );
		}
			
		
	}
	
	
	public static void checkMutation(int noOfRunning) throws IOException{
		double[] offSelectProbTable = { 0.05, 0.1, 0.2,0.4, 0.5, 0.7,0.9};
		double[] offMutateProbTable = {0.05, 0.1, 0.2, 0.4, 0.5, 0.7, 0.9};
		
		for(int iter = 0 ; iter <  offSelectProbTable.length; iter++){
			for( int iter1 = 0; iter1 < offMutateProbTable.length; iter1++){
				long start = System.currentTimeMillis();
				double[] mutationParamTable = {0.5, 0.2, offSelectProbTable[iter], offMutateProbTable[iter1], 0.00, 0.00};
				int countOfRunning = noOfRunning;
				
				Statistic[] statisticTable = new Statistic[countOfRunning];
				int[] resultTable = new int[countOfRunning];
				String algParam = "";
				Config conf= new Config();
				String graphName= "keller5";
				
				for (int i=0; i<countOfRunning; i++){
					long overallStartTimer, stopTimer,time;
					overallStartTimer=System.currentTimeMillis();
			        
			        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
			        MutationParam inMutationParam = new MutationParam(mutationParamTable );
			        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,
			        						ReproductionType.TOURNAMENT,
			        						CrossoverType.MULTI_POINT,20,0,
			        						inMutationParam,0,
			        						SuccessionType.SIMPLY,
			        						StopConditionType.STAGNACY_CONDITION, 50,
			        						true, conf,
			        						5);
			        if(i==(countOfRunning-1)){
			        	algParam = alg.getAlgParam();
			        }
			        alg.run();
			       
			        stopTimer=System.currentTimeMillis();
			        time=stopTimer-overallStartTimer;
			        System.out.println("Overall  time---------> "+ time + " MS");
			        int[] resutltClique = alg.getResult();
					System.out.println("TOURNAMENT, size: " + 5+ 
							" COUNT OF RUN: "+i+ " SIZE: "+resutltClique.length+ 
							"  BEST CLIQUE: "+ Arrays.toString(resutltClique));
					System.out.println("select_prob: " +mutationParamTable[2] + " mutate_prob:"+mutationParamTable[3]);
					statisticTable[i] = alg.getStatistic();
			        resultTable[i] = resutltClique.length;
				}
				
				double sum=0.0;
				double sumStd = 0.0;
				String dirPath = "resourceData/result/" +LocalDate.now().toString()+
						"Muta_"+ offSelectProbTable[iter] +"_"+offMutateProbTable[iter1]  + "_100.txt";
				Path newFile = Paths.get(dirPath);
				BufferedWriter writer = null;
			
				Files.deleteIfExists(newFile);
				newFile=Files.createFile(newFile);
				writer = Files.newBufferedWriter(newFile, Charset.defaultCharset());
				writer.append(algParam);
				writer.newLine(); 

				
				for(int i=0 ; i<countOfRunning; i++){
					int noOfIterate = statisticTable[i].getNoOfPopulation();
					double sumDev = 0.0;
					for(int j = 0 ; j < noOfIterate ; j++){
						double stdDev = statisticTable[i].getStdDevBasePopulation(j);
						sumDev += stdDev;
					}
					double avgStdDev = sumDev/noOfIterate;
					String line = i+". Count "+ noOfIterate+" RESULT: " +resultTable[i] + " AvgStdDev: " +avgStdDev;
					System.out.println(line);
					writer.append(line);
					writer.newLine();
					
					sum+= noOfIterate;
					sumStd +=avgStdDev;
				
				}
				String line = "AVG NO ITERATE: " + (sum/countOfRunning);
				writer.append(line);
				System.out.println(line);
				writer.newLine();
				
				line = "AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble();
				writer.append(line);
				System.out.println(line);
				writer.newLine();
				
				line = "AVG (AVD STD DEV: " + (sumStd/countOfRunning) + "\n";
				writer.append(line);
				System.out.println(line);
				
				long stop = System.currentTimeMillis();
				line = "OVERALL TIME 50IT: " + (stop-start);
				writer.append(line);
				System.out.println(line);
				writer.flush();
				writer.close();
				
				
				System.out.println("select_prob: " +mutationParamTable[2] + " mutate_prob:"+mutationParamTable[3]);
				System.out.println("AVG NO ITERATE: " + (sum/countOfRunning));
				System.out.println("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
				System.out.println("AVG (AVD STD DEV: " + (sumStd/countOfRunning) );
			}
		}
	}
	
	public static void checkCrossover(int noOfRunning) throws IOException{
		int[] noOfCuttingTable = {2,5,10,20,30,40,50,75,100,150,200 };
		double[] mutationParamTable = {0.5, 0.2, 0.05, 0.9, 0.00, 0.1};
		
		
		for(int k=0; k<noOfCuttingTable.length; k++){
			long start = System.currentTimeMillis();
			int countOfRunning = noOfRunning;
			Statistic[] statisticTable = new Statistic[countOfRunning];
			int[] resultTable = new int[countOfRunning];
			String algParam = "";
			Config conf= new Config();
			String graphName= "keller5";
			for (int i=0; i<countOfRunning; i++){
				long overallStartTimer, stopTimer,time;
				overallStartTimer=System.currentTimeMillis();
		        
		        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
		        MutationParam inMutationParam = new MutationParam(mutationParamTable );
		        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,
		        						ReproductionType.TOURNAMENT,
		        						CrossoverType.MULTI_POINT,noOfCuttingTable[k],10,
		        						inMutationParam,10,
		        						SuccessionType.SIMPLY,
		        						StopConditionType.STAGNACY_CONDITION, 50,
		        						true, conf,
		        						5);
		        if(i==(countOfRunning-1)){
		        	algParam = alg.getAlgParam();
		        }
		        alg.run();
		       
		        stopTimer=System.currentTimeMillis();
		        time=stopTimer-overallStartTimer;
		        
		        System.out.println("Overall  time---------> "+ time + " MS");
		        int[] resutltClique = alg.getResult();
				System.out.println(" COUNT OF RUN: "+i+ " SIZE: "+resutltClique.length+ 
						" BEST CLIQUE: "+ Arrays.toString(resutltClique));
				statisticTable[i] = alg.getStatistic();
		        resultTable[i] = resutltClique.length;
		        
			}
			
			double sum=0.0;
			double sumStd = 0.0;
			String dirPath = "resourceData/result/" +LocalDate.now().toString()+ "Crossover_"+noOfCuttingTable[k]  + ".txt";
			Path newFile = Paths.get(dirPath);
			BufferedWriter writer = null;
			
				Files.deleteIfExists(newFile);
				newFile=Files.createFile(newFile);
				writer = Files.newBufferedWriter(newFile, Charset.defaultCharset());
				writer.append(algParam);
				writer.newLine(); 
				for(int i=0 ; i<countOfRunning; i++){
					int noOfIterate = statisticTable[i].getNoOfPopulation();
					double sumDev = 0.0;
					for(int j = 0 ; j < noOfIterate ; j++){
						double stdDev = statisticTable[i].getStdDevBasePopulation(j);
						sumDev += stdDev;
					}
					double avgStdDev = sumDev/noOfIterate;
					String line = i+". Count "+ noOfIterate+" RESULT: " +resultTable[i] + " AvgStdDev: " +avgStdDev;
					System.out.println(line);
					writer.append(line);
					writer.newLine();
					sum+= noOfIterate;
					sumStd +=avgStdDev;
				
				}
				
				writer.append("AVG NO ITERATE: " + (sum/countOfRunning));
				System.out.println("AVG NO ITERATE: " + (sum/countOfRunning));
				writer.newLine();
				
				writer.append("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
				System.out.println("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
				writer.newLine();
				
				writer.append("AVG (AVD STD DEV: " + (sumStd/countOfRunning) + "\n");
				System.out.println("AVG (AVD STD DEV: " + (sumStd/countOfRunning) );
				
				long stop = System.currentTimeMillis();
				writer.append("OVERALL TIME 50IT: " + (stop-start));
				System.out.println("OVERALL TIME 50IT: " + (stop-start));
				writer.flush();
				writer.close();
		  
			

	}
	}
	/*Set loadPopWithFile
	 * 
	 */
	public static void checkReproductionInTheSamePop(){
		int countOfRunning = 10 ;
		Statistic[] statisticTable = new Statistic[countOfRunning];
		int[] resultTable = new int[countOfRunning];
		
		Config conf= new Config();
		double[] mutationParamTable = {0.5, 0.2, 0.4, 0.2, 0.05, 0.05};
		
		String graphName= "keller5";
		for (int i=0; i<countOfRunning; i++){
			long overallStartTimer, stopTimer,time;
			overallStartTimer=System.currentTimeMillis();
	        
	        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
	        MutationParam inMutationParam = new MutationParam(mutationParamTable );
	        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,
	        						ReproductionType.TOURNAMENT,
	        						CrossoverType.ONE_POINT,1,0,
	        						inMutationParam,0,
	        						SuccessionType.SIMPLY,
	        						StopConditionType.STAGNACY_CONDITION, 50,
	        						true, conf,
	        						50);
	        alg.run();
	       
	        stopTimer=System.currentTimeMillis();
	        time=stopTimer-overallStartTimer;
	        System.out.println("Overall  time---------> "+ time + " MS");
	        int[] resutltClique = alg.getResult();
			System.out.println(" COUNT OF RUN: "+i+ "\n SIZE: "+resutltClique.length+ 
					" \n BEST CLIQUE: "+ Arrays.toString(resutltClique));
	        //System.out.println("is Clique: " + graphEx.checkClique(alg.getResult()));
			
			LineChart avgFit = new LineChart(i+" Œrednie przystosowanie, best chromosome",alg.getStatistic());
	        avgFit.pack();
	        RefineryUtilities.centerFrameOnScreen(avgFit);
	        avgFit.setVisible(true); 
	        statisticTable[i] = alg.getStatistic();
	        resultTable[i] = resutltClique.length;
		}
		
		double sum=0.0;
		double sumStd = 0.0;
		
		for(int i=0 ; i<countOfRunning; i++){
			int noOfIterate = statisticTable[i].getNoOfPopulation();
			double sumDev = 0.0;
			for(int j = 0 ; j < noOfIterate ; j++){
				double stdDev = statisticTable[i].getStdDevBasePopulation(j);
				sumDev += stdDev;
			}
			double avgStdDev = sumDev/noOfIterate;
			System.out.println(i+". Count IT: "+ noOfIterate+" RESULT: " +
			resultTable[i] + " AVG STD DEV: " +avgStdDev);
			sum+= noOfIterate;
			sumStd +=avgStdDev;
		
		}
		
		System.out.println("AVG NO ITERATE: " + (sum/countOfRunning));
		System.out.println("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
		System.out.println("AVG (AVD STD DEV: " + (sumStd/countOfRunning) );
		
		
		int[] sizePopTable = new int[statisticTable.length];
		for(int i=0;i<sizePopTable.length;i++) sizePopTable[i] = statisticTable[i].getNoOfPopulation();
		int maxSizeNoPop = Arrays.stream(sizePopTable).max().getAsInt();
		
		double[][] avgFitInBasePop = new double[maxSizeNoPop][3];
		for(int i=0; i<countOfRunning; i++){
			for(int j=0;j < statisticTable[i].getNoOfPopulation();j++){
				avgFitInBasePop[j][0]+=statisticTable[i].getAvgBasePopulation(j);
				avgFitInBasePop[j][1]++;
				avgFitInBasePop[j][2] = avgFitInBasePop[j][0]/avgFitInBasePop[j][1];
			}
			
		}
		
		
		for(int i=0; i<countOfRunning; i++){
			System.out.print("Research No: "+ i +";");
			for(int j=0;j<statisticTable[i].getNoOfPopulation(); j++){
				System.out.print(statisticTable[i].getAvgBasePopulation(j) + ";");
			}
			System.out.println();
		}
	}
	
	public static void checkTournamentReproduction(){
		
	int[] sizeTournament = {30,40,60,70,80,90,100};
	for(int iter = 0 ; iter <  sizeTournament.length; iter++){
			int countOfRunning = 50;
			Statistic[] statisticTable = new Statistic[countOfRunning];
			int[] resultTable = new int[countOfRunning];
			
			Config conf= new Config();
			double[] mutationParamTable = {0.5, 0.2, 0.4, 0.2, 0.05, 0.05};
		
			String graphName= "keller5";
			for (int i=0; i<countOfRunning; i++){
				long overallStartTimer, stopTimer,time;
				overallStartTimer=System.currentTimeMillis();
		        
		        Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
		        MutationParam inMutationParam = new MutationParam(mutationParamTable );
		        EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,100,
		        						ReproductionType.TOURNAMENT,
		        						CrossoverType.ONE_POINT,1,0,
		        						inMutationParam,0,
		        						SuccessionType.SIMPLY,
		        						StopConditionType.STAGNACY_CONDITION, 50,
		        						true, conf,
		        						sizeTournament[iter]);
		        alg.run();
		       
		        stopTimer=System.currentTimeMillis();
		        time=stopTimer-overallStartTimer;
		        System.out.println("Overall  time---------> "+ time + " MS");
		        int[] resutltClique = alg.getResult();
				System.out.println("TOURNAMENT, size: " + sizeTournament[iter]+ 
						" COUNT OF RUN: "+i+ "\n SIZE: "+resutltClique.length+ 
						" \n BEST CLIQUE: "+ Arrays.toString(resutltClique));
				statisticTable[i] = alg.getStatistic();
		        resultTable[i] = resutltClique.length;
			}
			
			double sum=0.0;
			double sumStd = 0.0;
			
			for(int i=0 ; i<countOfRunning; i++){
				int noOfIterate = statisticTable[i].getNoOfPopulation();
				double sumDev = 0.0;
				for(int j = 0 ; j < noOfIterate ; j++){
					double stdDev = statisticTable[i].getStdDevBasePopulation(j);
					sumDev += stdDev;
				}
				double avgStdDev = sumDev/noOfIterate;
				System.out.println(i+". Count IT: "+ noOfIterate+" RESULT: " +
				resultTable[i] + " AVG STD DEV: " +avgStdDev);
				sum+= noOfIterate;
				sumStd +=avgStdDev;
			
			}
			System.out.println("TOURNAMENT, size: " + sizeTournament[iter]);
			System.out.println("AVG NO ITERATE: " + (sum/countOfRunning));
			System.out.println("AVG RESULT: " + Arrays.stream(resultTable).average().getAsDouble());
			System.out.println("AVG (AVD STD DEV: " + (sumStd/countOfRunning) );
		}  
	}
	
	public static void checkSeries(){
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
		double[] mutationParamTable = {0.5, 0.2, 0.4, 0.2, 0.05, 0.05};
		
		HashMap<String, Object[]> summaryResult = new HashMap<>();
		
		for( String graphName: graphNameTab){
		
		System.out.println( "--------------------Graph name : "+ graphName+ "--------------------");
		Graph graphEx=new Graph(conf.getInGraphDirPath()+ graphName + conf.getInGraphExtension(),graphName);
		MutationParam inMutationParam = new MutationParam(mutationParamTable );
		EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(graphEx,50,
				ReproductionType.ROULLETEWHEEL,
				CrossoverType.MULTI_POINT,10,0,
				inMutationParam,0,
				SuccessionType.HAMMING_REPLACEMENT,
				StopConditionType.STAGNACY_CONDITION, 5,
				true,conf);
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
	}

}
