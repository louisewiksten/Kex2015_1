package yahtzee;
import java.util.Random;
/**
 * Creates a Dice class that represents a Dice and implements the different functions of
 * the dice.
 * To simlate same dicethrows in different algorithms, we "roll" the dice three times and 
 * remember the rolls so that if reroll is choosen we send the next simulated data.
 * 
 * @author D.Jendeberg and L.Wikstén
 */
public class Dice {
	private int[] score;
	private int current;
	
	public Dice(){
		this.roll();
	}
	

	/**
	 * Simulates rolling the dice. Saves 3 values in case of extra rolls.
	 */
	public void roll(){
		Random rng = new Random();
		score = new int[3];
		//Save value for all extra rolls.
		score[0] = rng.nextInt(6)+1;
		score[1] = rng.nextInt(6)+1;
		score[2] = rng.nextInt(6)+1;
		current = 0;
	}
	/**
	 * Simulates re-rolling the dice, i.e. points to next value.
	 */
	public void reroll(){
		current++;
	}
	/**
	 * Returns number facing upwards on dice.
	 */
	public int getScore(){
		return score[current];
	}
	
	//Below method for testing purposes
	/*public static void main(String args[]){
		Dice d = new Dice();
		d.roll();
		System.out.println(d.getScore());
	}*/
}
