package evolutionaryAlgorithm;

import java.util.ArrayList;

public class Chromosom {
	
	  private Gen[] gens;
	    
	    
	    public Chromosom(int countGens){

	        gens=new Gen[countGens];
	        for(int i=0;i<countGens;i++){
	            gens[i]=new Gen();
	            gens[i].setLabel(i);
	        }
	    }
	    public Chromosom (String inputStrem){
	        gens=new Gen[inputStrem.length()];
	        for(int i=0;i<inputStrem.length();i++){
	            gens[i]=new Gen();
	            gens[i].setLabel(i);
	            if(inputStrem.charAt(i)=='1'){
	                gens[i].setValue();
	            }
	           
	        }
	        
	    }

	    public Chromosom( ArrayList<Integer> initSetA, int sizeChromosom){

	        gens = new Gen[sizeChromosom];
	        for(int i=0;i<gens.length;i++){
	            gens[i]=new Gen();
	            gens[i].setLabel(i);
	        }
	        for(Integer vertex:initSetA){
	            gens[vertex].setValue();
	        }
	    }
	    
	    
	    public Chromosom (ArrayList<Gen> initGen){
	       gens=new Gen[initGen.size()];
	        for(int i=0;i<gens.length;i++){
	            gens[i]= initGen.get(i);
	        }
	    }
	    public int getSize(){
	        return gens.length;
	    }
	    
	   public Gen getGen(int index){
	        
	        Gen result=new Gen();
	        result=gens[index];
	        return result;
	    }
	    
	    public boolean  setNegateGen(int indexGen){
	        boolean result=gens[indexGen].setValue();
	        return result;
	    }

	    public int getFitnes(){
	        int countOnes=0;
	        for(Gen item:gens){
	            if(item.getValue())
	                countOnes++;
	        }
	        return countOnes;
	    }
	    
	    public void update(boolean[] boolArrayVertex){
	        //if(getSize()!= boolArrayVertex.length) throw Exception;
	        for(int i=0;i<getSize();i++){
	            if(gens[i].getValue()!= boolArrayVertex[gens[i].getLabel()]){
	                gens[i].setValue();
	            }
	        }
	    }
	    
	    public ArrayList<Integer> getLabelList(){
	        ArrayList<Integer> result=new ArrayList<>();
	        for(int i=0;i<getSize();i++){
	            if(getGen(i).getValue()){
	                result.add(getGen(i).getLabel());
	            }
	        }
	        return result;
	    }
	    
	    public void print(){
	        System.out.print("Positio:");
	        for(int i=0;i<gens.length;i++){
	            System.out.format("%3d",i);
	        }
	        System.out.println("");
	        System.out.print("Genotyp: ");
	        for(int i=0;i<gens.length;i++){
	            if(gens[i].getValue()){
	                System.out.print(" 1 ");
	            }else {
	                System.out.print(" 0 ");
	            }

	        }
	        System.out.println("");
	        System.out.print("Labels: ");
	        for(int i=0;i<gens.length;i++){
	            System.out.format("%3d",gens[i].getLabel());
	        }
	        System.out.println("");

	    }
	    
	    public String getClique(){
	    	StringBuilder result= new StringBuilder();
	    	for(Gen x:gens){
	    		if(x.getValue()==true) result.append( x.getLabel()+", ");
	    	}
	    	return result.toString();
	    }
	    
	    @Override
	    public  Chromosom clone() throws CloneNotSupportedException{
	        Chromosom result=  new Chromosom(gens.length);
	        for(int i=0;i<gens.length;i++){
	            result.gens[i]= (Gen)this.gens[i].clone();
	        }
	    
	       return result;
	        
	    }
	    
	    
	    @Override
		public String toString(){
	    	StringBuilder resultValue= new StringBuilder();
	    	StringBuilder resultLabel= new StringBuilder();
	    	for(Gen x:gens){
	    		resultValue.append(String.format("%3d", x.getValue()? 1 : 0));
	    		resultLabel.append(String.format("%3d", x.getLabel()));
	    	}
	    	String result=resultValue.toString()+"\n"+resultLabel.toString();
	    	result+= "\n FITNESS : "+this.getFitnes();
	    	
	    	
	    	return result;
	    }
	    
	    public String getChromosomeCode(){
	    	StringBuilder resultValue= new StringBuilder();
	    	for(Gen x:gens){
	    		resultValue.append(String.format("%1d", x.getValue()? 1 : 0));
	    	
	    	}
	    	
	    	return resultValue.toString();
	    }
	    
	    public int getHammingDistance(Chromosom x){
	    	int result=0;
	    	for(int i=0;i<gens.length;i++){
	    		if(gens[i].getValue()!=x.gens[i].getValue()){
	    			result++;
	    		}
	    	}
	    	return result;
	    }
}
