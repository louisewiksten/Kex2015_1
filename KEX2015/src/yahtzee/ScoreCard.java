package yahtzee;

/**
 * A ScoreCard class that keeps the results of the game.
 * 
 * @author D.Jendeberg and L.Wikstén
 */
public class ScoreCard {
	private int[] results;
	private int[][] diceResults;
	private int sum;
	private final static int BONUSLIMIT = 63;
	
	public ScoreCard(){
		results = new int[16]; //Change to n.o. lines
		diceResults = new int[16][5];
		sum = 0;
	}
	
	/**
	 * This Method is used to set the score for a certain row in the scorecard. 
	 * It calculates the score depending on the set of dices and which row is selected.
	 * @param rolls
	 * @param row
	 */
	public void setScore(int[] rolls, int row){
		//TODO change this method to a switch case that calculates the score for selected row given a set of dices.
		int rowSum = 0;
		for(int i = 0; i < 5; i++){
			diceResults[row][i] = rolls[i];
		}
		sum += rowSum;
		results[row] = rowSum;
	}
	
	public int getTotalScore(){
		int tempSum = 0;
		for (int i = 0; i < 6; i++){
			tempSum = results[i];
		}
		if(tempSum >= BONUSLIMIT)
			sum += BONUSLIMIT;
		return sum;
	}
	
}
