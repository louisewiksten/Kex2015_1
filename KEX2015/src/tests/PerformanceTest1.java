package tests;
import yahtzee.*;

/**
 * This class tests the performance of the HumanAlgo algorithm for xx tests. 
 * The result score is the base for the tests. 
 * @author louise
 *
 */
public class PerformanceTest1 {
	
	
	public static void main(String[] args){
		final int runs = 100000; //The number of runs in this test. 
		ScoreCard sc = new ScoreCard();
		Dice[] dices = new Dice[5];
		for (int i = 0; i<5; i++)
			dices[i] = new Dice();
		int totalScore = 0;
		int[] scores = new int[runs]; //Print to an excel file?
		for (int i = 0; i < runs; i++){
			for (int j = 0; j<15; j++){
				HumanAlgo.play(dices, sc);
				
				for(Dice d : dices){
					d.roll();
				}
			}
			totalScore += sc.getTotalScore();
			sc = new ScoreCard();
		}
		
		System.out.println("During "+runs+" tests, the Human Algo algorithm got" +
				" a total \nscore of: "+totalScore + " which means " + 
		totalScore/((double) runs) + " is the average score.");
		
	}
	
}
