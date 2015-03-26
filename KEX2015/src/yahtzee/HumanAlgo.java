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
		
		/* *** *** NO THROWS LEFT - DECIDE WHERE TO PUT SCORE *** *** */
		if(rollsLeft == 0){
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			for(int i = 1; i < 16; i++){ //Go through score card
				if(playableScores[i]){ //If playable: 
					int rowiscore =	calculatePossibleScore(i, tempScore); //calculate possible score
					if(rowiscore >= maxscore){
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
			
			
		/* *** *** ONE THROW LEFT *** *** */	
		} else if(rollsLeft == 1) { //TODO this method - all of it!
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			Arrays.sort(tempScore);
			
			
			/* * * TWO DICES ALIKE - 1 and 2 * * */
			if(tempScore[0] == tempScore[1]){
				/* * Three of a kind * */
				if(tempScore[0] == tempScore[2]){ //Three of same kind
					if(tempScore[0] == tempScore[3]){//Four of same kind
						if(tempScore[0] == tempScore[4]){ //Yahtzee!
							return;
						} 
					} else if(playableScores[13]){ //Full house possible
						if(tempScore[3] == tempScore[4]){ //Full house! 
							return;
						}
						for(Dice d : dices){
							if(d.getScore() != tempScore[0] && d.getScore() != tempScore[3]) //Re-roll dice that is not paired
								d.reroll();
						}
						return;
					} 
					for(Dice d : dices){ //Re-roll different dice
						if(d.getScore() != tempScore[0])
							d.reroll();
					}
					return;
				/* * Two pairs * */
				} else if(tempScore[2] == tempScore[3]){
					if(playableScores[13]){//Full house free
						if(tempScore[2]==tempScore[4]){//Full house!
							return;
						} 
						for(Dice d : dices){
							if(d.getScore() != tempScore[2] && d.getScore() != tempScore[0]) //Re-roll the one different dice
								d.reroll();
						}
						return;
					}
					if(!playableScores[8]){//Two pairs not free
						//keep best pair
						for(Dice d : dices){
							if(d.getScore() != tempScore[2]) //Re-roll different dice
								d.reroll();
						}
						return;
					}
				/* * Two pairs * */	
				} else if(tempScore[3]==tempScore[4]){//Two pairs!
					if(playableScores[13] || playableScores[8]){ //Full house possible or two pairs
						for(Dice d : dices){
							if(d.getScore() != tempScore[0] && d.getScore() != tempScore[3])
								d.reroll(); //Re-roll for full house or just to improve situation.
						}
						return;
					} else {
						for(Dice d : dices){
							if(d.getScore() != tempScore[3])
								d.reroll(); //Save best pair
						}
						return;
					}	
				}
				
			/* * * TWO DICES ALIKE - 2 and 3 * * */
			} else if(tempScore[1] == tempScore[2]){
				/* * Three of a kind * */ 
				if(tempScore[1]==tempScore[3]){
					if(tempScore[1] == tempScore[4]){//Four of a kind
					}
					
				}
				
			/* * * TWO DICES ALIKE - 3 and 4 * * */	
			} else if(tempScore[2] == tempScore[3]){
				/* * Three of a kind * */ 
				if(tempScore[1]==tempScore[3]){
				}
				
				
			/* * * TWO DICES ALIKE - 4 and 5 * * */	
			} else if(tempScore[3] == tempScore[4]){
				
			
			} 
			/* * * NO DICES ALIKE * * */
			if(playableScores[12]){//Large straight free
				if(hasLargeStraight(tempScore)){
					return;//Put score in straight
				} 
			}
			if(playableScores[11]){//Small straight free
				if(hasSmallStraight(tempScore)){
					return;//Put score in straight
				} 
			}
		
			
		/* *** *** TWO THROWS LEFT *** *** */	
		} else if(rollsLeft == 2){
			int[] tempScore = new int[5];
			for(int i = 0; i<5; i++){
				tempScore[i] = dices[i].getScore();
				//System.out.println("In evaluate ");
			}
			Arrays.sort(tempScore);
			
			/* * * THE FIRST TWO DICES ALIKE - all possible * * */
			if(tempScore[0] == tempScore[1]){
				/* * Three of a kind * */
				if(tempScore[0] == tempScore[2]){ //Three of same kind
					if(tempScore[0] == tempScore[3]){//Four of same kind
						if(tempScore[0] == tempScore[4]){ //Yahtzee!
							return;
						} 
					} else if(playableScores[13]){ //Full house possible
						if(tempScore[3] == tempScore[4]){ //Full house! 
							return;
						}
						for(Dice d : dices){
							if(d.getScore() != tempScore[0] && d.getScore() != tempScore[3]) //Re-roll different dice
								d.reroll();
						}
						return;
					} 
					for(Dice d : dices){
						if(d.getScore() != tempScore[0]) //Re-roll different dice
							d.reroll();
					}
					return;
				/* * Two pairs * */	
				} else if(tempScore[2]==tempScore[3]){ //Two pairs!
					if(playableScores[13]){//Full house free
						if(tempScore[2]==tempScore[4]){//Full house!
							return;
						} 
						for(Dice d : dices){
							if(d.getScore() != tempScore[2] && d.getScore() != tempScore[0]) //Re-roll the one different dice
								d.reroll();
						}
						return;
					}
					if(!playableScores[8]){//Two pairs not free
						//keep best pair
						for(Dice d : dices){
							if(d.getScore() != tempScore[2]) //Re-roll different dice
								d.reroll();
						}
						return;
					}
				/* * Two pairs * */	
				} else if(tempScore[3]==tempScore[4]){//Two pairs!
					if(playableScores[13] || playableScores[8]){ //Full house possible or two pairs
						for(Dice d : dices){
							if(d.getScore() != tempScore[0] && d.getScore() != tempScore[3])
								d.reroll(); //Re-roll for full house or just to improve situation.
						}
						return;
					} else {
						for(Dice d : dices){
							if(d.getScore() != tempScore[3])
								d.reroll(); //Save best pair
						}
						return;
					}
				/* * Only two alike * */ 	
				} else{ //Only the two first alike-no other.
					if(tempScore[0] == 1 && tempScore[4] != 6){ //No sixes - Small straight
						if(playableScores[11]){
							for(Dice d : dices){
								if(d.getScore() == tempScore[0]){//Re-roll the dice with existing number.
									d.reroll();
									return;
								}	
							}
						}
					} else if(tempScore[0] != 1 && tempScore[4] == 6){ //No ones - LG straight
						if(playableScores[12]){
							for(Dice d : dices){
								if(d.getScore() == tempScore[0]){ //Re-roll the dice with existing number.
									d.reroll();
									return;
								}	
							}
						}
					} 
				}
				if(playableScores[(tempScore[0])]){
					for(Dice d : dices){
						if(d.getScore() != tempScore[0]){ //Re-roll different dices.
							d.reroll();
						}	
					}
					return;
				}
				for(Dice d : dices){
					if(d.getScore() != 6){ //Only save sixes for a good start.
						d.reroll();
					}	
				}
				
				
				
				
			/* * TWO DICES ALIKE, value higher than 2 and one dice shows less.  * */	
			} else if(tempScore[1] == tempScore[2]){ //Dice 2 and 3 shows same value
				if(tempScore[1] == tempScore[3]){ //Three of same kind
					if(tempScore[1] == tempScore[4] || playableScores[13]){//Four of same kind or go for full house
						for(Dice d : dices) 
							if(d.getScore() < tempScore[1]) //Re-roll different dice (or dice with lowest value for full house)
								d.reroll();
					}  else { //Try to get better results.
						for(Dice d : dices) 
							if(d.getScore() != tempScore[1]) //Re-roll different dice
								d.reroll();
					}	
				} else if(tempScore[3]==tempScore[4]){//Two pairs - maybe go for full house
					if(playableScores[13]){ //Full house possible
						for(Dice d : dices) 
							if(d.getScore() < tempScore[1]) //Re-roll the only dice of one kind
								d.reroll(); //Re-roll for full house!
					} else if(playableScores[8]){ //Two pairs possible
						//Already got two pairs
					} else { //re-roll dice with lowest value. 
						if(playableScores[(tempScore[4])]){ //Possible to go for 1-6 (highest value.)
							for(Dice d : dices) 
								if(d.getScore() != tempScore[4]) //Re-roll the dices that doesn't match.
									d.reroll();
						} else if(playableScores[(tempScore[1])]){ //second best pair free?
							for(Dice d : dices) 
								if(d.getScore() != tempScore[1]) //Re-roll the dices that doesn't match.
									d.reroll();
						} else{
							for(Dice d : dices) 
								if(d.getScore() != tempScore[4]) 
									d.reroll(); //Re-roll lowest ones anyway
						}
					}
				} else if(playableScores[12]){ //Large straight free?
					if(tempScore[0] != 1) //All other values correct for lg.straight.
						for(Dice d : dices){ 
							if(d.getScore() == tempScore[1]){ //Re-roll the dices that doesn't match.
								d.reroll();
								break;
							}
						}
					else if(playableScores[(tempScore[1])] || tempScore[1] > 3){ //Row free or worth going for trice etc.?
						for(Dice d : dices) 
							if(d.getScore() != tempScore[1]) //Re-roll the dices that doesn't match.
								d.reroll();
					} else { //Nothing worth saving - go for 6's or nothing.
						for (int i = 0; i < 5; i++){
							if(dices[i].getScore() < 6)
								dices[i].reroll();
						}
					}
				} else if(playableScores[11]){ //Is small straight free?
					if(tempScore[4] != 6) //All other values correct for sm.straight.
						for(Dice d : dices){ 
							if(d.getScore() == tempScore[1]){ //Re-roll the dice that doesn't match.
								d.reroll();
								break;
							}
						}
					else if(playableScores[(tempScore[1])] || tempScore[1] > 3){ //Row free or worth going for trice etc.?
						for(Dice d : dices) 
							if(d.getScore() != tempScore[1]) //Re-roll the dices that doesn't match.
								d.reroll();
					} else { //Worth saving?
						for (int i = 0; i < 5; i++){
							if(dices[i].getScore() < 6)
								dices[i].reroll();
						}
					}
				} else if(tempScore[1] > 3 || playableScores[(tempScore[1])]) { //Row free or worth going for?
					for(Dice d : dices) 
						if(d.getScore() != tempScore[1]) //Re-roll the dices that doesn't match.
							d.reroll();
				} else {
					for (int i = 0; i < 5; i++){
						if(dices[i].getScore() < 6)
							dices[i].reroll();
					}
				}
				
			
			/* * TWO DICES ALIKE with value more than 3 and two dices with different values below  * */	
			} else if(tempScore[2] == tempScore[3]){
				if(tempScore[2] == tempScore[4]){ //Three of same kind
					if(playableScores[(tempScore[2])]){ //Go for top row of this kind
						for (Dice d : dices){
							if(d.getScore() != tempScore[2])
								d.reroll();
						}
					} else if(playableScores[13]){ //Full house free. 
						if(tempScore[2] >= 4 && tempScore[1] >= 3) { //Will not accept a value that's too low.
							for(Dice d : dices){
								if(d.getScore() != tempScore[2] || d.getScore() != tempScore[1]) //Re-roll other dice
									d.reroll();
							}
						} else if(tempScore[2] >= 4){ // TempScore[1] was too low to keep.
							for(Dice d : dices){
								if(d.getScore() != tempScore[2]) //Re-roll other dice
									d.reroll();
							}
						} else {
							for(Dice d : dices){ //Nothing worth saving.
								d.reroll();
							}
						}
					} else if(tempScore[2] > 3){//Go for three of a kind or more
						for (Dice d : dices){
							if(d.getScore() != tempScore[2])
								d.reroll();
						}
					} else {
						for(Dice d : dices){ //Nothing worth saving.
							d.reroll();
						}
					}
				} else if(playableScores[(tempScore[2])]){ //Go for top row of this kind
					for (Dice d : dices){
						if(d.getScore() != tempScore[2])
							d.reroll();
					}
				} else if(playableScores[12]){ //L-g. straight
					if(tempScore[0] != 1){ //Go for straight (one different dice)
						for (Dice d : dices){
							if(d.getScore() == tempScore[2]){
								d.reroll(); //Re-roll one of the alike dices. 
								break;
							}
						}
					} else if(playableScores[11]){//Small straight
						if(tempScore[4] != 6){ //Go for straight (one different dice)
							for (Dice d : dices){
								if(d.getScore() == tempScore[2]){
									d.reroll(); //Re-roll one of the alike dices. 
									break;
								}
							}
						}
					} else if(tempScore[2] > 3){ //Go for high value (three, four of a kind etc.)
						for (Dice d : dices){
							if(d.getScore() != tempScore[2])
								d.reroll();
						}
					} else {
						for (Dice d : dices){
							if(d.getScore() > 4) //Re-roll dices lower than 6
								d.reroll();
						}
					}
					
				} else if(playableScores[11]){//Small straight
					if(tempScore[4] != 6){ //Go for straight (one different dice)
						for (Dice d : dices){
							if(d.getScore() == tempScore[2]){
								d.reroll(); //Re-roll one of the alike dices. 
								break;
							}
						}
					} else if(tempScore[2] > 3){ //Go for high value (three, four of a kind etc.)
						for (Dice d : dices){
							if(d.getScore() != tempScore[2])
								d.reroll();
						}
					} else {
						for (Dice d : dices){
							if(d.getScore() > 4) //Re-roll dices lower than 6
								d.reroll();
						}
					}
					
				} else if(tempScore[2] > 3){ //Go for high value (three, four of a kind etc.)
					for (Dice d : dices){
						if(d.getScore() != tempScore[2])
							d.reroll();
					}
				} else {
					for (Dice d : dices){
						if(d.getScore() > 4) //Re-roll dices lower than 6
							d.reroll();
					}
				}
				
				
				
			/* * ONLY TWO DICES ALIKE value higher than 4 and 3 dices with different values below * */	
			} else if(tempScore[3] == tempScore[4]){//Only pair possible 
				if(playableScores[(tempScore[3])]){//Go for this row
					for(Dice d : dices){
						if(d.getScore() != tempScore[3])
							d.reroll(); //Re-roll different dice.
					}
				} else { //Always worth saving, because of sorted and no other pairs, this is at least 4.
					if(tempScore[0] == 1 && tempScore[3] != 6){ //No sixes - Small straight
						if(playableScores[11]){
							for(Dice d : dices){
								if(d.getScore() == tempScore[3]){
									d.reroll();
									return;
								}	
							}
						}
					} else if(tempScore[0] != 1 && tempScore[3] == 6){ //No ones - LG straight
						if(playableScores[12]){
							for(Dice d : dices){
								if(d.getScore() == tempScore[3]){
									d.reroll();
									return;
								}	
							}
						}
					}
					for(Dice d : dices){ //Go for this score.
						if(d.getScore() != tempScore[3])
							d.reroll(); //Re-roll different dice.
					}
				}
				
				
				
			/* * NO DICES ALIKE * */	
			} else { //Check for straights
				if(playableScores[12]) { //Go for large straight if empty
					if(hasLargeStraight(tempScore)){	
						return;
					} 
				} 
				if(playableScores[11]){ //Go for small straight if empty
					if(hasSmallStraight(tempScore)){
						return;
					}else {
						for(Dice d : dices){
							if(d.getScore() == 6)
								d.reroll();
						}
					}
				} else {
					for(Dice d : dices){
						if(d.getScore() >= 5) //keep best dice(s)
							d.reroll();
					}
				}
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
					if(rowSum >= 3*row)
						rowSum += 50; //Bonus if more than three
				}
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
