package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Population {

	
	 private ArrayList<Chromosom> chromosome;
     //private Chromosom[] chromosome;
     //private int sizePopulation;
     
     public Population(int inSizePopulation){
         //sizePopulation=inSizePopulation;
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
     
     public double getAvgFitness(){
         double result=0.0;
         for(Chromosom chrom:chromosome){
             result+=(double)chrom.getFitnes();
         }
         return result/chromosome.size();
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
     
}
