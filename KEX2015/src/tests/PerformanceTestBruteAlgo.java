package tests;

import java.io.FileWriter;

import yahtzee.*;
/**
 * Performance test for the BruteAlgo yahtzee algorithm.The result score is the 
 * base for the tests.
 * @author louise
 *
 */
public class PerformanceTestBruteAlgo {
	public static void main(String[] args){
		final int runs = 100000; //The number of runs in this test. 
		ScoreCard sc = new ScoreCard();
		Dice[] dices = new Dice[5];
		for (int i = 0; i<5; i++)
			dices[i] = new Dice();
		int totalScore = 0;
		int maxScore = 0;
		int minScore = 0;
		int tempScore = 0;
		int[] scores = new int[runs]; //Print to an excel file?
		for (int i = 0; i < runs; i++){
			tempScore = 0;
			for (int j = 0; j<15; j++){
				BruteAlgo.play(dices, sc);
				
				for(Dice d : dices){
					d.roll();
				}
			}
			tempScore = sc.getTotalScore();
			scores[i] = tempScore;
			totalScore += tempScore;
			maxScore = Math.max(maxScore, tempScore);
			minScore = Math.min(minScore, tempScore);
			sc = new ScoreCard();
		}
		
		/* ** Write to file** */
		StringBuilder sb = new StringBuilder(""+scores[0]);
		for(int i = 1; i < runs; i++){
			sb.append("\n"+scores[i]);
		}
		try
		{
		    FileWriter writer = new FileWriter("bruteForceAlgo.csv");
		    writer.append(sb.toString());
		    writer.flush();
		    writer.close();
		   
		} catch(Exception e){
			//File error.
			 e.printStackTrace();
		}
		/* * * * * * * * * * */
		
		System.out.println("During "+runs+" tests, the Brute Force algorithm got" +
				" a total \nscore of: "+totalScore + " which means " + 
		totalScore/((double) runs) + " is the average score.\nThe maximum score was: "+maxScore);
		
	}
}
