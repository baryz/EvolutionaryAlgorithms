package evolutionaryAlgorithm;

import java.math.BigDecimal;
import java.math.MathContext;

public class MutationParam {
	private double[] params;
	
	public MutationParam(double[] inParam){
		if(inParam.length!=6) throw new IndexOutOfBoundsException("Invalid Count of Params");
		for( double param : inParam){
			if(param<0.0 || param >1.0) throw new IndexOutOfBoundsException("Invalid value of Param");
		}
		
		this.params = inParam.clone();
	}
	
	public double getInitSelectProb(){
		return params[0];
	}
	
	public double getInitMutateProb(){
		return params[1];
	}
	
	public double getOffSelectProb(){
		return params[2];
	}
	
	public double getOffMutateProb(){
		return params[3];
	}
	
	public double getStepOffSelectProb(){
		
		return params[4];
	}
	
	public double getStepOffMutateProb(){
		return params[5];
	}
	
	public boolean reduceOffSelectProb(){
		BigDecimal a = new BigDecimal(getOffSelectProb());
		BigDecimal b = new BigDecimal(getStepOffSelectProb());
		MathContext mc = new MathContext(6); // 6 precision
		
		if( getOffSelectProb() <= getStepOffSelectProb() ){
			return false;
		}else{
			params[2] = a.subtract(b,mc).doubleValue();
			return true;
		}
	}
	public boolean reduceOffMutateProb(){
		BigDecimal a = new BigDecimal(getOffMutateProb());
		BigDecimal b = new BigDecimal(getStepOffMutateProb());
		MathContext mc = new MathContext(6); // 6 precision
		
		if( getOffMutateProb() <= getStepOffMutateProb() ){
			return false;
		}else{
			params[3] = a.subtract(b,mc).doubleValue();
			return true;
		}
	}
}
