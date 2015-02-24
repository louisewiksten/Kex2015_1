package yahtzee;

/**
 * A ScoreCard class that keeps the results of the game.
 *
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
	
	public void setScore(int[] rolls, int row){
		int rowSum = 0;
		for(int i = 0; i < 16; i++){
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
