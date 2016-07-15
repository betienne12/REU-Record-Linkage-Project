package edu.dase.prl.ExcelFiles;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class CorruptedDataGenerator {

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter("/C:/Users/Brian/git/prl/data/Phonetic_Edits/Phonetic_Edits_5_2.csv");
		 Scanner scanner = new Scanner(new File("/C:/Users/Brian/git/prl/data/Phonetic_Edits/Phonetic_Edits_5.csv"));
	       // scanner.useDelimiter(",");
	        
	        while(scanner.hasNext()){
	        	String line = scanner.nextLine();
	        	String[] tokens = line.split("[,]");
	        	String corruptedFirstName = tokens[0].trim();
	        	String corruptedLastName = tokens[1].trim();
	        	String orgFirst=tokens[2].trim();
	        	String orgLast=tokens[3].trim();
	        	double percentage=Double.parseDouble(tokens[4]);
	        	
	        	//if ((percentage>0.05)&&(percentage<=0.1)){
				//if ((percentage>0.02)&&(percentage<=0.05)){
				if ((percentage>=0)&&(percentage<=0.02)){
					pw.print(corruptedFirstName + "," + corruptedLastName+ ",");
				}
				else{
					pw.print(orgFirst + "," + orgLast+",");
				}
				pw.println();

	        }
	        scanner.close();
	        pw.close();
	        System.out.println("Done");
	}       
}
