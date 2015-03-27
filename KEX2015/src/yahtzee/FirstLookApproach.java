package yahtzee;

import java.util.Arrays;

public class FirstLookApproach {
	/**
	 * Play method to simulate a game of solitaire yahtzee.
	 *  
	 * @param dices The the sides facing up on the dices.
	 * @param sc The score card, i.e. current points and empty rows for "not played".
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
		for(int i = 2; i >= 0; i--){
			evaluate(dices, playableScore, i, sc);
		}
	}
	
	/**
	 * Method that evaluates a list of dices and re-rolls the dices if needed.
	 *
	 * @param dices A list of Dices that can be re-rolled if needed.
	 * @param playableScores A list with playable rows in score card.
	 * @param rollsLeft The number of rolls left when playing.
	 * @param sc The score card to fill in.
	 */
	public static void evaluate(Dice[] dices, boolean[] playableScores, int rollsLeft, ScoreCard sc){
		int selectedRow = 0; //The row where to fill in a score. 
		int maxscore = 0;
		int[] finalScore = new int[5];
		int[] tempScore = new int[5];
		for(int i = 0; i<5; i++){
			tempScore[i] = dices[i].getScore();
		}
		Arrays.sort(tempScore);
		
		if(rollsLeft == 0){/* No rolls left - calculate where to put score! */
			
			for(int i = 1; i < 16; i++){ //Go through score card
				if(playableScores[i]){ //If playable: 
					int rowiscore =	calculatePossibleScore(i, tempScore); //calculate possible score
					if(rowiscore > maxscore){
						selectedRow = i; //This row keeps the best score
						maxscore = rowiscore; 
					}
				}
			}
			if(maxscore == 0){ //No fit
				int minMaxrowscore = 100; //The row with the lowest maximum score
				if(playableScores[14]){//Chance free to put score in
					selectedRow = 14;
				} else{
					for(int i = 1; i < 16; i++){ //Go through score card
						if(playableScores[i]){ //If playable, calculate maximum score
							int rowiscore =	calculateMaximumScore(i);
							if(rowiscore < minMaxrowscore){
								selectedRow = i; //This row keeps the 'best' row to set 0 in
								minMaxrowscore = rowiscore; 
							}
						}
					}
				}
			}
			finalScore = tempScore;
			sc.setScore(finalScore, selectedRow);
			
		} else {/* Two or one roll left */
			//--FIRST CHECK!--
			if(tempScore[0] == tempScore[4]){ //YAHTZEE! First check.
				if(playableScores[15] || playableScores[(tempScore[0])]){ 
					return;
				}
			}
			int bestRow = 0; //Keeps the row that is most frequent.
			int rowCount = 0; //Keeps the number of dices with bestRow's value.
			//--loop backwards through top rows--
			for(int i = 1; i < 7; i++){ 
				if(playableScores[i]){
					int noIs = 0;
					for(int j = 0; j < 5; j++){
						if(tempScore[j] == i){
							noIs++;
						}
					}
					if(noIs >= rowCount) {
						bestRow = i;
						rowCount = noIs;
					}
				}
			}
			if(rowCount > 2){
				for(Dice d : dices){
					if(d.getScore() != bestRow)
					d.reroll();
				}
				return;
			}
			for(int i = 7; i < 16; i++){ 
				if(playableScores[i]){
					
				}
			}
			if(rowCount >= 2){
				for(Dice d : dices){
					if(d.getScore() != bestRow)
					d.reroll();
				}
				return;
			}
			
		}
	}
	
	/**
	 * 
	 * @param dices A sorted array of dice results
	 * @return True if it has large straight
	 */
	private static boolean hasLargeStraight(int[] dices){
		if(dices[0] == 2 && dices[1] == 3 && 
				dices[2] == 4 && dices[3] == 5 && dices[4] == 6){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param dices A sorted array of dice results
	 * @return True if it has small straight
	 */
	private static boolean hasSmallStraight(int[] dices){
		if(dices[0] == 1 && dices[1] == 2 && 
				dices[2] == 3 && dices[3] == 4 && dices[4] == 5){
			return true;
		}
		return false;
	}
	/**
	 * Calculates the possible row score for a given row and given dice results(1-15).
	 * @param row The row to calculate the score for with the given dice results.
	 * @param rolls The dice results.
	 * @return The score for this row.
	 */
	private static int calculatePossibleScore(int row, int[] rolls){
		int rowSum = 0;
		Arrays.sort(rolls);
		switch(row){
			case 1: 	
			case 2: 
			case 3: 
			case 4: 
			case 5: 
			case 6: 
				for(int r : rolls){
					if(r == row)
						rowSum += r;
				}
				if(rowSum <= row)
					rowSum = 0;
				if(rowSum >= 3*row)
					rowSum += 50; //Bonus if more than three
				break;
			case 7: 
				for(int i = 4; i > 0; i--)
					if(rolls[i] == rolls[i-1]){
						rowSum += rolls[i] + rolls[i-1];
						break;
					}
				break;
			case 8: 
				for(int i = 4; i > 2; i--)
					if(rolls[i] == rolls[i-1]){
						if(rolls[i-2] == rolls[i-3] && rolls[i] != rolls[i-2]){ //First pair != second pair
							rowSum += 2*rolls[i] + 2*rolls[i-2];
							break;
						} else if(i > 3 && rolls[i-3] == rolls[i-4] && rolls[i] != rolls[i-3]){ 
							//Rolls >3 otherwise error. First pair != second pair
							rowSum += 2*rolls[i] + 2*rolls[i-3];
							break;
						}
					}
				break;
			case 9:
				for(int i = 0; i < 3; i++){
					if(rolls[i] == rolls[i+1] && rolls[i] == rolls[i+2] ){
						rowSum += 3*rolls[i]; //Three of a kind sum
						break;
					}
				}
				break;
			case 10:
				for(int i = 0; i < 2; i++){
					if(rolls[i] == rolls[i+1] && rolls[i] == rolls[i+2] && rolls[i] == rolls[i+3]){
						rowSum += 4*rolls[i]; //Four of a kind sum
						break;
					}
				}
				break;
			case 11:
				if(rolls[0]==1 && rolls[1]==2 && rolls[2]==3 && rolls[3]==4 && rolls[4]==5) 
					rowSum+=15;
				break;
			case 12:
				if(rolls[0]==2 && rolls[1]==3 && rolls[2]==4 && rolls[3]==5 && rolls[4]==6) 
					rowSum+=20;
				break;
			case 13:
					if(rolls[0] == rolls[1] && rolls[0] == rolls[2]){
						if(rolls[3] == rolls[4]){
							rowSum += rolls[0]+rolls[1]+rolls[2]+rolls[3]+rolls[4];
						}
					} else if(rolls[0] == rolls[1]){
						if(rolls[2] == rolls[3] && rolls[2] == rolls[4]){
							rowSum += rolls[0]+rolls[1]+rolls[2]+rolls[3]+rolls[4];
						}
					}
					break;
			case 14:
					rowSum = 0; //Chance is not to be preferred
				break;
			case 15:
				if(rolls[0] == rolls[4]){
					rowSum = 100; //YAHTZEE
					break;
				}
				break;
		}
		return rowSum;
	}
	
	/**
	 * Calculates the maximum score for a given yahtzee row.
	 * @param row The row to calculate the maximum score for (1-15).
	 * @return The maximum score for the given row.
	 */
	private static int calculateMaximumScore(int row){
		int rowSum = 0;
		switch(row){
			case 1: 	
			case 2: 
			case 3: 
			case 4: 
			case 5: 
			case 6: 
				rowSum = row*5+50;
				break;
			case 7: 
				rowSum = 12;
				break;
			case 8: 
				rowSum = 22;
				break;
			case 9:
				rowSum = 18;
				break;
			case 10:
				rowSum = 24;
				break;
			case 11:
				rowSum = 15;
				break;
			case 12:
				rowSum = 20;
				break;
			case 13:
				rowSum = 28;
				break;
			case 14:
				rowSum = 30;
				break;
			case 15:
				rowSum = 50;
				break;
		}
		return rowSum;
	}
}
