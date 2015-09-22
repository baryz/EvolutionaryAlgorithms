package evolutionaryAlgorithm.reproduction;



public class ReproductionFactory implements IReproductionFactory {

	@Override
	public Reproduction produceReproduction(ReproductionType type, int... quantityTournament){
		Reproduction resultReproduction=null;
		
		switch(type){
			case RANK:{
				resultReproduction=new RankReproduction();
				break;
			}
			case TOURNAMENT:{
				resultReproduction = new TournamentReproduction(quantityTournament);
				break;
			}
			case ROULLETEWHEEL:{
				resultReproduction = new RoulleteWheelReproduction();
				break;
			}
		}
		
		return resultReproduction;
	}
}
