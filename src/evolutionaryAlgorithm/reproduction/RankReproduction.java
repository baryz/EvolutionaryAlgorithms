package evolutionaryAlgorithm.reproduction;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import distribution.RandomByDistribution;
import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.Population;

public class RankReproduction extends Reproduction{
	
	
	@Override
	public ArrayList<Integer> getPairForCrossing(Population inPop){
		
		if((inPop.getSizePopulation()%2) == 1) throw new NullPointerException();
		
		ArrayList<Integer> result = new ArrayList<>();
		Hashtable<Chromosom,Integer> source= new Hashtable<>();
		for(int i=0; i < inPop.getSizePopulation(); i++){
			source.put( inPop.getChromosom(i),i);
		}
		
		Chromosom[] chrom= inPop.sortByFitness();
        
        double[] probab = evalProbabByRank(chrom.length);
        RandomByDistribution grbd= new RandomByDistribution(chrom.length);
        grbd.setProbability(probab);
        Random randGen = new Random();
        
       for(int i=0;i<chrom.length;i++){
        	double randDouble = randGen.nextDouble();
            int indexOfChromInSortTable = grbd.getIndex(randDouble);
            int indexOfChromInPopulation = source.get(chrom[indexOfChromInSortTable]);
            result.add(indexOfChromInPopulation) ;
            //System.out.print( + source.get(chrom[result.get(i)]) + ", ");
        }
        
        
        return result;
		
	}
	
    public void check(){
        
        double rankMin=1.0;
       double rankMax=16.0;
       double sumRank=(rankMin+rankMax)/2;
       double downBound,upBound;
       System.out.println("Sumrank= "+sumRank);
       
       downBound=-1.0/(sumRank*(rankMax-1.0));
       upBound= (rankMax - 1.0)/(sumRank*(rankMax-1.0));
       
       System.out.println("downBound: "+downBound);
       System.out.println("upBound: "+ upBound);
       
       //double randParam = randGen.;
       
       double paramK = upBound;//2.0/51.0;
       double paramA = (1.0 -(sumRank-1.0)*paramK)/rankMax;
       
       System.out.println("Param K="+paramK+" Param A= "+paramA);
       double sum=0.0;
       double probab=0.0;
       for(int i=1;i<=rankMax;i++){
           //-----------------p. 120 Arabas ----------> 
           probab=paramA+paramK*(1.0-((i/rankMax)));
           sum+=probab;
          // System.out.println(i+". Pr= "+ probab + " Sum: "+ sum);
       }
   }
   
   public double[] evalProbabByRank(double rankMax){
      
       double[] result=new double[ (int)rankMax];
       double rankMin=1.0;
       double sumRank=(rankMin+rankMax)/2;
       double downBound,upBound;
       
       downBound=-1.0/(sumRank*(rankMax-1.0));
       upBound= (rankMax - 1.0)/(sumRank*(rankMax-1.0));
       double paramK = upBound;//2.0/51.0;
       double paramA = (1.0 -(sumRank-1.0)*paramK)/rankMax;
       
       //System.out.println("Param K="+paramK+" Param A= "+paramA);
       //double sum=0.0;
       double probab=0.0;
       for(int i=1;i<=rankMax;i++){
           probab=paramA+paramK*(1.0-((i/rankMax)));
           result[i-1]=probab;
           //sum+=probab;
           //System.out.println(i+". Pr= "+ probab + " Sum: "+ sum);
       }
       
       return result;
   }  

   public static void main(String [] args){
	   
	   

	   
	   
   }
}
