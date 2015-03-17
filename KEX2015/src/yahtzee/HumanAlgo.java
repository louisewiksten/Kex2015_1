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
		//The value for turn will either be submitted or calculated,
		//can easily be calculated by the scorecard.
		for (int i = 0; i<2; i++){
			evaluate(1, dices);
		}
		//Sets the score for a given row (turn).
		int[] list = new int[5];
		for(int i = 0; i < 5; i++)
			list[i] = dices[i].getScore();
		sc.setScore(list, 2);
	}
	/**
	 * Method that evaluates a list of dices and rerolls the dices if needed.
	 *
	 * @param turn The number of turns into the game.
	 * @param dices A list of Dices that can be rerolled if needed.
	 */
	public static void evaluate(int turn, Dice[] dices){
		
	}
}
