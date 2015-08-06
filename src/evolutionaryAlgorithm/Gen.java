package evolutionaryAlgorithm;

public class Gen {

	 private boolean value;
     private int label;
     
     public Gen(){
         value=false;
         label=0;
     }
     public void setLabel(int inLabel){
         label=inLabel;
     }
     
     public boolean getValue(){
         return value;
     }
     
     public boolean setValue(){
         
         value=!value;
         return value;
     }
     
     public int getLabel(){
         return label;
     }
     
     @Override
     public Object clone() throws CloneNotSupportedException{
    	 Gen resultGen = new Gen();
    	 resultGen.value=this.value;
    	 resultGen.label=this.label;
    	 return resultGen;
        
     }
}
