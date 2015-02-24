package KEX2015.src.diceClass;

/**
 * Creates a Dice class that represents a Dice and implements the different functions of
 * the dice.
 * @author D.Jendeberg and L.Wiksten
 *
 */
public class Dice {
	public int score;
	
	public Dice(){
		
	}
	
	public void roll(){
		score = 1;
	}
	
	public int getScore(){
		return score;
	}
}
