package yahtzee;

import java.util.Arrays;

public class HumanAlgo {
	/**
	 * Play method to simulate a game of solitare yahtzee.
	 *  
	 * @param dices The the sides facing up on the dices.
	 * @param sc The scorecard, i.e. current points and empty rows for not played.
	 */
	public static void play(Dice[] dices, ScoreCard sc){
		//-- Keep track of where score can be put --\\
		boolean[] playableScore = new boolean[16]; 
		for(int i = 1; i < 16; i++){
			if(sc.getRowScore(i)==-1){
				playableScore[i] = true;
			} else {
				playableScore[i] = false;
			}
		}
		
		//-- Evaluate result --\\
		for(int i = 2; i > 0; i--){
			evaluate(dices, playableScore, i, sc);
		}
		
	}
	
	/**
	 * Method that evaluates a list of dices and rerolls the dices if needed.
	 *
	 * @param dices A list of Dices that can be rerolled if needed.
	 * @param playableScores A list with playable rows in scorecard.
	 * @param rollsLeft The number of rolls left when playing.
	 * @param sc The scorecard to fill in.
	 */
	public static void evaluate(Dice[] dices, boolean[] playableScores, int rollsLeft, ScoreCard sc){
		int selectedRow = 0; //The row where to fill in a score. 
		int[] finalScore = new int[5]; //The dices to fill in to scorecard. 
		if(rollsLeft == 0){
			//TODO Find where to save results. 
		} else if(rollsLeft == 1) {
			//TODO Only one more chance to reroll
		} else {
			//TODO Two more rerolls. Evaluate based on empty scorecard etc.
		}
		sc.setScore(finalScore, selectedRow);
	}
}
