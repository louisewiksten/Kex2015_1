package tests;

import java.io.*;

import yahtzee.*;

public class SameValueTest1 {
	public static void main(String[] args) throws FileNotFoundException{
		ScoreCard sc = new ScoreCard(); //ForcedAlgo
		ScoreCard sc2 = new ScoreCard();//HumanAlgo
		ScoreCard sc3 = new ScoreCard();//OptimizedAlgo
		
		String [] valuelist = new String[15];
		int [] thisDiceV = new int [3]; //The values to send to the dice.
		
		Dice[] dices = new Dice[5]; //Create dices
		for (int i = 0; i<5; i++){
			dices[i] = new Dice();
		}
		
		
		/*Reading from file*/
		BufferedReader br = new BufferedReader(new FileReader("dice19.txt"));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            //sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        valuelist = everything.split("#");
	    }catch(Exception e){
	    	System.out.println("");
	    } try {
	        br.close();
	    } catch(Exception e){ System.out.println("Could not close file.");}
	    /*Close file - reading.*/
	    

		
		for (int i = 0; i<15; i++){
			
			String [] thisRound = valuelist[i*2].split(" ");
			int iter = 0;
			for(Dice d : dices){ //Simulate rerolls (or values for dice.
				thisDiceV[0] = Integer.parseInt(thisRound[iter]);
				iter++;
				thisDiceV[1] = Integer.parseInt(thisRound[iter]);
				iter++;
				thisDiceV[2] = Integer.parseInt(thisRound[iter]);
				iter++;
				d.setValue(thisDiceV); //TODO read values.
				System.out.println("Values for dice: "+thisDiceV[0]+","+thisDiceV[1]+","+thisDiceV[2]);
			}
			System.out.println("-");
			
			
			//Playing:
			BruteAlgo.play(dices, sc);
			for(int j = 0; j < 5; j++){
				dices[j].reset(); //Start with first throw for each round.
			}
			HumanAlgo.play(dices, sc2);
			for(int j = 0; j < 5; j++){
				dices[j].reset(); //Start with first throw for each round.
			}
			OptimizedAlgo.play(dices,sc3);
		}
		
		
		System.out.println("The total score for the Optimal Human Algorithm is: "+sc3.getTotalScore());
		System.out.println("The total score for the Human algorithm is:"+sc2.getTotalScore());
		System.out.println("The total score for the Forced Scoring algorithm is:"+sc.getTotalScore());
		
	}
}
