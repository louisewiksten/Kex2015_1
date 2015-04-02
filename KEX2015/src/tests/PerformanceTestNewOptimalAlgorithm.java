package tests;

import yahtzee.*;

public class PerformanceTestNewOptimalAlgorithm {

	public static void main(String[] args) {
		ScoreCard sc = new ScoreCard();
		NewOptimalAlgorithm noa = new NewOptimalAlgorithm(sc);
		
		System.out.println("Optimal Algorithm!");
		for (int i = 0; i<15; i++){
			System.out.print("\n"+ sc.rowNames[i] +": " + sc.getRowScore(i+1) + "p - ");
			for(int index = 0; index<5; index++){
				System.out.print(""+sc.getDiceRes()[i+1][index]+ " ");
			}
			
			if(i == 5){ //Print part time score
				System.out.print("\n-------------------" +
						"\nSum of top rows: " + (sc.getTopRowSum()));
				if(sc.getTopRowSum() >= 63){
					System.out.print("\nBouns: 50\n-------------------");
				} else{
					System.out.print("\nBonus: 0\n-------------------");
				}
			}
		}
		System.out.println("\n-------------------\nTotal Score: " + sc.getTotalScore());
	}
}
