package distribution;

import java.util.ArrayList;
import java.util.Random;

public class RandomWithoutDuplicate {
	public static int[] get(int size, int maxValue){
		int [] result = new int[size];
		ArrayList<Integer> mapNumber = new ArrayList<>();
		Random randEng = new Random();
		
		for(int i=0;i<maxValue;i++){
			mapNumber.add(i);
		}
		
		int rangeRandomInMap = mapNumber.size()-1;
		for(int i=0;i<size;i++){
			int randomIndex=randEng.nextInt(rangeRandomInMap);
			result[i]=mapNumber.get(randomIndex);
			mapNumber.set(randomIndex,mapNumber.get(rangeRandomInMap));
			rangeRandomInMap--;
		}
		
		
		return result;
	}
}
