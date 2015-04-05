package yahtzee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class NewOptimalAlgorithm {
	private static final short NUMBER_OF_ELEMENTS = 6 ^ 5 + 1; // Might not be
																// needed.
	private boolean[] playable;
	private Dice[] dices;
	private ScoreCard sc;

	public NewOptimalAlgorithm(ScoreCard sc) {
		this.sc = sc;
		dices = new Dice[5];
		for (short i = 0; i < 5; i++) {
			dices[i] = new Dice();
		}
		playable = new boolean[16];
		playable[0] = false;

		// Game time
		for (short i = 1; i < 16; i++) {
			for (short j = 0; j < 5; j++) {
				dices[j].roll();
			}
			for (short roll = 2; roll > 0; roll--)
				evaluate(roll);
			score();
		}
	}

	/**
	 * Evaluates, rerolls and scores the dices.
	 * 
	 * @param rollsLeft
	 */
	public void evaluate(short rollsLeft) {
		for (short i = 1; i < 16; i++) {
			if (sc.getRowScore(i) == -1)
				playable[i] = true;
			else
				playable[i] = false;
		}
		simulateRolls(rollsLeft);
	}

	private void simulateRolls(short rollsLeft) {
		/*
		 * Beräkna det förväntade resultatet från varje tärningskast. Spara det
		 * värdet för de tärningarna för att se vilka som ska rullas om.
		 */

		// TODO Någonting är skumt här, den gör shorte alls som den ska.
		// Beräkningarna blir shorte helt rätt misstänker jag.

		double highestExpected = 0.0;
		short[] rerollers = new short[5]; // Save values to check which dices should
										// be rerolled.
		short[] values = new short[5];
		for (short i = 0; i < 5; i++)
			// Values contains all dices values, but is allowed to be changed.
			values[i] = (short) dices[i].getScore();
		final short[] diceValues = values.clone();

		// Case 0, save what I have
		highestExpected = bestScore(diceValues);
		rerollers = diceValues.clone();

		// First loop, 1 dice changed per loop.
		for (short j = 0; j < 5; j++) {
			// short j is the position of the dice.
			double thisExpected = 0.0;
			for (short i = 1; i < 7; i++) {
				// short i is the value of a dice.
				values[j] = i;
				// Calculations for probability
				double prob = probability(diceValues, values, rollsLeft);
				double expect = bestScore(values);

				thisExpected += prob * expect;
			}
			if (highestExpected <= thisExpected) {
				highestExpected = thisExpected;
				rerollers = values.clone();
			}
			values[j] = (short) dices[j].getScore();

		}

		// Second loop, 2 dices changed per loop.
		for (short i = 0; i < 5; i++) {
			// i is the number of the first dice to simulate.
			for (short j = 0; j < 5; j++) {
				// j is the number of the second dice to simulate.
				double thisExpected = 0.0;
				for (short k = 1; k < 7; k++) {
					// k is the value of first dice.
					for (short m = 1; m < 7; m++) {
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
				for (short it = 0; it < 5; it++) {
					values[it] = diceValues[it];
				}
			}
		}
		// Third loop, 3 dices changed per loop.
		for (short i = 0; i < 5; i++) {
			// i is first dice
			for (short j = 0; j < 5; j++) {
				// j is second dice
				for (short k = 0; k < 5; k++) {
					// k is third dice
					double thisExpected = 0.0;
					for (short a = 1; a < 7; a++) {
						// a is value of first dice (dice i)
						for (short b = 1; b < 7; b++) {
							// b is value of second dice (dice j)
							for (short c = 1; c < 7; c++) {
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
					for (short it = 0; it < 5; it++) {
						values[it] = diceValues[it];
					}
				}

			}
		}
		// Fourth roll, 4 dices changed
		for (short i = 0; i < 5; i++) {
			// i is first dice
			for (short j = 0; j < 5; j++) {
				// j is second dice
				for (short k = 0; k < 5; k++) {
					// k is third dice
					for (short l = 0; l < 5; l++) {
						// l is fourth dice
						double thisExpected = 0.0;
						for (short a = 1; a < 7; a++) {
							// a is value of first dice (dice i)
							for (short b = 1; b < 7; b++) {
								// b is value of second dice (dice j)
								for (short c = 1; c < 7; c++) {
									// c is value of third dice(dice k)
									for (short d = 1; d < 7; d++) {
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
						for (short it = 0; it < 5; it++) {
							values[it] = diceValues[it];
						}
					}
				}
			}
		}
		// Last roll
		for (short it = 0; it < 1; it++) {
			// Outer loop so trasch-collector pick up thisExpected.
			short thisExpected = 0;
			for (short i = 1; i < 7; i++) {
				for (short j = 1; j < 7; j++) {
					for (short k = 1; k < 7; k++) {
						for (short l = 1; l < 7; l++) {
							for (short m = 1; m < 7; m++) {
								values = new short[] { i, j, k, l, m };
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
		for (short i = 0; i < 5; i++) {
			if (dices[i].getScore() != rerollers[i]) {
				dices[i].reroll();
			}
		}
	}

	private double bestScore(short[] DiceValues) {
		short[] values = DiceValues.clone();
		Arrays.sort(values);
		short ret = 0;
		short bestScore = 0;
		for (short category = 1; category < 16; category++) {
			ret = 0;
			if (playable[category]) {
				switch (category) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
					for (short value : values) {
						if (value == category)
							ret += value;
					}
					if (ret == 5 * category) {
						ret = 49;
						break;
					} else if (expectedTopSum() - 3 * category + ret > 62)
						ret += 50;
					else
						ret = 0;
					break;
				case 7:// Pair
					for (short i = 0; i < 4; i++) {
						if (values[i] == values[i + 1]) {
							ret = values[i];
						}
					}
					break;
				case 8:// Two pairs
					short fpair = 0;
					short tpair = 0;
					for (short i = 0; i < 4; i++) {
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
						ret = (short) (2 * (fpair + tpair));
					} else {
						ret = 0;
					}
					break;
				case 9:// Three of a kind
					ret = 0;
					for (short i = 0; i < 3; i++) {
						if (values[i] == values[i + 2]) {
							ret = (short) (values[i] * 3);
						}
					}
					break;
				case 10:// Four of a kind
					for (short i = 0; i < 2; i++) {
						if (values[i] == values[i + 3]) {
							ret = (short) (values[i] * 4);
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
						ret = (short) (3 * values[0] + 2 * values[3]); // Small three
																// big pair
					} else if (values[0] == values[1] && values[2] == values[4]
							&& values[0] != values[4]) {
						ret = (short) (2 * values[0] + 3 * values[2]); // Small pair big
																// three
					} else {
						ret = 0;
					}
					break;
				case 14:// Chance
					ret = 0;
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
		// TODO Ask Louise if we could make this method more like here way of
		// writing.
		short bestScore = 0;
		short bestCategory = 0;
		short[] diceValues = new short[5];
		for (short i = 0; i < 5; i++) {
			diceValues[i] = (short) dices[i].getScore();
		}
		Arrays.sort(diceValues);

		short score = 0;

		// Ones-Sixes
		// ****************************************************************
		for (short j = 1; j < 7; j++) {
			score = 0;
			for (short i = 0; i < 5; i++)
				if (diceValues[i] == j)
					score += j;
			if (expectedTopSum() - 3 * j + score > 62)
				score += 50;
			else
				score = 0;
			if (expectedScore((short) 15, diceValues) != 0) {
				if (diceValues[0] == j)
					score = 49;
			}
			if (score >= bestScore && playable[j]) {
				bestCategory = j;
				bestScore = score;
			}
		}

		// Pair (7)
		// ****************************************************************
		score = 0;
		for (short i = 0; i < 4; i++) {
			if (diceValues[i] == diceValues[i + 1])
				score = (short) (diceValues[i] * 2);
		}
		if (score >= bestScore && playable[7]) {
			bestScore = score;
			bestCategory = 7;
		}

		// Two Pairs (8)
		// ****************************************************************
		short pair = 0;
		short spair = 0;
		for (short i = 0; i < 4; i++) {
			if (diceValues[i] == diceValues[i + 1]) {
				if (pair == 0) {
					pair = diceValues[i];
				} else if (spair == 0 && diceValues[i] != pair) {
					spair = diceValues[i];
				}
			}
		}
		if (pair != 0 && spair != 0)
			score = (short) (2 * (pair + spair));
		else
			score = 0;
		if (score >= bestScore && playable[8]) {
			bestScore = score;
			bestCategory = 8;
		}
		// Three of a kind (9)
		// ****************************************************************
		score = 0;
		for (short i = 0; i < 3; i++) {
			if (diceValues[i] == diceValues[i + 2])
				score = (short) (diceValues[i] * 3);
		}
		if (score >= bestScore && playable[9]) {
			bestScore = score;
			bestCategory = 9;
		}

		// Four of a kind (10)
		// ****************************************************************
		score = 0;
		for (short i = 0; i < 2; i++) {
			if (diceValues[i] == diceValues[i + 3])
				score = (short) (diceValues[i] * 4);
		}
		if (score >= bestScore && playable[10]) {
			bestScore = score;
			bestCategory = 10;
		}

		// Small Straight
		// (11)****************************************************************
		score = 0;
		if (diceValues[0] == 1 && diceValues[1] == 2 && diceValues[2] == 3
				&& diceValues[3] == 4 && diceValues[4] == 5)
			score = 15;
		if (score >= bestScore && playable[11]) {
			bestScore = score;
			bestCategory = 11;
		}

		// Large Straight
		// (12)****************************************************************
		score = 0;
		if (diceValues[0] == 2 && diceValues[1] == 3 && diceValues[2] == 4
				&& diceValues[3] == 5 && diceValues[4] == 6)
			score = 20;
		if (score >= bestScore && playable[12]) {
			bestScore = score;
			bestCategory = 12;
			score = 0;
		}

		// Full House
		// (13)****************************************************************
		score = 0;
		if (diceValues[0] == diceValues[2])
			if (diceValues[3] == diceValues[4])
				score = (short) (diceValues[0] * 3 + diceValues[4]);
			else
				score = 0;
		else if (diceValues[0] == diceValues[1])
			if (diceValues[2] == diceValues[4])
				score = (short) (2 * diceValues[0] + 3 * diceValues[4]);
			else
				score = 0;
		else
			score = 0;
		if (score >= bestScore && playable[13]) {
			bestScore = score;
			bestCategory = 13;
			score = 0;
		}

		// No Chance
		// (14)****************************************************************
		/*
		 * score = 0; for (short i = 0; i < 5; i++) score += diceValues[i]; if
		 * (score > bestScore && playable[14]) { bestScore = score; bestCategory
		 * = 14; }
		 */

		// Yahtzee
		// (15)****************************************************************
		score = 0;
		if (diceValues[0] == diceValues[4])
			score = 50;
		if (score >= bestScore && playable[15]) {
			bestScore = score;
			bestCategory = 15;
		}
		if (bestScore == 0) {
			if (playable[14]) {
				bestCategory = 14;
			}
			System.out.println("\nScratching following row:");
			bestCategory = scratch();

		}
		System.out.println("Best row is: " + bestCategory
				+ " with dice values: " + diceValues[0] + ", " + diceValues[1]
				+ ", " + diceValues[2] + ", " + diceValues[3] + " and "
				+ diceValues[4]);
		int[] values = new int[5];
		for(short i = 0; i<5; i++)
			values[i] = (int) diceValues[i];
		sc.setScore(values, bestCategory);
		this.playable[bestCategory] = false;
	}

	private short expectedTopSum() {
		short sum = 0;
		for (short i = 1; i < 7; i++) {
			if (playable[i]) {
				sum += 3 * i;
			} else {
				sum += sc.getRowScore(i);
			}
		}
		return sum;
	}

	/**
	 * 
	 * @return
	 */
	private short scratch() {
		double expVal = Double.MAX_VALUE;
		short rowToDiscard = 0;
		short c = 1;
		for (short i = 7; i < 16; i++) {
			if (playable[i]) {
				c = 7;
				break;
			} else
				c = 1;
		}
		for (short i = c; i < 16; i++) {
			if (playable[i]) {
				double thisVal = 0;
				if (i < 7) {
					short[] val = { 0, 0, 0, 0, 0 };
					short[] valNext = { i, 0, 0, 0, 0 };
					for (short j = 0; j < 5; j++) {
						val[j] = i;
						if (j < 4) {
							valNext[j + 1] = i; // Used because the probability
												// method calculates the chance
												// of at least getting the
												// result.
							thisVal += expectedScore(i, val)
									* (probability(null, val, 3) - probability(
											null, valNext, 3));
						} else {
							thisVal += expectedScore(i, val)
									* (probability(null, val, 3));
						}
					}
				} else if (i == 7) {
					short[] val = { 1, 1, 0, 0, 0 }; // Same probability to
													// receive any pair.
					double prob = probability(null, val, 3);
					for (short j = 0; j < 6; j++) {
						val[0] = (short) (j + 1);
						val[1] = (short) (j + 1);
						thisVal += expectedScore(i, val) * prob;
					}
				} else if (i == 8) {
					short[] val = { 1, 1, 2, 2, 0 }; // Same probability to
													// receive any two pairs.
					double prob = probability(null, val, 3);
					for (short j = 0; j < 6; j++) {
						for (short k = (short) (j + 1); k < 6; k++) {
							val[0] = (short) (j + 1);
							val[1] = (short) (j + 1);
							val[2] = (short) (k + 1);
							val[3] = (short) (k + 1);
							thisVal += expectedScore(i, val) * prob;
						}
					}
				} else if (i == 9) {
					short[] val = { 1, 1, 1, 0, 0 }; // Same probability to
													// receive any pair.
					double prob = probability(null, val, 3);
					for (short j = 0; j < 6; j++) {
						val[0] = (short) (j + 1);
						val[1] = (short) (j + 1);
						val[2] = (short) (j + 1);
						thisVal += expectedScore(i, val) * prob;
					}
				} else if (i == 10) {
					short[] val = { 1, 1, 1, 1, 0 }; // Same probability to
													// receive any three of a
													// kind.
					double prob = probability(null, val, 3);
					for (short j = 0; j < 6; j++) {
						val[0] = (short) (j + 1);
						val[1] = (short) (j + 1);
						val[2] = (short) (j + 1);
						val[3] = (short) (j + 1);
						thisVal += expectedScore(i, val) * prob;
					}
				} else if (i == 11) {
					short[] val = { 1, 2, 3, 4, 5 }; // Same probability to
													// receive any four of a
													// kind.
					double prob = probability(null, val, 3);
					thisVal += expectedScore(i, val) * prob;

				} else if (i == 12) {
					short[] val = { 2, 3, 4, 5, 6 };
					thisVal = 20 * probability(null, val, 3);
				} else if (i == 13) {
					short[] val = new short[5];
					for (short j = 1; j <= 6; j++) {
						for (short k = 1; k <= 6; k++) {
							if (j == k) {
								continue;
							}
							val = new short[] { j, j, j, k, k };
							thisVal += expectedScore(i, val)
									* probability(null, val, 3);
						}
					}
				} else if (i == 15) {
					short[] val = { 1, 1, 1, 1, 1 }; // Same probability to
													// receive any yahtzee.
					double prob = probability(null, val, 3);
					for (short j = 0; j < 6; j++) {
						val[0] = (short) (j + 1);
						val[1] = (short) (j + 1);
						val[2] = (short) (j + 1);
						val[3] = (short) (j + 1);
						val[4] = (short) (j + 1);
						thisVal += 50 * prob;
					}
				}
				if (thisVal < expVal) {
					expVal = thisVal;
					rowToDiscard = i;
				}
			}
		}
		return rowToDiscard;
	}

	/**
	 * 
	 * @param j
	 * @param values
	 * @return
	 */
	private double expectedScore(short j, short[] values) {
		short ret = 0;
		Arrays.sort(values);
		switch (j) {
		case 1:
			for (short value : values) {
				if (value == j)
					ret += value;
			}
			if (ret >= j * 3 && sc.getTopRowSum() < 63) {
				ret += 100;
			}
			if (expectedScore((short)15, values) != 0) {
				if (values[0] == j)
					ret = 49;
			}
			return ret;
		case 2:
			for (short value : values) {
				if (value == j)
					ret += value;
			}
			if (ret <= j) {
				ret = 0;
			} else if (ret >= j * 3) {
				ret += 50;
			}
			if (expectedScore((short)15, values) != 0) {
				if (values[0] == j)
					ret = 49;
			}
			return ret;
		case 3:
		case 4:
		case 5:
			for (short value : values) {
				if (value == j)
					ret += value;
			}
			if (ret <= j * 2) {
				ret = 0;
			} else if (ret >= j * 3) {
				ret += 50;
			}
			if (expectedScore((short)15, values) != 0) {
				if (values[0] == j)
					ret = 49;
			}
			return ret;
		case 6:
			for (short value : values) {
				if (value == j)
					ret += value;
			}
			if (ret <= j * 2) {
				ret = 0;
			} else if (ret >= j * 3) {
				ret += 50;
			}
			return ret;
		case 7:// Pair
			for (short i = 0; i < 4; i++) {
				if (values[i] == values[i + 1]) {
					ret = values[i];
				}
			}
			if (ret < 4) {
				ret = 0;
			}
			return (ret * 2);
		case 8:// Two pairs
			short fpair = 0;
			short tpair = 0;
			for (short i = 0; i < 4; i++) {
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
			if (tpair > 1 && fpair > 0) {
				return (2 * fpair + 2 * tpair);
			} else {
				return 0;
			}
		case 9:// Three of a kind
			for (short i = 0; i < 3; i++) {
				if (values[i] == values[i + 1] && values[i] == values[i + 2]) {
					ret = values[i];
					break;
				}
			}
			if (ret < 2) {
				ret = 0;
			}
			return ret * 3;
		case 10:// Four of a kind
			for (short i = 0; i < 2; i++) {
				if (values[i] == values[i + 1] && values[i] == values[i + 2]
						&& values[i] == values[i + 3]) {
					ret = (short) (values[i] * 4);
					break;
				}
			}
			if (ret < 8) {
				ret = 0;
			}
			return ret;
		case 11:// Small Straight
			if (values[0] == 1 && values[1] == 2 && values[2] == 3
					&& values[3] == 4 && values[4] == 5) {
				return 15;
			} else {
				return 0;
			}
		case 12:// Large Straight
			if (values[0] == 2 && values[1] == 3 && values[2] == 4
					&& values[3] == 5 && values[4] == 6) {
				return 20;
			} else {
				return 0;
			}
		case 13:// Full House
			Arrays.sort(values);
			if (values[0] == values[2] && values[3] == values[4]
					&& values[0] != values[4]) {
				return (3 * values[0] + 2 * values[3]); // Small three big pair
			} else if (values[0] == values[1] && values[2] == values[4]
					&& values[0] != values[4]) {
				return 2 * values[0] + 3 * values[2]; // Small pair big three
			} else {
				return 0;
			}
		case 14:// Chance
			return 0;
		case 15:// Yahtzee
			if (values[0] == values[4])
				return 50;
			return 0;
		}

		return -1; // error, this should not occur!
	}

	/**
	 * Calculates the probability of getting certain dice values in a number of
	 * rolls.
	 * 
	 * @param dices2
	 *            Current dice values
	 * @param dices3
	 *            Wanted dice values
	 * @param l
	 *            Number of rolls left.
	 * @return
	 */
	public static double probability(short[] dices2, short[] dices3, int l) {
		if (dices2 == null) {
			dices2 = new short[] { -1, -1, -1, -1, -1 };
		}
		if(dices2.length < 5){
			short[] array = new short[5];
			for(short i = 0; i<dices2.length; i++){
				array[i] = dices2[i];
			}
			Arrays.sort(array);
			dices2 = array;
		}
		if (Arrays.equals(dices2, dices3))
			return 1.0;
		Arrays.sort(dices2);
		Arrays.sort(dices3);
		short n = 0;
		short k = 0;
		double prob = 0.0;
		short jstart = 0;
		for (short i = 0; i < dices2.length; i++) {
			for(short j = jstart; j < dices3.length; j++){
				if(dices2[i] == dices3[j]){
					n++;
					jstart = (short) (j + 1);
					break;
				}
			}
		}
		n = (short) (5 - n);
		/*
		 * n är antalet olika siffror i current och wanted. Antalet tärningar
		 * som ska rullas om.
		 */

		for (short i : dices3) {
			if (i == 0)
				k++;
		}
		k = (short) (n - k);
		n *= l;

		prob = probabilityMassFunction(n, k);

		return prob;
	}

	/**
	 * Probability mass function of P(X>=k).
	 * 
	 * @param n
	 * @param k
	 */
	private static double probabilityMassFunction(short n, short k) {
		/*
		 * The probability mass function is the sum of all probabilities for
		 * P(X<k) (the probabilities that k successes will occur in n attempts).
		 */
		double ret = 0;
		double p = 1.0 / 6;
		for (short i = 0; i < k; i++) {
			double d1 = (fact(n) / (fact(i) * fact(n - i)));
			double d2 = Math.pow(p, (double) i);
			double d3 = Math.pow((1.0 - p), (double) (n - i));
			ret += d1 * d2 * d3;
		}
		if (n == k) {
			return Math.pow(p, n);
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
		for (short i = 1; i <= n; i++) {
			ret *= (double) i;
		}
		return ret;
	}
}
