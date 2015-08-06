package evolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Random;

import distribution.RandomByDistribution;

public class Reproduction {
	   public  ArrayList<Integer> rank(Population initPopul) throws NullPointerException{
	        
	        ArrayList<Integer> result= new ArrayList<>();
	        
	        if((initPopul.getSizePopulation()%2) == 1) throw new NullPointerException();
	        
	        Chromosom[] chrom= initPopul.sortByFitness();
	        double[] probab = evalProbabByRank(chrom.length);
	        RandomByDistribution grbd= new RandomByDistribution(chrom.length);
	        grbd.setProbability(probab);
	        Random randGen = new Random();
	        
	        for(int i=0;i<chrom.length;i++){
	            result.add(grbd.getIndex(randGen.nextDouble()));
	            
	        }
	        return result;
	    }
	    
	    public void getRankByRoulette(double[] prababArray){
	        
	    }
	    
	    public void check(){
	        Random randGen = new Random();
	         double rankMin=1.0;
	        double rankMax=64.0;
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
	            /*-----------------p. 120 Arabas ----------> */
	            probab=paramA+paramK*(1.0-((i/rankMax)));
	            sum+=probab;
	            System.out.println(i+". Pr= "+ probab + " Sum: "+ sum);
	        }
	    }
	    
	    private double[] evalProbabByRank(double rankMax){
	       
	        double[] result=new double[ (int)rankMax];
	        double rankMin=1.0;
	        double sumRank=(rankMin+rankMax)/2;
	        double downBound,upBound;
	        //System.out.println("Sumrank= "+sumRank);
	        downBound=-1.0/(sumRank*(rankMax-1.0));
	        upBound= (rankMax - 1.0)/(sumRank*(rankMax-1.0));
	        //System.out.println("downBound: "+downBound);
	        //System.out.println("upBound: "+ upBound);
	        double paramK = upBound;//2.0/51.0;
	        double paramA = (1.0 -(sumRank-1.0)*paramK)/rankMax;
	        
	        //System.out.println("Param K="+paramK+" Param A= "+paramA);
	        double sum=0.0;
	        double probab=0.0;
	        for(int i=1;i<=rankMax;i++){
	            probab=paramA+paramK*(1.0-((i/rankMax)));
	            result[i-1]=probab;
	            //System.out.println(i+". Pr= "+ probab + " Sum: "+ sum);
	        }
	        
	        return result;
	    }
}
