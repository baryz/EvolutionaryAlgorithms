package distribution;

public class RandomByDistribution {
	 
	private double probability[];
	    
    public RandomByDistribution(int range){
        probability=new double[range];
    }
    
    public double setProbability(double[] probabArray){
        double sum=0.0;
        for(int i=0;i<probabArray.length;i++){
            sum+=probabArray[i];
            probability[i]=sum;
        }
        return sum;
    }
    
    public int getIndex(double random){
        
        for(int i=0;i<probability.length;i++){
            if(random<probability[i]){
                return i;
            }
        }
        return 0;
    }
}
