package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;


public class Statistic {

	
	private int[] resultInitPopulation;
	private String bestInitClique;
	
	private ArrayList<int[]> resultBasePopulation;
	private ArrayList<String> bestCliqueBasePopulation;
	
	private ArrayList<int[]> resultTempPopulation;
	private ArrayList<String> bestCliqueTempPopulation;
	private int noOfPopulation;
	//private int[] algorithmParameter; 
	
	public Statistic( Population initPop){
		
		resultInitPopulation = getResultTable(initPop);
		bestInitClique = initPop.getBestChromosome().getClique();
		
		resultBasePopulation = new ArrayList<>();
		bestCliqueBasePopulation = new ArrayList<>();
		
		resultTempPopulation = new ArrayList<>();
		bestCliqueTempPopulation = new ArrayList<>();
		
		insertData(initPop, initPop);
		
	
		
	}
	
	public void  insertData( Population basePop, Population tempPop ){
		
		resultBasePopulation.add(getResultTable(basePop));
		bestCliqueBasePopulation.add(basePop.getBestChromosome().getClique());
		resultTempPopulation.add(getResultTable(tempPop));
		bestCliqueTempPopulation.add(tempPop.getBestChromosome().getClique());
		noOfPopulation++;
	}
	
	public double getAvgInitPopulation(){
		
		return getAvgTable(resultInitPopulation);
	}
	
	public int getMaxFitnesInit( ){
		return getMax(resultInitPopulation);
	}
	
	public int getMaxFitnesBase(int index){
		return getMax(resultBasePopulation.get(index));
	}
	
	public int getMaxFitnesTemp(int index){
		return getMax(resultTempPopulation.get(index));
	}
	
	public void printInitPopulationData(){
		
    	System.out.println("BEST INIT RESULT : " + bestInitClique);
        System.out.println("BEST INIT FITNES : " + getMaxFitnesInit() );
        System.out.println("AVG INIT: " + getAvgInitPopulation());
	
	}
	
	public void printIterationData(int index){
		
		//System.out.println("ITERATION " + index);
		System.out.println("BEST BASE RESULT : " + getMaxFitnesBase(index));
       
        System.out.println("AVG BASE: " + getAvgBasePopulation(index));
		System.out.println("BEST TEMP RESULT : " + getMaxFitnesTemp(index));

        System.out.println("AVG TEMP: " + getAvgTempPopulation(index));
	}
	
	public double getAvgBasePopulation(int indexPop){
		return getAvgTable( resultBasePopulation.get(indexPop) );
	}
	
	public double getAvgTempPopulation(int indexPop){
		return getAvgTable( resultTempPopulation.get(indexPop) );
	}
	
	public double[] getAvgBasePopulationList(){
		
		double[] result = new double[resultBasePopulation.size()];
		for(int i=0; i < resultBasePopulation.size(); i++){
			result[i]= getAvgBasePopulation(i);
		}
		return result;
	}
	
	public int[] getBestChromBaseList(){
		int[] result = new int[resultBasePopulation.size()];
		for(int i=0; i< resultBasePopulation.size(); i++){
			result[i] = getMax(resultBasePopulation.get(i));
		}
		return result;
	}
	
	public double[] getAvgTempPopulationList(){
		
		double[] result = new double[resultTempPopulation.size()];
		for(int i=0; i < resultTempPopulation.size(); i++){
			result[i]= getAvgTempPopulation(i);
		}
		return result;
	}
	
	public int getNoOfPopulation(){
		return noOfPopulation;
	}
	
	private int[] getResultTable(Population inPop){
		int size = inPop.getSizePopulation();
		int[] result = new int[size];
		
		for(int i=0; i<size; i++){
			int fitnes=inPop.getChromosom(i).getFitnes();
			result[i] = fitnes;
		}
		
		return result;
	}
	
	private double getAvgTable( int[] inData){
		
		return Arrays.stream(inData).average().getAsDouble();
	}
	
	private int getMax( int[] inData){
		return Arrays.stream(inData).max().getAsInt();
	}
	
}
