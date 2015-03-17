package yahtzee;

/**
 * An algorithm that plays yahtzee optimally.
 */
public class OptimizedAlgo {

	/**
	 * Evaluates the score based on the given dices. 
	 */
	public static void play(Dice[] dices, ScoreCard sc){
		boolean[] playableScore = new boolean[16];
		for(int i = 1; i<16; i++){//row 0 is not used in scorecard.
			if(sc.getRowScore(i)==-1){
				playableScore[i]=true;
			}else{
				playableScore[i]=false;
			}
		}
		for(int i=2; i>0; i--){
			evaluate(dices, playableScore, i);
		}
		
	}
	/**
	 * Evaluates a set of dices an a list of playable scores in the scorecard.
	 * The evaluation evaluates which score is most probable to be reached in a given set of
	 * turns.
	 * @param dices
	 * @param playableScore
	 */
	public static void evaluate(Dice[] dices, boolean[] playableScore, int rollsLeft){
		double[] expValues = new double[16];
		int[] values = new int[5];
		
		//ones to sixes
		for(int j = 1; j<7;j++){
			for(int i=0;i<3;i++){
			values[i]=j;
			}
			//calculates expected value of each category.
			expValues[j] = probability(dices, values, rollsLeft)*score(j, values);
		}
		
	}
	
	/**
	 * Help method to calculate the distance from a list of dices to a 
	 * set of dice scores.
	 * 
	 * @param dices 
	 * @param sc
	 */
	private static double probability(Dice[] dices, int[] scores, int rollsLeft){
		
		
		return 0;
	}
	/**
	 * 
	 * 
	 * @param category Category for which expected value is needed.
	 * @return Returns score for supplied category.
	 */
	private static double score(int category, int[] values){
		int ret = 0;
		switch(category){
		case(1):
		case(2):
		case(3):
		case(4):
		case(5):
		case(6):
			for(int value : values){
				ret+=value;
			}
			return ret;
		}
		return 0;
	}
}
