package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import evolutionaryAlgorithm.Chromosom;
import evolutionaryAlgorithm.Gen;

public class Graph implements Cloneable {
    
    private boolean[][] edge;
    private int noVertex;
    private int[] degreeVertex;
    private String name;
    
    public String filename;
    
    @SuppressWarnings("finally")
	private int getCountVertexWithFile(String fileName){
        int resultNoVertex=0;
        BufferedReader br =null;
          try{
              br= new BufferedReader(new FileReader(fileName));
              String sCurrentLine;
               while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.contains("p edge")){
                        String[] lineParts=sCurrentLine.split(" ");
                        lineParts = Arrays.stream(lineParts).
                        		filter( s-> ( s.length() > 0 )).toArray(String[]::new);
                        if(lineParts[2]!=null){
                           resultNoVertex =Integer.parseInt(lineParts[2]);
                         }
                    }
                }
            
            br.close();
            return resultNoVertex;
          }catch(IOException ex){
              ex.printStackTrace();
          }finally{
             return resultNoVertex;
          }
    }
    
    
    public Graph(int inNoVertex){
        noVertex=inNoVertex;
        edge = new boolean[noVertex][noVertex];
        degreeVertex = new int[noVertex];
    }
    
    public Graph(String filename,String name){
    	this.setName(name);
        noVertex=getCountVertexWithFile(filename);
        edge = new boolean[noVertex][noVertex];
        degreeVertex=new int[noVertex];
        this.filename=filename;
        BufferedReader br=null;
        try{
            br= new BufferedReader(new FileReader(filename));
            String sCurrentLine;
            
            while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.charAt(0)=='e'){
                        String[] lineParts = sCurrentLine.split(" ");
                        int noVertexA=Integer.parseInt(lineParts[1]);
                        int noVertexB=Integer.parseInt(lineParts[2]);
                        setEdge(noVertexA-1, noVertexB-1);
                    }
            }
        } catch(IOException e) {
             e.printStackTrace();
     }
    }
    
    
    public void setName(String graphName){
    	this.name=graphName;
    }
    
    public String getName(){
    	return this.name;
    }
    
    private void setEdge(int noVertexA,int noVertexB){
        edge[noVertexA][noVertexB] = true;
        edge[noVertexB][noVertexA] = true;
        degreeVertex[noVertexA]++;
        degreeVertex[noVertexB]++;
    }
        
    public boolean hasEdge(int noVertexA, int noVertexB){
    	return edge[noVertexA][noVertexB] && edge[noVertexB][noVertexA];
    }
    public int getNoVertex(){
        return  noVertex;
    }
    
    public ArrayList<Integer>  getMinDegreeVertex(){
        ArrayList<Integer> result=new ArrayList<>();
        if(noVertex==0) return result;
        int minResult=degreeVertex.length;
        
        for(int i=0;i<degreeVertex.length;i++){
            if(degreeVertex[i]<minResult && degreeVertex[i]!=0){
                minResult=degreeVertex[i];
                result.clear();
                result.add(i);
            }else if(degreeVertex[i]==minResult){
                result.add(i);
            }
        }
        return result;
    }
    
    public int[] getAdjacencyList(int inNoVertex){
        int[] result;
        int countNeighbours=getDegreeVertex(inNoVertex);
        result=new int[countNeighbours];
        int counter=0;
        for(int i=0;i<edge.length;i++){
            if(edge[inNoVertex][i]){
                result[counter]=i;
                counter++;
            }
        }
        return result;
    }
    
    public ArrayList<Integer> getAdjacencyArrayList(int inNoVertex){
        
        int countNeighbours=getDegreeVertex(inNoVertex);
        ArrayList<Integer> result=new ArrayList<>(countNeighbours);
       
        for(int i=0;i<noVertex;i++){
            if(edge[inNoVertex][i])
                result.add(i);
               
            
        }
        return result;
    }
    public int getDegreeVertex(int noVertex){
        return degreeVertex[noVertex];
    }
    
    
    public void printEdge(){
        System.out.println("Edges:");
        System.out.print("   ");
        for(int i=0;i<edge.length;i++) {
            
            System.out.format("%3d",i);
        }
        System.out.println();
        for(int i=0;i<edge.length;i++){
            System.out.format("%3d",i);
            for(int j=0;j<edge.length;j++){
                if(edge[i][j]){
                    System.out.format("%3d",1);
                }else{
                    System.out.format("%3d",0);
                }
                
            }
            System.out.println();
        }
    }
    public void printDegreeVertex(){
        System.out.println("Degre Vertexes:");
        for(int i=0;i<degreeVertex.length;i++){
            System.out.format("%3d",i);
            
        }
        System.out.println();
        for(int i=0;i<degreeVertex.length;i++){
            System.out.format("%3d",degreeVertex[i]);
        }
        System.out.println();
    }

    
   public static void main(String[] args) {  

        
 
   }
   
	public boolean checkClique(ArrayList<Integer> setA, int inputNoVertex) {
	    
	    if(setA.size()<1){
	        return false;
	    }
	    for(Integer vertex:setA){
	       if(!(edge[inputNoVertex][vertex])){
	           return false;
	        }
	    }
	    
	    return true;
	}



	public void  extractionClique(){
	    ArrayList<Integer> minVertexList=null;
	    Random randomGenerator=new Random();
	    int randomInt=0;
	   
	    while(!(isClique())){
	        minVertexList=getMinDegreeVertex();
	        if(minVertexList.size()==0) break;
	        randomInt=randomGenerator.nextInt(minVertexList.size());
	        int noOfVertex = minVertexList.get(randomInt);
	        removeEdgeOfVertex(noOfVertex);
	       
	    }
	    
	}


	public int improvementClique (Graph inGraph,Chromosom inChrom) 
	        throws CloneNotSupportedException{
	    
	    int countOfImprove=0;
	    Random randGen=new Random();
	    int randPosition =  randGen.nextInt(inChrom.getSize()-1);
	   
	    for(int i=randPosition;i<inChrom.getSize();i++){
	        Gen tmpGen= (Gen) inChrom.getGen(i).clone();
	        if(!(tmpGen.getValue())){
	            if(inGraph.checkClique(inChrom.getLabelList(),tmpGen.getLabel())){
	                inChrom.getGen(i).setValue();
	                countOfImprove++;
	            }
	        }
	    }
    
	    for(int i=0;i<randPosition;i++){
	        Gen tmpGen= (Gen) inChrom.getGen(i).clone();
	        if(!(tmpGen.getValue())){
	            if(inGraph.checkClique(inChrom.getLabelList(),tmpGen.getLabel())){
	                inChrom.getGen(i).setValue();
	                countOfImprove++;
	            }
	        }
	    }
	    return countOfImprove;
	}



	public boolean isClique() {
	  
		boolean isEmptyGraph=true;
	   for(int degree:degreeVertex){
		   if(degree!=0) isEmptyGraph=false;
	       if(degree!=0 && degree!=(noVertex-1))
	           return false;
	   }
	   if(isEmptyGraph) return false;
	   return true;
	}


	public void loadChromosom (Chromosom inputChrom) throws CloneNotSupportedException{
	    Gen tmpGen=null;
	    int vertex;
	    for(int i=0;i<inputChrom.getSize();i++){
	        if(!(inputChrom.getGen(i).getValue())){
	            tmpGen=(Gen)inputChrom.getGen(i).clone();
	            vertex=tmpGen.getLabel();
	            this.removeEdgeOfVertex(vertex);
	        }
	    }
	}


	private void removeEdgeOfVertex(int inputNoVertex){
	   
	   if(degreeVertex[inputNoVertex]==0) {
	       return;
	   }
	   
	   for(int i=0;i<degreeVertex.length;i++){
	        if(this.edge[i][inputNoVertex]){
	            this.degreeVertex[inputNoVertex]--;
	            this.degreeVertex[i]--;
	            if(this.degreeVertex[i]==0){ 
	                noVertex--;
	            }
	            this.edge[i][inputNoVertex]=false;
	            this.edge[inputNoVertex][i]=false;
	        }
	        
	    }
	    noVertex--;
	}

	public boolean[] getBoolArrayVertex(){
	    
	    boolean[] emptyRow = new boolean[edge.length];
	    boolean[] result = new boolean[edge.length];
	    for(int i=0;i<edge.length;i++){
	        if(!(Arrays.equals(edge[i], emptyRow))){
	            result[i]=true;
	        }
	    }
	    
	    return result;
	}
	
	
	public boolean checkClique(int[] vertexTable){
		for(int i=0; i<vertexTable.length; i++){
			for(int j=i+1; j<vertexTable.length; j++){
				if( ! hasEdge( vertexTable[i], vertexTable[j])) return false; 
			}
		}
		return true;
	}
	@Override
	public  Graph clone() throws CloneNotSupportedException{
	    Graph result=  new Graph(noVertex);
	    for(int i=0;i<noVertex;i++){
	        result.edge[i]= (boolean[])this.edge[i].clone();
	    }
	 
	    result.degreeVertex=(int[]) this.degreeVertex.clone();
	    return result;
	    
	}
}
