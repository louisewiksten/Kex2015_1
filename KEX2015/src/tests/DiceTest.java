package tests;

import yahtzee.Dice;

public class DiceTest {
	/**
	 * Test to see the expectancy value of the Dice class,
	 * which is basically java.util.Random's nextInt() method.
	 * @param args
	 */
	public static void diceTest() {
		//Initilize variables used in function
		double average = 0;
		final int NUMBER_OF_DICES = 1000000;
		//Initialize Dices in list
		Dice[] dices = new Dice[NUMBER_OF_DICES];
		for(int i = 0; i<NUMBER_OF_DICES; i++)
			dices[i] = new Dice();
		//Calculate average score.
		for(int i = 0; i<3; i++){
			for(Dice dice : dices){
				average += (double) dice.getScore();
				dice.reroll();
			}
		}
		average /= (3*NUMBER_OF_DICES);
		//Print information gathered.
		System.out.println("For " + 3*NUMBER_OF_DICES + " tries we recived " + average + " as our expectancy value.");
	}
}
