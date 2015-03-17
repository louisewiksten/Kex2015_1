package yahtzee;

import java.util.Arrays;

/**
 * A ScoreCard class that keeps the results of the game.
 * 
 * @author D.Jendeberg and L.Wikstén
 */
public class ScoreCard {
	private int[] results;
	private int[][] diceResults;
	public String[] rowNames;
	private int sum;
	private final static int BONUSLIMIT = 63;
	
	public ScoreCard(){
		
		results = new int[16]; //Change to n.o. lines
		for(int i = 1; i<16; i++){
			results[i]=-1;
		}
		diceResults = new int[16][5];
		rowNames = new String[16];
		sum = 0;
		//For prints
		rowNames[0]="Ones";
		rowNames[1]="Twos";
		rowNames[2]="Threes";
		rowNames[3]="Fours";
		rowNames[4]="Fives";
		rowNames[5]="Sixes";
		rowNames[6]="Pair";
		rowNames[7]="Two Pair";
		rowNames[8]="Three of a kind";
		rowNames[9]="Four of a kind";
		rowNames[10]="Small straight";
		rowNames[11]="Large straight";
		rowNames[12]="Full house";
		rowNames[13]="Chance";
		rowNames[14]="Yahtzee";
	}
	public int[][] getDiceRes(){
		return diceResults;
	}
	
	public int getRowScore(int row){
		return results[row];
	}
	
	/**
	 * This Method is used to set the score for a certain row in the scorecard. 
	 * It calculates the score depending on the set of dices and which row is selected.
	 * @param rolls
	 * @param row
	 */
	public void setScore(int[] rolls, int row){
		int rowSum = 0;
		Arrays.sort(rolls);
		switch(row){
			case 1: 	
			case 2: 
			case 3: 
			case 4: 
			case 5: 
			case 6: 
				for(int r : rolls)
					if(r == row)
						rowSum += r;
				break;
			case 7: 
				for(int i = 4; i > 0; i--)
					if(rolls[i] == rolls[i-1]){
						rowSum += rolls[i] + rolls[i-1];
						break;
					}
				break;
			case 8: 
				for(int i = 4; i > 2; i--)
					if(rolls[i] == rolls[i-1]){
						if(rolls[i-2] == rolls[i-3] && rolls[i] != rolls[i-2]){ //First pair != second pair
							rowSum += 2*rolls[i] + 2*rolls[i-2];
							break;
						} else if(i > 3 && rolls[i-3] == rolls[i-4] && rolls[i] != rolls[i-3]){ 
							//Rolls >3 otherwise error. First pair != second pair
							rowSum += 2*rolls[i] + 2*rolls[i-3];
							break;
						}
					}
				break;
			case 9:
				for(int i = 0; i < 3; i++){
					if(rolls[i] == rolls[i+1] && rolls[i] == rolls[i+2] ){
						rowSum += 3*rolls[i]; //Three of a kind sum
						break;
					}
				}
				break;
			case 10:
				for(int i = 0; i < 2; i++){
					if(rolls[i] == rolls[i+1] && rolls[i] == rolls[i+2] && rolls[i] == rolls[i+3]){
						rowSum += 4*rolls[i]; //Four of a kind sum
						break;
					}
				}
				break;
			case 11:
				if(rolls[0]==1 && rolls[1]==2 && rolls[2]==3 && rolls[3]==4 && rolls[4]==5) 
					rowSum+=15;
				break;
			case 12:
				if(rolls[0]==2 && rolls[1]==3 && rolls[2]==4 && rolls[3]==5 && rolls[4]==6) 
					rowSum+=20;
				break;
			case 13:
					if(rolls[0] == rolls[1] && rolls[0] == rolls[2]){
						if(rolls[3] == rolls[4]){
							rowSum += rolls[0]+rolls[1]+rolls[2]+rolls[3]+rolls[4];
						}
					} else if(rolls[0] == rolls[1]){
						if(rolls[2] == rolls[3] && rolls[2] == rolls[4]){
							rowSum += rolls[0]+rolls[1]+rolls[2]+rolls[3]+rolls[4];
						}
					}
					break;
			case 14:
				for(int i = 0; i < 5; i++)
					rowSum += rolls[i]; //Chance sum
				break;
			case 15:
				if(rolls[0] == rolls[1] && rolls[0] == rolls[2] && rolls[0] == rolls[3] && rolls[0] == rolls[4]){
					rowSum += 50; //Four of a kind sum
					break;
				}
				break;
		}
		for(int i = 0; i < 5; i++){
			diceResults[row][i] = rolls[i];
		}
		sum += rowSum;
		results[row] = rowSum;
	}
	
	public int getTotalScore(){
		return sum;
	}
	
	public void addBonus(){
		int tempSum = 0;
		for (int i = 1; i < 7; i++){
			tempSum += results[i];
		}
		if(tempSum >= BONUSLIMIT){
			sum += 50;
		}
	}
	
	
	public static void main(String[] args){
		ScoreCard sc = new ScoreCard();
		Dice[] dices = new Dice[5];
		for (int i = 0; i<5; i++)
			dices[i] = new Dice();
		for (int i = 0; i<15; i++){
			BruteAlgo.play(dices, sc);
			for(Dice d : dices){
				d.roll();
			}
			System.out.print("\n"+ sc.rowNames[i] +": " + sc.getRowScore(i+1) + "p - ");
			for(int index = 0; index<5; index++){
				System.out.print(""+sc.getDiceRes()[i+1][index]+ " ");
			}
			
			if(i == 5){ //Print part time score
				System.out.print("\n-------------------" +
						"\nSum of top rows: " + (sc.getTotalScore()));
				if(sc.getTotalScore() >= 63){
					System.out.print("\nBouns: 50\n-------------------");
				} else{
					System.out.print("\nBonus: 0\n-------------------");
				}
			}
		}
		System.out.println("\n-------------------\nTotal Score: " + sc.getTotalScore());
		
	}
	
}
