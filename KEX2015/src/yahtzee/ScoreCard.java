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
	}
	
	public void setScore(int[] rolls, int row){
		
	}
	
}
