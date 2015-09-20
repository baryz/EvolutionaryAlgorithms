package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class Population implements Iterable<Chromosom> {
	 private final int id;
	 private  static int counter;
	 private ArrayList<Chromosom> chromosome;

     
     public Population(int inSizePopulation){
         
    	 id=counter;
    	 //System.out.println("CREATE POPULATION ID: "+ counter);
    	 counter++;
         chromosome = new ArrayList<>(inSizePopulation);
     }
     
     public int getSizePopulation(){
         return chromosome.size();
     }
     public void addChromosom(Chromosom chromosomeX){
         chromosome.add(chromosomeX);
     }
     
     public Chromosom getChromosom(int index){
         return chromosome.get(index);
     }
     
     public Chromosom setChromosom(int index,Chromosom chromosomX){
    	 
    	 Chromosom result=chromosome.set(index, chromosomX);
    	 return result;
     }
     
     public double getAvgFitness(){
         double result=0.0;
         for(Chromosom chrom:chromosome){
             result+=(double)chrom.getFitnes();
         }
         return result/chromosome.size();
     }
     
     public Chromosom getBestChromosome(){
    	 int bestFitness=0;
    	 
    	 Chromosom result = new Chromosom(chromosome.get(0).getSize());
    	 for(Chromosom x:chromosome){
    		 if( x.getFitnes()>bestFitness){
    			 result= x;
    			 bestFitness=x.getFitnes();
    		 }
    	 }
    	 
    	 return result;
     }
     
     public void clear(){
    	 chromosome.clear();
    }
     
     
     public Chromosom[] sortByFitness()  {
         Comparator<Chromosom> chromComparator= new Comparator<Chromosom>() {

             @Override
             public int compare(Chromosom chromA, Chromosom chromB) {
                 return Integer.compare(chromB.getFitnes(), chromA.getFitnes());
             }
         };
         
         Chromosom[] result= chromosome.toArray(new Chromosom[chromosome.size()]);
         Arrays.sort(result,chromComparator);
         
         return result;
     }
     
    @Override
    public  Population clone() throws CloneNotSupportedException{
        Population resultPopulation= new Population(this.getSizePopulation());
    	for(Chromosom x:this.chromosome){
    		resultPopulation.chromosome.add(x);
    	}
    	
        return resultPopulation;
        
    }
    


	@Override
	public Iterator<Chromosom> iterator() {
		Iterator<Chromosom> it= chromosome.iterator();
		return it;
		
	}
     
}
