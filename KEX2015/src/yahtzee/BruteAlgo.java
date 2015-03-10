package yahtzee;

import java.util.Arrays;

/**
 * The bruteforce algorithm to the game of yahtzee. 
 * Plays the game in the order of the scorecard, from top to bottom.
 * 
 * @author D.Jendeberg and L.Wikstén
 */
public class BruteAlgo {
	
	public static void play(int turn, Dice[] dices, ScoreCard sc){
		//The value for turn will either be submitted or calculated,
		//can easily be calculated by the scorecard.
		for (int i = 0; i<3; i++){
			evaluate(turn, dices);
		}
		//Sets the score for a given row (turn).
		int[] list = new int[5];
		for(int i = 0; i < 5; i++)
			list[i] = dices[i].getScore();
		sc.setScore(list, turn);
	}
	/**
	 * Method that evaluates a list of dices and rerolls the dices if needed.
	 *
	 * @param turn The number of turns into the game.
	 * @param dices A list of Dices that can be rerolled if needed.
	 */
	public static void evaluate(int turn, Dice[] dices){
		int [] scores;
		boolean pair;
		boolean trice;
		switch(turn){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			//1-6 just save the turn values.
			for(int i=0;i<5;i++){
				if(dices[i].getScore()==turn){
					//Do noting
				} else {
					dices[i].reroll();
				}
			}
			break;
		case 7:
			pair = false;
			scores = new int[5];
			for(int i = 0; i<5; i++){			//Get all scores.
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);				//Sort scores.
			for(int i = 0; i<5 && !pair; i++){	//Check for pairs.
				if(scores[i] == scores[i+1])
					pair = true;
			}
			if(pair) 							//If pair found.
				break;
			for(Dice d : dices){ 				//No pair found
				if(d.getScore() != scores[4])	//ReRoll all
					d.reroll();
			}
			break;
		case 8:
			//Two pair, two dices should be equal and another pair should also equal but not equal to the first.
			//first part, find if to dices equal.
			int[] firstpair = new int[2];
			int[] secondpair = new int[2];
			for(int j = 0; j<4; j++){										//For dice 1-4 see if there are any dices equal to it in 2-5.
				for(int i = j+1; i<5; i++){							
					if(dices[j].getScore() == dices[i].getScore()){			//If dice equal set first or second score.
						if(firstpair[0] == 0 && firstpair[1] == 0){			//First pair not found yet.
							firstpair[0] = j;
							firstpair[1] = i;
						}
						else if(secondpair[0] == 0 && secondpair[1] == 0){	//Second pair not found yet.
							secondpair[0] = j;
							secondpair[1] = i;
						}
					}
				}
			}
			//Second part, if pairs found do nothing otherwise reroll.
			if(firstpair[0] == 0 && firstpair[1] == 0){
				for(Dice d : dices)
					d.reroll();
			} else if(secondpair[0] == 0 && secondpair[1] == 0){
				for(int i = 0; i<5; i++){
					if(i != firstpair[0] || i != firstpair[1]){
						dices[i].reroll();
					}
				}
			} else {
				//Both pairs found no reroll needed.
			}
			break;
		case 9:
			trice = false;
			pair = false;
			int pairValue = 0;
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);
			for(int i=0; i<3; i++){
				if(scores[i] == scores[i+1] && scores[i] == scores[i+2]){
					trice = true;
				} else if(scores[i] == scores[i+1]){
					pair = true;
					pairValue = scores[i];
				} else if(scores[i+1] == scores[i+2]){
					pair = true;
					pairValue = scores[i+1];
				}
			}
			if(trice)
				break;
			if(pair){
				for(Dice d : dices){
					if(d.getScore() != pairValue)
						d.reroll();
				}
				break;
			}
			for(Dice d : dices)
				d.reroll();
			break;
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
			scores = new int[5];
			int noReRoll = 0;
			
			//First part, check if yahtzee.
			for(int i = 0; i<5; i++)
				scores[i] = dices[i].getScore();
			if(scores[0] == scores[1] && scores[1] == scores[2] && scores[2] == scores[3] && scores[3] == scores[4])
				break;
			
			//Second part, check if foursome.
			Arrays.sort(scores);
			if(scores[1] == scores[2] && scores[2] == scores[3] && scores[3] == scores[4])
				noReRoll = scores[0];
			else if(scores[0] == scores[1] && scores[1] == scores[2] && scores[2] == scores[3])
				noReRoll = scores[1];
			if(noReRoll != 0){
				for(int i = 0; i<5; i++){
					if(dices[i].getScore() != noReRoll)
						dices[i].reroll();
				}
				break;
			}
			
			//Third part, check if threesome.
			if(scores[2] == scores[3] && scores[3] == scores[4])
				noReRoll = scores[0];
			else if(scores[1] == scores[2] && scores[2] == scores[3])
				noReRoll = scores[1];
			else if(scores[0] == scores[1] && scores[1] == scores[2])
				noReRoll = scores[2];
			//If threesome is found, reroll rest.
			if(noReRoll != 0){
				for(int i = 0; i<5; i++){
					if(dices[i].getScore() != noReRoll)
						dices[i].reroll();
				}
				break;
			}
			//Fourth part, check if a pair.
			for(int i = 3; i>=0; i--){
				if(scores[i] == scores[i+1]){
					noReRoll = scores[i];
					break;
				}
			}
			//If pair found, reroll rest.
			if(noReRoll != 0){
				for(int i = 0; i<5; i++){
					if(dices[i].getScore() != noReRoll)
						dices[i].reroll();
				}
				break;
			}
			
			//Nothing found, go for highest.
			noReRoll = scores[4];
			for(int i = 0; i<5; i++){
				if(dices[i].getScore() != noReRoll)
					dices[i].reroll();
			}
			break;
		}
	}
}
