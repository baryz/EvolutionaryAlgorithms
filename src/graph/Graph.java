package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Graph implements Cloneable {
    
    private boolean[][] edge;
    private int noVertex;
    private int[] degreeVertex;
    
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
    
    public Graph(String fileName){
        noVertex=getCountVertexWithFile(fileName);
        edge = new boolean[noVertex][noVertex];
        degreeVertex=new int[noVertex];
        BufferedReader br=null;
        try{
            br= new BufferedReader(new FileReader(fileName));
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
    

    
    private void setEdge(int noVertexA,int noVertexB){
        edge[noVertexA][noVertexB] = true;
        edge[noVertexB][noVertexA] = true;
        degreeVertex[noVertexA]++;
        degreeVertex[noVertexB]++;
    }
        
    public int getNoVertex(){
        return  noVertex;
    }
    
    public ArrayList<Integer>  getMinDegreeVertex(){
        
        ArrayList<Integer> result=new ArrayList<>();
        int bestResult=degreeVertex.length;
        result.add(degreeVertex.length);
        for(int i=0;i<degreeVertex.length;i++){
            if(degreeVertex[i]<bestResult && degreeVertex[i]!=0){
                bestResult=degreeVertex[i];
                result.clear();
                result.add(i);
            }else if(degreeVertex[i]==bestResult){
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
    
    //System.out.println("Ocena osobnika: "+ inputChrom.getFitnes());
    //inputChrom.print();
    //loadChromosom(inputChrom);
    ArrayList<Integer> minVertexList=null;
    //System.out.println("Najmniejszy stopieñ: "+ getDegreeVertex(minVertexList.get(0)));
    
    Random randomGenerator=new Random();
   int randomInt=0;
   int i=0;
    while(!(isClique())){
        minVertexList=getMinDegreeVertex();
        randomInt=randomGenerator.nextInt(minVertexList.size());
        int noOfVertex = minVertexList.get(randomInt);
        removeEdgeOfVertex(noOfVertex);
        i++;
    }
    
}

/*
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

*/

public boolean isClique() {
  
   for(int degree:degreeVertex){
       if(degree!=0 && degree!=(noVertex-1))
           return false;
   }
   return true;
}

/*
public void loadChromosom (Chromosom inputChrom) throws CloneNotSupportedException{
    Gen tmpGen=null;
    int vertex;
    for(int i=0;i<inputChrom.getSize();i++){
        if(!(inputChrom.getGen(i).getValue())){
            tmpGen=(Gen)inputChrom.getGen(i).clone();
            //tmpGen.setLabel(10);
            vertex=tmpGen.getLabel();
            this.removeEdgeOfVertex(vertex);
        }
    }
}
*/

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
                //System.out.println("Usuwam wierzcholek: "+ i);
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
public void setTest(){
    noVertex=10;
    degreeVertex[0]=100;
    edge[1][1]=true;
}

@Override
public  Graph clone() throws CloneNotSupportedException{
    Graph result=  new Graph(noVertex);
    for(int i=0;i<noVertex;i++){
        result.edge[i]= (boolean[])this.edge[i].clone();
    }
    //result.edge= (boolean[][]) this.edge.clone();
    result.degreeVertex=(int[]) this.degreeVertex.clone();
    return result;
    
}
}
