package yahtzee;
import java.util.Random;
/**
 * Creates a Dice class that represents a Dice and implements the different functions of
 * the dice.
 * @author D.Jendeberg and L.Wiksten
 *
 */
public class Dice {
	private int score;
	
	public Dice(){
		this.roll();
	}
	
	/**
	 * Simulates rolling the dice.
	 */
	public void roll(){
		Random rng = new Random();
		score = rng.nextInt(5)+1;
	}
	
	/**
	 * Returns number facing upwards on dice.
	 */
	public int getScore(){
		return score;
	}
	
	//Below method for testing purposes
	/*public static void main(String args[]){
		Dice d = new Dice();
		d.roll();
		System.out.println(d.getScore());
	}*/
}
