package yahtzee;

import java.io.FileWriter;
import java.util.*;

/**
 * Class that creates a number of files with dice values. 
 * @author D.Jendeberg & L.Wiksten
 *
 */
public class DiceFiles {
	Random rand; 
	StringBuilder sb;
	
	/**
	 * Create dice files and write to file(s).
	 */
	public DiceFiles(String fileName){
		sb = new StringBuilder();
		rand = new Random();
		int [][] diceValues = new int[5][3]; //Values for one file.
		for (int turn = 0; turn < 15; turn++){
			for(int i = 0; i < 5; i++){ //Create diceValues.
				for (int j = 0; j < 3; j++){
					diceValues[i][j] = rand.nextInt(6)+1;
					sb.append(diceValues[i][j]+" ");
				}
				sb.append("\n");
			}
			sb.append("##\n");
		}
		writeToFile(fileName);
		
	}
	
	
	public void writeToFile(String fileName){
		try
		{
		    FileWriter writer = new FileWriter(fileName);
		    writer.append(sb.toString());
		    writer.flush();
		    writer.close();
		   
		} catch(Exception e){
			//File error.
			 e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		DiceFiles df;
		for(int i = 1; i < 51; i++)
			df = new DiceFiles("dice"+i+".txt");
		System.out.println("Done!");
	}
}
