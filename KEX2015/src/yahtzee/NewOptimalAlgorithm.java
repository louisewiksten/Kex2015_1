package yahtzee;

import java.util.Arrays;
import java.util.Random;

public class NewOptimalAlgorithm {
	private static final int NUMBER_OF_ELEMENTS = 6 ^ 5 + 1; // Might not be
																// needed.
	private boolean[] playable;
	private Dice[] dices;
	private ScoreCard sc;

	public NewOptimalAlgorithm(ScoreCard sc) {
		dices = new Dice[5];

		// Game time
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 5; j++) {
				dices[j].roll();
				for (int roll = 2; roll > 0; roll--)
					evaluate(roll);
				score();
			}
		}
	}

	/**
	 * Evaluates, rerolls and scores the dices.
	 * 
	 * @param rollsLeft
	 */
	public void evaluate(int rollsLeft) {
		playable = new boolean[16];
		playable[0] = false;
		for (int i = 1; i < 16; i++) {
			if (sc.getRowScore(i) == -1)
				playable[i] = true;
			else
				playable[i] = false;
		}
		simulateRolls(rollsLeft);
	}

	private void simulateRolls(int rollsLeft) {
		/*
		 * Beräkna det förväntade resultatet från varje tärningskast. Spara det
		 * värdet för de tärningarna för att se vilka som ska rullas om.
		 */

		double highestExpected = 0.0;
		int[] rerollers = new int[5]; // Save values to check which dices should
										// be rerolled.
		int[] values = new int[5];
		for (int i = 0; i < 5; i++)
			// Values contains all dices values, but is allowed to be changed.
			values[i] = dices[i].getScore();
		final int[] diceValues = values.clone();

		// Case 0, save what I have
		highestExpected = bestScore(diceValues);
		rerollers = diceValues.clone();

		// First loop, 1 dice changed per loop.
		for (int j = 0; j < 5; j++) {
			// Int j is the position of the dice.
			double thisExpected = 0.0;
			for (int i = 1; i < 7; i++) {
				// Int i is the value of a dice.
				values[j] = i;
				// Calculations for probability
				double prob = probability(diceValues, values, rollsLeft);
				double expect = bestScore(values);

				thisExpected += prob * expect;
			}
			if (highestExpected < thisExpected) {
				highestExpected = thisExpected;
				rerollers = values.clone();
			}
			values[j] = dices[j].getScore();

		}

		// Second loop, 2 dices changed per loop.
		for (int i = 0; i < 5; i++) {
			// i is the number of the first dice to simulate.
			for (int j = 0; j < 5; j++) {
				// j is the number of the second dice to simulate.
				double thisExpected = 0.0;
				for (int k = 1; k < 7; k++) {
					// k is the value of first dice.
					for (int m = 1; m < 7; m++) {
						// m is the value of second dice.
						values[i] = k;
						values[j] = m;
						// Calculations for probability
						double prob = probability(diceValues, values, rollsLeft);
						double expect = bestScore(values);

						thisExpected += prob * expect;

					}
				}
				if (highestExpected < thisExpected) {
					highestExpected = thisExpected;
					rerollers = values.clone();
				}
				for (int it = 0; it < 5; it++) {
					values[it] = diceValues[it];
				}
			}
		}
		// Third loop, 3 dices changed per loop.
		for (int i = 0; i < 5; i++) {
			// i is first dice
			for (int j = 0; j < 5; j++) {
				// j is second dice
				for (int k = 0; k < 5; k++) {
					// k is third dice
					double thisExpected = 0.0;
					for (int a = 1; a < 7; a++) {
						// a is value of first dice (dice i)
						for (int b = 1; b < 7; b++) {
							// b is value of second dice (dice j)
							for (int c = 1; c < 7; c++) {
								// c is value of third dice(dice k)
								values[i] = a;
								values[j] = b;
								values[k] = c;
								// Calculations for probability
								double prob = probability(diceValues, values,
										rollsLeft);
								double expect = bestScore(values);

								thisExpected += prob * expect;

							}
						}

					}
					if (highestExpected < thisExpected) {
						highestExpected = thisExpected;
						rerollers = values.clone();
					}
					for (int it = 0; it < 5; it++) {
						values[it] = diceValues[it];
					}
				}

			}
		}
		// Fourth roll, 4 dices changed
		for (int i = 0; i < 5; i++) {
			// i is first dice
			for (int j = 0; j < 5; j++) {
				// j is second dice
				for (int k = 0; k < 5; k++) {
					// k is third dice
					for (int l = 0; l < 5; l++) {
						// l is fourth dice
						double thisExpected = 0.0;
						for (int a = 1; a < 7; a++) {
							// a is value of first dice (dice i)
							for (int b = 1; b < 7; b++) {
								// b is value of second dice (dice j)
								for (int c = 1; c < 7; c++) {
									// c is value of third dice(dice k)
									for (int d = 1; d < 7; d++) {
										// d is value of fourth dice(dice l)
										values[i] = a;
										values[j] = b;
										values[k] = c;
										values[l] = d;
										// Calculations for probability
										double prob = probability(diceValues,
												values, rollsLeft);
										double expect = bestScore(values);

										thisExpected += prob * expect;

									}
								}
							}
						}
						if (highestExpected < thisExpected) {
							highestExpected = thisExpected;
							rerollers = values.clone();
						}
						for (int it = 0; it < 5; it++) {
							values[it] = diceValues[it];
						}
					}
				}
			}
		}
		// Last roll
		for (int it = 0; it < 1; it++) {
			// Outer loop so trasch-collector pick up thisExpected.
			int thisExpected = 0;
			for (int i = 1; i < 7; i++) {
				for (int j = 1; j < 7; j++) {
					for (int k = 1; k < 7; k++) {
						for (int l = 1; l < 7; l++) {
							for (int m = 1; m < 7; m++) {
								values = new int[] { i, j, k, l, m };
								double prob = probability(diceValues, values,
										rollsLeft);
								double expect = bestScore(values);
								thisExpected += prob * expect;
							}
						}
					}
				}
			}
			if (highestExpected < thisExpected) {
				highestExpected = thisExpected;
				rerollers = values.clone();
			}
		}

		// End loop, for rerolling.
		// Rerolling those dices that differ between
		// rerollers and dices.
		for (int i = 0; i < 5; i++) {
			if (dices[i].getScore() != rerollers[i]) {
				dices[i].reroll();
			}
		}
	}

	private double bestScore(int[] DiceValues) {
		int[] values = DiceValues.clone();
		Arrays.sort(values);
		int ret = 0;
		int bestScore = 0;
		for (int category = 1; category < 16; category++) {
			if (playable[category]) {
				switch (category) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
					for (int value : values) {
						if (value == category)
							ret += value;
					}
					break;
				case 7:// Pair
					for (int i = 0; i < 4; i++) {
						if (values[i] == values[i + 1]) {
							ret = values[i];
						}
					}
					break;
				case 8:// Two pairs
					int fpair = 0;
					int tpair = 0;
					for (int i = 0; i < 4; i++) {
						if (values[i] == values[i + 1]) {
							if (fpair == 0) {
								fpair = values[i];
								i++;
							} else {
								if (fpair != values[i]) {
									tpair = values[i];
									i = 5;
								}
							}
						}
					}
					if (fpair != 0 && tpair != 0) {
						ret = 2 * (fpair + tpair);
					} else {
						ret = 0;
					}
					break;
				case 9:// Three of a kind
					ret = 0;
					for (int i = 0; i < 3; i++) {
						if (values[i] == values[i + 2]) {
							ret = values[i] * 3;
						}
					}
					break;
				case 10:// Four of a kind
					for (int i = 0; i < 2; i++) {
						if (values[i] == values[i + 3]) {
							ret = values[i] * 4;
						}
					}
					break;
				case 11:// Small Straight
					if (values[0] == 1 && values[1] == 2 && values[2] == 3
							&& values[3] == 4 && values[4] == 5) {
						ret = 15;
					} else {
						ret = 0;
					}
					break;
				case 12:// Large Straight
					if (values[0] == 2 && values[1] == 3 && values[2] == 4
							&& values[3] == 5 && values[4] == 6) {
						ret = 20;
					} else {
						ret = 0;
					}
					break;
				case 13:// Full House
					Arrays.sort(values);
					if (values[0] == values[2] && values[3] == values[4]
							&& values[0] != values[4]) {
						ret = (3 * values[0] + 2 * values[3]); // Small three
																// big pair
					} else if (values[0] == values[1] && values[2] == values[4]
							&& values[0] != values[4]) {
						ret = 2 * values[0] + 3 * values[2]; // Small pair big
																// three
					} else {
						ret = 0;
					}
					break;
				case 14:// Chance
					ret = 0;
					for (int i = 0; i < 5; i++) {
						ret += values[i];
					}
					break;
				case 15:// Yahtzee
					if (values[0] == values[4])
						ret = 50;
					else
						ret = 0;
				}
				if (ret > bestScore)
					bestScore = ret;
			}
		}
		return bestScore;
	}

	private void score() {
		// TODO Fix an evaluating scoring method.
		int bestScore = 0;
		int bestCategory = 0;
		int[] diceValues = new int[5];
		for (int i = 0; i < 5; i++) {
			diceValues[i] = dices[i].getScore();
		}
		Arrays.sort(diceValues);

		int score = 0;

		// Ones-Sixes ****************************************************************
		for (int j = 1; j < 7; j++) {
			score = 0;
			for (int i = 0; i < 5; i++)
				if (diceValues[i] == 1)
					score += 1;
			if (score >= bestScore && playable[j]) {
				bestCategory = j;
				bestScore = score;
			}
		}

		// Pair (7) ****************************************************************
		score = 0;
		for (int i = 0; i < 4; i++) {
			if (diceValues[i] == diceValues[i + 1])
				score = diceValues[i] * 2;
		}
		if (score > bestScore && playable[7]) {
			bestScore = score;
			bestCategory = 7;
		}
		
		//Two Pairs (8) ****************************************************************
		int pair = 0;
		int spair = 0;
		for(int i = 0; i<4; i++){
			if(diceValues[i] == diceValues[i+1]){
				if(pair == 0){
					pair = diceValues[i];
				}else if(spair == 0 && diceValues[i] != pair){
					spair = diceValues[i];
				}
			}
		}
		if(pair != 0 && spair != 0)
			score = 2*(pair + spair);
		else
			score = 0;
		if (score > bestScore && playable[8]) {
			bestScore = score;
			bestCategory = 8;
		}
		//Three of a kind (9) ****************************************************************
		score = 0;
		for (int i = 0; i < 3; i++) {
			if (diceValues[i] == diceValues[i + 2])
				score = diceValues[i] * 3;
		}
		if (score > bestScore && playable[9]) {
			bestScore = score;
			bestCategory = 9;
		}
		
		//Four of a kind (10) ****************************************************************
		score = 0;
		for (int i = 0; i < 2; i++) {
			if (diceValues[i] == diceValues[i + 3])
				score = diceValues[i] * 4;
		}
		if (score > bestScore && playable[10]) {
			bestScore = score;
			bestCategory = 10;
		}
		
		//Small Straight (11)**************************************************************** 
		score = 0;
		if(diceValues[0] == 1 && diceValues[1] == 2 && diceValues[2] == 3 && diceValues[3] == 4 && diceValues[4] == 5)
			score = 15;
		if(score > bestScore && playable[11]){
			bestScore = score;
			bestCategory = 11;
		}
		
		//Large Straight (12)**************************************************************** 
		score = 0;
		if(diceValues[0] == 2 && diceValues[1] == 3 && diceValues[2] == 4 && diceValues[3] == 5 && diceValues[4] == 6)
			score = 20;
		if(score > bestScore && playable[12]){
			bestScore = score;
			bestCategory = 12;
			score = 0;
		}
		
		//Full House (13)****************************************************************
		score = 0;
		if(diceValues[0] == diceValues[2])
			if(diceValues[3] == diceValues[4])
				score = diceValues[0]*3+diceValues[4];
			else
				score = 0;
		else if(diceValues[0] == diceValues[1])
			if(diceValues[2] == diceValues[4])
				score = 2*diceValues[0]+3*diceValues[4];
			else
				score=0;
		else
			score = 0;
		if(score > bestScore && playable[13]){
			bestScore = score;
			bestCategory = 13;
			score = 0;
		}
		
		//Chance (14)****************************************************************
		score = 0;
		for(int i = 0; i<5; i++)
			score += diceValues[i];
		if(score > bestScore && playable[14]){
			bestScore = score;
			bestCategory = 14;
		}
		
		//Yahtzee (15)****************************************************************
		score = 0;
		if (diceValues[0] == diceValues[4])
		score = 50;
		if(score > bestScore && playable[15]){
			bestScore = score;
			bestCategory = 15;
		}
		if(bestScore == 0)
			bestCategory = scratch();
		sc.setScore(diceValues, bestCategory);
		this.playable[bestCategory] = false;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private int scratch() {
		double expVal = Double.MAX_VALUE;
		int rowToDiscard = 0;
		for(int i = 1; i < 16; i++){
			if(playable[i]){
				double thisVal = 0;
				if(i < 7){
					int[] val = {0,0,0,0,0};
					int[] valNext = {i,0,0,0,0};
					for(int j = 0; j<5; j++){
						val[j] = i;
						if(j < 4){
							valNext[j+1] = i; //Used because the probability method calculates the chance of at least getting the result.
							thisVal += expectedScore(i,val)*(probability(null,val,3)-probability(null,valNext,3));
						}else{
							thisVal += expectedScore(i,val)*(probability(null,val,3));
						}
						
					}
				} else if(i == 7){
					int[] val = {1,1,0,0,0}; //Same probability to receive any pair.
					double prob = probability(null,val,3);
					for(int j = 0; j < 6; j++){
						val[0] = j+1;
						val[1] = j+1;
						thisVal += expectedScore(i,val)*prob;
					}
				} else if(i == 8){
					int[] val = {1,1,2,2,0}; //Same probability to receive any two pairs.
					double prob = probability(null,val,3);
					for(int j=0; j<6; j++){
						for(int k=j+1;k<6;k++){
							val[0] = j+1;
							val[1] = j+1;
							val[2] = k+1;
							val[3] = k+1;
							thisVal += expectedScore(i,val)*prob;
						}
					}
				} else if(i == 9){
					int[] val = {1,1,1,0,0}; //Same probability to receive any pair.
					double prob = probability(null,val,3);
					for(int j = 0; j < 6; j++){
						val[0] = j+1;
						val[1] = j+1;
						val[2] = j+1;
						thisVal += expectedScore(i,val)*prob;
					}
				} else if(i == 10){
					int[] val = {1,1,1,1,0}; //Same probability to receive any three of a kind.
					double prob = probability(null,val,3);
					for(int j = 0; j < 6; j++){
						val[0] = j+1;
						val[1] = j+1;
						val[2] = j+1;
						val[3] = j+1;
						thisVal += expectedScore(i,val)*prob;
					}
				} else if(i == 11){
					int[] val = {1,2,3,4,5}; //Same probability to receive any four of a kind.
					double prob = probability(null,val,3);
					thisVal += expectedScore(i,val)*prob;
					
				} else if(i == 12){
					int[] val = {2,3,4,5,6};
					thisVal = 20*probability(null,val,3);
				} else if(i == 13){
					int[] val = new int[5];
					for (int j = 1; j<=6; j++){
						for(int k = 1; k<=6; k++){
							if(j == k){
								continue;
							}
							val = new int[]{j,j,j,k,k};
							thisVal += expectedScore(i,val)*probability(null,val,3);
						}
					}
				} else if(i == 15){
					int[] val = {1,1,1,1,1}; //Same probability to receive any yahtzee.
					double prob = probability(null,val,3);
					for(int j = 0; j < 6; j++){
						val[0] = j+1;
						val[1] = j+1;
						val[2] = j+1;
						val[3] = j+1;
						val[4] = j+1;
						thisVal += 50*prob;
					}
				}
				if(thisVal < expVal){
					expVal=thisVal;
					rowToDiscard = i;
				}
			}
		}
		return rowToDiscard;
	}
	
	/**
	 * 
	 * @param category
	 * @param values
	 * @return
	 */
	private static double expectedScore(int category, int[] values){
		int ret = 0;
		Arrays.sort(values);
		switch(category){
		case 1:
			for(int value : values){
				if(value == category)
					ret+=value;
			}
			if(ret >= category*3){				
				ret+=50;
			}
			if(expectedScore(15,values) != 0){
				if(values[0] == category)
					ret = 49;
			}
			return ret;
		case 2:
			for(int value : values){
				if(value == category)
					ret+=value;
			}
			if(ret <= category){
				ret = 0;
			} else if(ret >= category*3){				
				ret+=50;
			}
			if(expectedScore(15,values) != 0){
				if(values[0] == category)
					ret = 49;
			}
			return ret;
		case 3:
		case 4:
		case 5:
			for(int value : values){
				if(value == category)
					ret+=value;
			}
			if(ret <= category*2){
				ret = 0;
			} else if(ret >= category*3){				
				ret+=50;
			}
			if(expectedScore(15,values) != 0){
				if(values[0] == category)
					ret = 49;
			}
			return ret;
		case 6:
			for(int value : values){
				if(value == category)
					ret+=value;
			}
			if(ret <= category*2){
				ret = 0;
			} else if(ret >= category*3){
				ret+=50;
			}
			return ret;
		case 7://Pair
			for(int i = 0; i<4; i++){
				if(values[i]==values[i+1]){
					ret = values[i];
				}
			}
			if(ret < 4){
				ret = 0;
			}
			return (ret*2);
		case 8://Two pairs
			int fpair = 0;
			int tpair = 0;
			for(int i = 0; i<4;i++){
				if(values[i] == values[i+1]){	
					if(fpair==0){
						fpair = values[i];
						i++;
					}else{
						if(fpair != values[i]){
							tpair = values[i];
							i = 5;
						}
					}
				}
			}
			if(tpair > 1 && fpair > 0){
				return (2*fpair + 2*tpair);
			}else{
				return 0;
			}
		case 9://Three of a kind
			for(int i = 0; i<3; i++){
				if(values[i] == values[i+1] && values[i] == values[i+2]){
					ret = values[i];
					break;
				}
			} 
			if(ret < 2){
				ret = 0;
			}
			return ret*3;
		case 10://Four of a kind
			for(int i = 0; i<2; i++){
				if(values[i]==values[i+1] && values[i] == values[i+2] && values[i] == values[i+3]){
					ret = values[i]*4;
					break;
				}
			}
			if(ret < 8){
				ret = 0;
			}
			return ret;
		case 11://Small Straight
			if(values[0]==1 && values[1]==2 && values[2] == 3 && values[3] == 4 && values[4] == 5){
				return 15;
			}else{
				return 0;
			}
		case 12://Large Straight
			if(values[0]==2 && values[1]==3 && values[2] == 4 && values[3] == 5 && values[4] == 6){
				return 20;
			}else{
				return 0;
			}
		case 13://Full House
			Arrays.sort(values);
			if(values[0] == values[2] && values[3] == values[4] && values[0]!=values[4]){ 
				return (3*values[0] + 2*values[3]); //Small three big pair
			} else if(values[0] == values[1] && values[2] == values[4] && values[0]!=values[4]){
				return 2*values[0] + 3*values[2]; //Small pair big three
			} else {
				return 0;
			}
		case 14://Chance
			return 0;
		case 15://Yahtzee
			if(values[0] == values[4])
				return 50;
			return 0;
		}
		
		return -1; //error, this should not occur!
	}

	/**
	 * Calculates the probability of getting certain dice values in a number of
	 * rolls.
	 * 
	 * @param current
	 *            Current dice values
	 * @param wanted
	 *            Wanted dice values
	 * @param rollsLeft
	 *            Number of rolls left.
	 * @return
	 */
	private static double probability(int[] current, int[] wanted, int rollsLeft) {
		Arrays.sort(current);
		Arrays.sort(wanted);
		int n = 0;
		int k = 0;
		double prob = 0.0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (current[i] == wanted[j]) {
					n++;
					break;
				}
			}
		}
		n = 5 - n;
		/*
		 * n är antalet olika siffror i current och wanted. Antalet tärningar
		 * som ska rullas om.
		 */

		for (int i : wanted) {
			if (i == 0)
				k++;
		}
		k = n - k;
		n *= rollsLeft;

		for (int i = 0; i < k; i++) {
			prob = probabilityMassFunction(n, k);
		}

		return prob;
	}

	/**
	 * Probability mass function of P(X>=k).
	 * 
	 * @param n
	 * @param k
	 */
	private static double probabilityMassFunction(int n, int k) {
		/*
		 * The probability mass function is the sum of all probabilities for
		 * P(X<k) (the probabilities that k successes will occur in n attempts).
		 */
		double ret = 0;
		double p = 1.0 / 6;
		for (int i = 0; i < k; i++) {
			double d1 = (fact(n) / (fact(i) * fact(n - i)));
			double d2 = Math.pow(p, (double) i);
			double d3 = Math.pow((1.0 - p), (double) (n - i));
			ret += d1 * d2 * d3;
		}
		return 1 - ret;
	}

	/**
	 * Calculates the factorial of the double n.
	 * 
	 * @param n
	 * @return
	 */
	private static double fact(double n) {
		double ret = 1;
		for (int i = 1; i <= n; i++) {
			ret *= (double) i;
		}
		return ret;
	}
}
