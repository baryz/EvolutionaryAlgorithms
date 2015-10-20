package config;

public class Config {
	
    private final String inGraphDirPath;
    private final String initPopDirPath;
    private final String inGraphExtension;
    private final String populationExtension;
    private final boolean debugFlag;
    public Config(){
    	
    	this.inGraphDirPath="resourceData/graph/";
    	this.initPopDirPath="resourceData/initPopulation/";
    	this.inGraphExtension = ".clq";
    	this.populationExtension=".pop";
    	this.debugFlag = false;
    }
    
	public Config (boolean debugFlag ){
    	this.inGraphDirPath="resourceData/graph/";
    	this.initPopDirPath="resourceData/initPopulation/";
    	this.inGraphExtension = ".clq";
    	this.populationExtension=".pop";
    	this.debugFlag = debugFlag;
    }

	public Config (String inGraphDirectory, String initPopulationDirectory,
			String graphExtension, String populationExtension, boolean debugFlag  ){
    	this.inGraphDirPath=inGraphDirectory;
    	this.initPopDirPath=initPopulationDirectory;
    	this.inGraphExtension = graphExtension;
    	this.populationExtension=populationExtension;
    	this.debugFlag = debugFlag;
    }
    

	
	public String getInGraphDirPath(){
		return this.inGraphDirPath;
	}
	
	public String getInitPopulationDirPath(){
		return this.initPopDirPath;
	}

	public String getInGraphExtension(){
		return this.inGraphExtension;
	}
	
	public String getPopulationExtension(){
		return this.populationExtension;
	}
	
	public boolean isDebug(){
		return this.debugFlag;
	}
}
