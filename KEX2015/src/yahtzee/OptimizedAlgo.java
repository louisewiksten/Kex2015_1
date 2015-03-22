package yahtzee;

import java.util.Arrays;

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
		//TODO
		//Create evaluations for all 16 categories.
		
	}
	
	/**
	 * Help method to calculate the distance from a list of dices to a 
	 * set of dice scores.
	 * 
	 * @param dices 
	 * @param sc
	 */
	private static double probability(Dice[] dices, int[] scores, int rollsLeft){
		//TODO
		
		return 0;
	}
	/**
	 * A help method to calculate the score a certain set of values would
	 * generate in a specific category on the scorecard.
	 * 
	 * @param category Category for which expected value is needed.
	 * @return Returns score for supplied category.
	 */
	private static double score(int category, int[] values){
		int ret = 0;
		switch(category){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			for(int value : values){
				ret+=value;
			}
			return ret;
		case 8://Pair
			for(int i = 0; i<4; i++){
				for(int j = i+1; j<5;j++){
					if(values[i]==values[j]){
						ret = values[i];
					}
				}
			}
			return (ret*2);
		case 9://Two pairs
			int fpair = 0;
			int tpair = 0;
			for(int i = 0; i<4;i++){
				for(int j = i+1; j<5; j++){
					if(values[i] == values[j]){	
						if(fpair==0){
							fpair = values[i];
							i++;
						}else{
							tpair = values[i];
							i=5; //break
						}
					}
				}
			}
			if(tpair > 0 && fpair > 0){
				return (2*fpair + 2*tpair);
			}else{
				return 0;
			}
		case 10://Three of a kind
			for(int i = 0; i<5; i++){
				if(values[i] == values[i+1] && values[i] == values[i+2]){
					ret = values[i];
					break;
				}
			}
			return ret*3;
		case 11://Four of a kind
			for(int i = 0; i<2; i++){
				if(values[i]==values[i++] && values[i] == values[i+2] && values[i] == values[i+3]){
					return values[i]*4;
				}
			}
		case 12://Small Straight
			Arrays.sort(values);
			if(values[0]==1 && values[1]==2 && values[2] == 3 && values[3] == 4 && values[4] == 5){
				return 15;
			}else{
				return 0;
			}
		case 13://Large Straight
			Arrays.sort(values);
			if(values[0]==2 && values[1]==3 && values[2] == 4 && values[3] == 5 && values[4] == 6){
				return 20;
			}else{
				return 0;
			}
		case 14://Full House
			Arrays.sort(values);
			if(values[0] == values[1] && values[0] == values[2] && values[3] == values[4]){ 
				return (3*values[0] + 2*values[3]); //Small three big pair
			} else if(values[0] == values[1] && values[2] == values[3] && values[3] == values[4]){
				return 2*values[0] + 3*values[2]; //Small pair big three
			} else {
				return 0;
			}
		case 15://Chance
			for(int value : values){
				ret+=value;
			}
			return ret;
		case 16://Yahtzee
			return 50;
		}
		return -1; //error, this should not occur!
	}
}
