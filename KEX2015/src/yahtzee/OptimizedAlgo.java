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
		
		//TODO
		//Evaluate in which box to score the points.
		
	}
	/**
	 * Evaluates a set of dices an a list of playable scores in the scorecard.
	 * The evaluation evaluates which score is most probable to be reached in a given set of
	 * turns.
	 * 
	 * @param dices
	 * @param playableScore
	 */
	public static void evaluate(Dice[] dices, boolean[] playableScore, int rollsLeft){
		double[] expValues = new double[16];
		int[] values = new int[5];
		
		//ones to sixes
		for(int j = 1; j<7;j++){
			if(!playableScore[j]){
				expValues[j] = -1;			//Category allready scored.
			}else{
				for(int i=0;i<3;i++)
					values[i] = j;		//First three scores sets to values.
				for(int i = 4; i<5; i++)
					values[i] = 0;		//Last two scores set to zero.
				//calculates expected value of each category.
				expValues[j] = probability(dices, values, rollsLeft)*expectedScore(j, values);
			}
		}
		//TODO
		//Create evaluations for category 7-15.
		/* Chance */
		expValues[14] = 0; //Never calculate value for chance, use chance when all else fails.
		
		
		int maxCategory = 0;
		double maxExpScore = 0;
		for(int i = 0; i<16; i++){
			if(expValues[i] > maxExpScore){
				maxCategory=i;
				maxExpScore=expValues[i];
			}
		}
		
		reroll(dices,maxCategory);
	}
	
	/**
	 * Help method to calculate the distance from a list of dices to a 
	 * set of dice scores.
	 * 
	 * @param dices 
	 * @param sc
	 */
	private static double probability(Dice[] dices, int[] wanted, int rollsLeft){			
		double prob = 0;
		
		//Create int array from dices for easier calculations.
		int[] current = new int[5];
		for(int i = 0; i<5; i++){
			current[i] = dices[i].getScore();
		}
		Arrays.sort(current);
		Arrays.sort(wanted);
		
		//Calculate n to use in formula
		int[] values = Arrays.copyOf(wanted, 5);
		int n = 0;														//Edit distance current -> wanted
		for(int i = 0; i<5; i++){
			for(int j = 0; j<5; j++){
				if(current[i] == values[j]){
					values[j] = 0;
					n++;
					break;
				}
			}
		}
		n = 5-n;
		
		//Calculate k t use in fromula
		int k = 0;
		for(int i = 0; i<5; i++){
			for(int j = 0; j<5; j++){
				if(wanted[i]==0){
					k++;
					break;
				}
			}
		}
		k = n-k;
		n *= rollsLeft;
		
		prob = probabilityMassFunction(n,k);
		
		return prob;										//Return calculated probability.
	}
	/**
	 * A help method to calculate the score a certain set of values would
	 * generate in a specific category on the scorecard.
	 * 
	 * @param category Category for which expected value is needed.
	 * @return Returns score for supplied category.
	 */
	private static double expectedScore(int category, int[] values){
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
			if(ret >= category*3){
				ret+=50;
			}
			return ret;
		case 7://Pair
			for(int i = 0; i<4; i++){
				for(int j = i+1; j<5;j++){
					if(values[i]==values[j]){
						ret = values[i];
					}
				}
			}
			return (ret*2);
		case 8://Two pairs
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
		case 9://Three of a kind
			for(int i = 0; i<5; i++){
				if(values[i] == values[i+1] && values[i] == values[i+2]){
					ret = values[i];
					break;
				}
			}
			return ret*3;
		case 10://Four of a kind
			for(int i = 0; i<2; i++){
				if(values[i]==values[i++] && values[i] == values[i+2] && values[i] == values[i+3]){
					return values[i]*4;
				}
			}
		case 11://Small Straight
			Arrays.sort(values);
			if(values[0]==1 && values[1]==2 && values[2] == 3 && values[3] == 4 && values[4] == 5){
				return 15;
			}else{
				return 0;
			}
		case 12://Large Straight
			Arrays.sort(values);
			if(values[0]==2 && values[1]==3 && values[2] == 4 && values[3] == 5 && values[4] == 6){
				return 20;
			}else{
				return 0;
			}
		case 13://Full House
			Arrays.sort(values);
			if(values[0] == values[1] && values[0] == values[2] && values[3] == values[4]){ 
				return (3*values[0] + 2*values[3]); //Small three big pair
			} else if(values[0] == values[1] && values[2] == values[3] && values[3] == values[4]){
				return 2*values[0] + 3*values[2]; //Small pair big three
			} else {
				return 0;
			}
		case 14://Chance
			for(int value : values){
				ret+=value;
			}
			return ret;
		case 15://Yahtzee
			return 50;
		}
		return -1; //error, this should not occur!
	}
	
	/**
	 * Method to reroll the correct dices for the choosen category.
	 * 
	 * @param dices
	 * @param category
	 */
	private static void reroll(Dice[] dices, int category){
		int[] scores = new int[5];
		int save = 0;
		switch(category){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			for(Dice dice : dices){
				if(!(dice.getScore() == category))
					dice.reroll();
			}
			break;
			
		case 7: //Pair
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore(); 
			}
			Arrays.sort(scores);
			for(int i = 0; i<4; i++){
				if(scores[i] == scores[i+1]){
					save = scores[i];
				}
			}
			if(save == 0){
				save = scores[4];
			}
			for(Dice dice : dices){
				if(!(dice.getScore() == save)){
					dice.reroll();
				}
			}
			break;
			
		case 8: //Two Pairs
			int fpair = 0;
			int spair = 0;
			scores = new int[5];
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);											//Sort scores
			
			for(int i = 0; i<4; i++){										//Find pair for fpair.
				if(scores[i] == scores[i+1]){
					fpair = scores[i];
				}
			}
			if(fpair == 0){													//No pair exist.
				fpair = scores[4];											//Choose highest values.
				spair = scores[3];
			}else{															//fpair is a pair.
				for(int i = 0; i<4; i++){									//see if pair for spair.
					if(scores[i] == scores[i+1] && scores[i] != fpair){
						spair = scores[i];									
					}
				}
				if(spair == 0){												//No pair for spair
					for(int i = 0; i<5; i++){
						if(scores[i] != fpair){								//Select highest value != fpair. 
							spair = scores[i];
						}
					}
				} else {
					//we have a two pair. Might want to go for full house so still reroll last.
				}
			}
			for(Dice dice : dices){
				if(dice.getScore() != fpair && dice.getScore() != spair)
					dice.reroll();
			}
			
			break;
			
		case 9: //Three of a kind
			scores = new int[5];
			save = 0;
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);
			for(int i = 0; i<3; i++){										
				if(scores[i] == scores[i+2])	//Do we have a three of a kind?
					save = scores[i];										//Save the three, reroll all else
			}
			if(save == 0){													//Did not have a three of a kind
				for(int i = 0; i<4; i++){									//Check for pair instead
					if(scores[i] == scores[i+1])							//Save the pair, reroll all else
						save = scores[i];
				}
				if(save == 0){												//Did not have a pair
					save = scores[4];
				}
			}
			for(Dice dice : dices){											//Reroll all dices.
				if(dice.getScore() != save)
					dice.reroll();
			}
			break;
			
		case 10://Four of a kind
			scores = new int[5];
			save = 0;
			
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);
			for(int i = 0; i<2; i++){										//Check for four of a kind
				if(scores[i] == scores[i+3])
					save = scores[i];
			}
			if(save == 0){													//No Four
				for(int i = 0; i<3; i++){									//Check for three of a kind
					if(scores[i] == scores[i+2])
						save = scores[i];
				}
				if(save == 0){												//No three
					for(int i = 0; i<5; i++){								//Check for pair
						if(scores[i] == scores[i+1])
							save = scores[i];
					}
					if(save == 0)
						save = scores[4];
				}
			}
			for(Dice dice : dices){
				if(dice.getScore() != save)
					dice.reroll();
			}
			break;
			
		case 11://Small Straight
			scores = new int[5];
			save = 0;
			for(int i = 0; i<5; i++)
				scores[i] = dices[i].getScore();
			Arrays.sort(scores);
			for(int i = 0; i<5; i++){
				if(scores[i] != i+1)
					save = -1;
			}
			if(save != -1){													//We have a small straight, nothing needed.
				return;
			}else{
				save = 0;
			}
			//We don't have a small straight.
			for(int i = 0; i<5; i++){										//Remove sixes from scores, they will allways reroll.
				if(scores[i] == 6)
					scores[i] = 0;
			}

			for(int i = 0; i<5; i++){										//For all scores
				if(scores[i] == scores[i+1]){								//If a pair reroll.
					for(Dice dice : dices){
						if(dice.getScore() == scores[i]){
							dice.reroll();
							break;
						}
					}
				}
			}
			for(Dice dice : dices){											//If six reroll
				if(dice.getScore() == 6)
					dice.reroll();
			}
			
			break;
			
		case 12://Large Straight
			scores = new int[5];
			save = 0;
			for(int i = 0; i<5; i++)
				scores[i] = dices[i].getScore();
			Arrays.sort(scores);
			for(int i = 0; i<5; i++){
				if(scores[i] != i+2)
					save = -1;
			}
			if(save != -1){													//We have a large straight, nothing needed.
				return;
			}else{
				save = 0;
			}
			//We don't have a large straight.
			for(int i = 0; i<5; i++){										//Remove ones from scores, they will allways reroll.
				if(scores[i] == 1)
					scores[i] = 0;
			}

			for(int i = 0; i<5; i++){										//For all scores
				if(scores[i] == scores[i+1]){								//If a pair 
					for(Dice dice : dices){									//reroll one dice of that value.
						if(dice.getScore() == scores[i]){
							dice.reroll();
							break;
						}
					}
				}
			}
			for(Dice dice : dices){											//If ones reroll
				if(dice.getScore() == 1)
					dice.reroll();
			}
			
			break;
			
		case 13://Full House
			int first = 0;
			int second = 0;
			for(int i = 0; i<5; i++){
				scores[i] = dices[i].getScore();
			}
			Arrays.sort(scores);
			
			for(int i = 0; i<3; i++){										//find Three of a kind.
				if(scores[i] == scores[i+1] && scores[i] ==scores[i+2]){
					first = scores[i];
					break;
				}
			}
			if(first == 0){													//Don't have three of a kind.
				for(int i = 0; i<5; i++){									//Find pair
					if(scores[i] == scores[i+1]){
						first = scores[i];
					}
				}
				if(first == 0){												//Don't have a pair
					first = scores[4];										//Highest
					second = scores[3];										//Second Highest
				}else{														//Pair found for first.
					for(int i = 0; i<4; i++){
						if(scores[i] == scores[i+1] && scores[i] != first){	//Find Pair that makes first!=second.
							second = scores[i];
						}
					}
					if(second == 0){										//No pair for secnd.
						for(int i = 0; i<5; i++){							//Find highest for second.
							if(scores[i] != first)
								second = scores[i];
						}
					}
				}
			} else { 														//find a pair or decide what to save as second.
				for(int i = 0; i<5; i++){
					if(scores[i] == scores[i+1] && scores[i] != first){		//not equal to the chosen three of a kind.
						second = scores[i];
					}
				}
			}
			
			for(Dice dice : dices){											//Reroll dices that we don't wish to save.
				if(dice.getScore() != first && dice.getScore() != second)
					dice.reroll();
			}
			break;
			
		case 14://Chance
			for(Dice dice : dices){
				if(!(dice.getScore()>3)){
					dice.reroll();
				}
			}
			break;
			
		case 15://Yahtzee
			int timesOccured = 0;
			for(int i = 0; i<5; i++){
				int tmp = 0;
				if(scores[i] == i+1){
					tmp++;
				}
				if(tmp >= timesOccured){
					save = i;
					timesOccured = tmp;
				}
			}
			for(Dice dice : dices){
				if(dice.getScore() != save){
					dice.reroll();
				}
			}
			break;
		}
	}
	
	/**
	 * Private help method to calculate the probability mass function of P(X>=k).
	 * 
	 * @param n
	 * @param k
	 */
	private static double probabilityMassFunction(int n, int k){
		/*
		 * The probability mass function is the sum of 
		 * all probabilities for P(X<k) (the probabilities 
		 * that k successes will occur in n attempts).
		 */
		double ret = 0;
		double p=1/6;
		for(int i = 0; i<=k; i++){
			ret += (fact(n)/(fact(i)*fact(n-i)))*Math.pow(p, i)*Math.pow((1-p),n-i);
		}
		return 1-ret;
	}
	
	private static int fact(int n){
		int ret = 1;
		for(int i = 1; i<=n; i++){
			ret*=i;
		}
		return ret;
	}
}
