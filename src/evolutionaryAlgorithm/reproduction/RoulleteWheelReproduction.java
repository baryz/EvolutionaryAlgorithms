package evolutionaryAlgorithm.reproduction;

import java.util.ArrayList;
import java.util.Random;

import distribution.RandomByDistribution;
import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.Population;

public class RoulleteWheelReproduction extends Reproduction {

	@Override
	public ArrayList<Integer> getPairForCrossing(Population inPop) {
		
		ArrayList<Integer> result = new ArrayList<>();
		int size = inPop.getSizePopulation();
		
		double [] probab = evalProbab(inPop);
		RandomByDistribution grbd= new RandomByDistribution(size);
        grbd.setProbability(probab);
        Random randGen = new Random();
        
        for(int i=0; i<size; i++){
        	double randDouble = randGen.nextDouble();
        	int indexOfChrom = grbd.getIndex(randDouble);
        	result.add(indexOfChrom);

        }
		return result;
	}
	
	private double[] evalProbab( Population inPop){
		int size = inPop.getSizePopulation();
		double[] result = new double[size];
		
		int sumFitness = 0;
		for(Chromosom x:inPop){
			sumFitness+=x.getFitnes();
		}
		
		double probabChrom = 0.0;
		for(int i=0; i<size; i++){
			probabChrom = (double)inPop.getChromosom(i).getFitnes() / (double) sumFitness;
			result[i] = probabChrom;
		}
		
		return result;
	}
	
	public static void main(String [] args){
		   Population population=new Population(8);
		   Population selectParentPopulation = new Population(8);
		   
		   Chromosom[] chromTest={
				   new Chromosom("10100000"),new Chromosom("00110011"),new Chromosom("11001000"),new Chromosom("00111100"),
				   new Chromosom("11100011"),new Chromosom("11001100"),new Chromosom("11110000"), new Chromosom("11000111")
		   };
		   
		   for(Chromosom x:chromTest){
			   population.addChromosom(x);
		   }
		   
		   ReproductionFactory repFactory = new ReproductionFactory();
		   Reproduction repro =repFactory.produceReproduction(ReproductionType.ROULLETEWHEEL);
		   ArrayList<Integer> pair=repro.getPairForCrossing(population);
		   
		   for(int x:pair) System.out.print(x+", ");
		   
		   
		   for(int i=0; i<pair.size(); i++){
			   selectParentPopulation.addChromosom(population.getChromosom(pair.get(i)));
		   }
	}

}
