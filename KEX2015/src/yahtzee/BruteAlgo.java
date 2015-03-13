package yahtzee;

import java.util.Arrays;

/**
 * The bruteforce algorithm to the game of yahtzee. 
 * Plays the game in the order of the scorecard, from top to bottom.
 * 
 * @author D.Jendeberg and L.Wikstén
 */
public class BruteAlgo {
	
	/**
	 *  Play method to simulate a game of solitare yahtzee.
	 *  
	 * @param turn The turn number, between 1 and 15.
	 * @param dices The the sides facing up on the dices.
	 * @param sc The scorecard, i.e. current points and empty rows for not played.
	 */
	public static void play(int turn, Dice[] dices, ScoreCard sc){
		//The value for turn will either be submitted or calculated,
		//can easily be calculated by the scorecard.
		for (int i = 0; i<2; i++){
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
		boolean foursome;
		switch(turn){
		case 1: //Ones
		case 2: //Twos
		case 3: //Threes
		case 4: //Fours
		case 5: //Fives
		case 6: //Sixes
			//1-6 just save the turn values.
			for(int i=0;i<5;i++){
				if(dices[i].getScore()==turn){
					//Do nothing
				} else {
					dices[i].reroll();
				}
			}
			break;
		case 7: //Pair
			pair = false;
			scores = new int[5];
			for(int i = 0; i<5; i++){			//Get all scores.
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);				//Sort scores.
			for(int i = 0; i<4 && !pair; i++){	//Check for pairs.
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
		case 8: //Two pairs
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
		case 9: //Threesome
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
		case 10: //Foursome TODO
			pair = false;
			trice = false;
			foursome = false;
			int bestValue = 0;
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores); //Sort the list of scores. 
			for(int i = 0; i < 5; i++){
				if(i < 2){ //only then this is possible
					if(scores[i] == scores[i+1] && scores[i] == scores[i+2] && scores[i] == scores[i+3]){
						foursome = true; //Done!!
						break;
					}
				}
				if(i < 3){
					if(scores[i] == scores[i+1] && scores[i] == scores[i+2]){
						trice = true; //Only two dice to reroll. 
						bestValue = scores[i];
					}
				}
				if(i<4){
					if(scores[i] == scores[i+1] ){
						pair = true;
						if(!trice)
							bestValue = scores[i];
					}
				}
				if(bestValue == 0){
					bestValue = scores[4];
				}
			}
			if(!foursome){
				for(Dice d : dices){
					if(d.getScore() != bestValue){
						d.reroll();
					} 
				}
			}
			break; 
		case 11: //Small Straight
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i]=-1;
			}
			/* This loop will make the scores list look as follows,
			 * if a dice contains the value i then a dice contains that value
			 * if a dice position does not come up in scores then the dice should be rerolled. 
			 * */
			for(int i = 0; i<5; i++){
				for(Dice dice : dices){
					if(dice.getScore() == i){
						scores[i] = dice.getScore();
					}
				}
			}
			for(int i = 0; i<5; i++){
				if(scores[i]==-1){
					dices[i].reroll();
				}
			}
			break;
		case 12: //Large Straight
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i]=-1;
			}
			/* This loop will make the scores list look as follows,
			 * if a dice contains the value i then we set that position in scores to the dice placement
			 * if a dice position does not come up in scores then the dice should be rerolled. 
			 * */
			for(int i = 0; i<5; i++){
				for(int j = 0; j<5; j++){
					if(dices[j].getScore() == i+1){
						scores[i] = j;
					}
				}
			}
			for(int i = 0; i<5; i++){
				if(scores[i]==-1){
					dices[i].reroll();
				}
			}
			break;
		case 13: //Full House
			int tri = 0;
			int par = 0;
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);
			for(int i = 0; i<4; i++){
				if(scores[i] == scores[i+1]){
					if(i < 3 && scores[i] == scores[i+2] ){
						//Three equal dices will allways be in a row.
						tri = scores[i];
					}else{
						//Two dices in a row are equal.
						par = scores[i];
					}
					i++;
				}
			}
			for(int i = 0; i<5; i++){
				if(dices[i].getScore() == tri || dices[i].getScore() == par){
					//Do Nothing 
					//This is a good threesome
					//or a par
				}else{
					dices[i].reroll();
				}
			}
			break;
		case 14: //Chance
			//Save everything over nomalized value 3,5 i.e. 4 or over.
			for(int i = 0; i<5; i++){
				if(dices[i].getScore() < 4){
					dices[i].reroll();
				}
			}
			break;
		case 15: //Yahtzee
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
