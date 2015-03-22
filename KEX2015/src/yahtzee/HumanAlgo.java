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
				//System.out.println("In check for unplayed rows"); //Trace prints
			} else {
				playableScore[i] = false;
			}
		}
		
		//-- Evaluate result --\\
		for(int i = 2; i >= 0; i--){
			evaluate(dices, playableScore, i, sc);
			//System.out.println("after evaluate on roll" + i);
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
		if(rollsLeft == 0){
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			for(int i = 1; i < 16; i++){ //Go through score card
				if(playableScores[i]){ //If playable, calculate possible score
					int rowiscore =	calculatePossibleScore(i, tempScore);
					if(rowiscore >= maxscore){
						selectedRow = i; //This row keeps the best score
						maxscore = rowiscore; 
					}
				}
			}
			if(maxscore == 0){ //No fit
				int minMaxrowscore = 100; //The row with the lowest maximum score
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
			finalScore = tempScore;
			sc.setScore(finalScore, selectedRow);
		} else if(rollsLeft == 1) {
			/*for(Dice d : dices){
				d.reroll();
			}*/
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			Arrays.sort(tempScore);
			
			//TODO whatever neccessary
			if(false){ //TODO fix
				
			} else { //Check for straights
				if(playableScores[12]) { //Go for large straight if empty
					if(hasLargeStraight(tempScore)){	
					}else 
						dices[0].reroll();
				} else if(playableScores[11]){ //Go for small straight if empty
					if(hasSmallStraight(tempScore)){	
					}else 
						dices[4].reroll();
				} else {
					for (int i = 0; i < 4; i++)
						dices[i].reroll(); //Save best dice
				}
			}
			//TODO Only one more chance to reroll
		} else if(rollsLeft == 2){
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			Arrays.sort(tempScore);
			if(tempScore[0] == tempScore[1]){ //Check for similarities
				if(tempScore[0] == tempScore[2]){ //Three of same kind
					
				}
				 
			} else if(tempScore[1] == tempScore[2]){ //Dice 2 and 3 shows same value
				if(tempScore[1] == tempScore[3]){ //Three of same kind
					if(tempScore[1] == tempScore[4]){//Four of same kind
						dices[0].reroll(); //Go for yahtzee or maximize score in some way
					} else if(playableScores[13]){ //Full house possible
						dices[0].reroll(); //dices[4] high enough to go for full house
					} else { //Try to get better results.
						dices[0].reroll();
						dices[4].reroll();
					}	
				} else if(tempScore[3]==tempScore[4]){//Two pairs - maybe go for full house
					if(playableScores[13]){ //Full house possible
						dices[0].reroll(); //Re-roll for full house!
					} else if(playableScores[8]){ //Two pairs possible
						//Already got two pairs
					} else { //re-roll dice with lowest value. 
						if(playableScores[(tempScore[4])]){ //best pair free?
							for(int i = 0; i<3; i++)
								dices[i].reroll(); //Re-roll lowest ones 
						} else if(playableScores[(tempScore[1])]){ //second best pair free?
							dices[0].reroll();
							dices[3].reroll();
							dices[4].reroll(); //Re-roll other dice. 
						} else{
							for(int i = 0; i<3; i++)
								dices[i].reroll(); //Re-roll lowest ones anyway
						}
					}
				} else if(playableScores[12]){ //Large straight free?
					if(tempScore[0] != 1) //All other values correct for lg.straight.
						dices[1].reroll();
					else if(playableScores[(tempScore[1])] || tempScore[1] > 3){ //Row free or worth going for trice etc.?
						dices[0].reroll();
						dices[3].reroll();
						dices[4].reroll();
					} else { //Worth saving?
						for (int i = 0; i < 5; i++){
							if(tempScore[i] < 6)
								dices[i].reroll();
						}
					}
				} else if(playableScores[11]){ //Is small straight free?
					if(tempScore[4] != 6) //All other values correct for sm.straight.
						dices[1].reroll();
					else if(playableScores[(tempScore[1])] || tempScore[1] > 3){ //Row free or worth going for trice etc.?
						dices[0].reroll();
						dices[3].reroll();
						dices[4].reroll();
					} else { //Worth saving?
						for (int i = 0; i < 5; i++){
							if(tempScore[i] < 6)
								dices[i].reroll();
						}
					}
				} else if(tempScore[1] > 3 || playableScores[(tempScore[1])]) { //Row free or worth going for?
					dices[0].reroll();
					dices[3].reroll();
					dices[4].reroll();
				} else {
					for (int i = 0; i < 5; i++){
						if(tempScore[i] < 6)
							dices[i].reroll();
					}
				}
			} else if(tempScore[2] == tempScore[3]){
				if(tempScore[2] == tempScore[4]){ //Three of same kind
					
				} else { //Two pairs not possible. 
					
				}
				
			} else if(tempScore[3] == tempScore[4]){
				
			} else { //Check for straights
				if(playableScores[12]) { //Go for large straight if empty
					if(hasLargeStraight(tempScore)){	
					}else 
						dices[0].reroll();
				} else if(playableScores[11]){ //Go for small straight if empty
					if(hasSmallStraight(tempScore)){	
					}else 
						dices[4].reroll();
				} else {
					for (int i = 0; i < 4; i++)
						dices[i].reroll(); //Save best dice
				}
			}
			//TODO Two more rerolls. Evaluate based on empty scorecard etc.
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
				for(int r : rolls)
					if(r == row)
						rowSum += r;
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
				for(int i = 0; i < 5; i++)
					rowSum += rolls[i]; //Chance sum
				break;
			case 15:
				if(rolls[0] == rolls[1] && rolls[0] == rolls[2] && rolls[0] == rolls[3] && rolls[0] == rolls[4]){
					rowSum += 50; //Four of a kind sum
					break;
				}
				break;
		}
		return rowSum;
	}
	
	private static int calculateMaximumScore(int row){
		int rowSum = 0;
		switch(row){
			case 1: 	
			case 2: 
			case 3: 
			case 4: 
			case 5: 
			case 6: 
				rowSum = row*5;
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
