package tests;

import yahtzee.*;

public class PerformanceTestOptimizedHumanAlgo {
	public static void main(String[] args){
		final int runs = 100000; //The number of runs in this test. 
		ScoreCard sc = new ScoreCard();
		Dice[] dices = new Dice[5];
		for (int i = 0; i<5; i++)
			dices[i] = new Dice();
		int totalScore = 0;
		int maxScore = 0;
		int minScore = 330;
		//int[] scores = new int[runs]; //Print to an excel file?
		for (int i = 0; i < runs; i++){
			for (int j = 0; j<15; j++){
				OptimizedAlgo.play(dices, sc);
				
				for(Dice d : dices){
					d.roll();
				}
			}
			totalScore += sc.getTotalScore();
			maxScore = Math.max(maxScore, sc.getTotalScore());
			minScore = Math.min(minScore, sc.getTotalScore());
			sc = new ScoreCard();
		}
		
		System.out.println("During "+runs+" tests, the Optimized Algorithm got" +
				" a total \nscore of: "+totalScore + " which means " + 
		totalScore/((double) runs) + " is the average score.\nThe maximum score was: "+maxScore+"" +
				" and the minimum score was: "+minScore);
		
	}
}
