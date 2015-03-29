package tests;

import yahtzee.*;

public class SameValueTest1 {
	public static void main(String[] args){
		
		ScoreCard sc = new ScoreCard();
		ScoreCard sc2 = new ScoreCard();
		
		Dice[] dices = new Dice[5];
		for (int i = 0; i<5; i++)
			dices[i] = new Dice();
		for (int i = 0; i<15; i++){
			BruteAlgo.play(dices, sc);
			
			for(int j = 0; j < 5; j++){
				dices[j].reset(); //Start with roll 0 for each round.
			}
			
			HumanAlgo.play(dices, sc2);
			
			for(Dice d : dices){
				d.roll();
			}
			
		}
		System.out.println("The total score for the First Look Approach algorithm is:"+sc2.getTotalScore());
		System.out.println("The total score for the Forced Scoring algorithm is:"+sc.getTotalScore());
	}
}
