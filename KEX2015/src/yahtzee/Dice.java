package yahtzee;
import java.util.Random;
/**
 * Creates a Dice class that represents a Dice and implements the different functions of
 * the dice.
 * To simulate same dice throws in different algorithms, we "roll" the dice three times and 
 * remember the rolls so that if re-roll is chosen we send the next simulated data.
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
		score = new int[3];
		//Not necessary..
		Random rng = new Random();
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
	
	/**
	 * Sets the dice rolls to the given values.
	 * @param values
	 */
	public void setValue(int [] values){
		score = new int[3];
		score[0] = values[0];
		score[1] = values[1];
		score[2] = values[2];
		current = 0;
 	}
	
	/**
	 * Resets the dice value to 0.
	 */
	public void reset(){
		current = 0;
	}
	
	//Below method for testing purposes
	/*public static void main(String args[]){
		Dice d = new Dice();
		d.roll();
		System.out.println(d.getScore());
	}*/
}
