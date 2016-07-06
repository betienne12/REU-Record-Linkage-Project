package edu.dase.prl.stringComparsion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.language.Soundex;

//import org.apache.commons.codec.language.Soundex;
//import org.apache.commons.codec.language.

import edu.dase.prl.benchMark.records.BetterRecord;
import edu.dase.prl.encryption.DataEncryptor;

public class QgramEvaluator {

	public static void main(String[] args) throws Exception {
		// Analyze the Qgram performance on encrypted last names
				int qNumber=2;
				
				
				// Read in all of the records
				ArrayList<BetterRecord> records = new ArrayList<>();
				
				Scanner in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Eclipse Files/new_encrypt_data.csv"));
				//Scanner in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Pawels_encryptednew_data.csv"));

				while (in.hasNext()) {
					records.add(new BetterRecord(in.nextLine()));
				}
			
				in.close();
				
				HashMap<String, ArrayList<BetterRecord>> blockingTable = new HashMap<>();
				for (BetterRecord r: records) {
					String QGramVal = Arrays.toString(QGram.convertToQgram(r.lastName, qNumber));
					if (blockingTable.containsKey(QGramVal)) {
						blockingTable.get(QGramVal).add(r);
					} else {
						ArrayList<BetterRecord> temp = new ArrayList<>();
						temp.add(r);
						blockingTable.put(QGramVal, temp);
					}
				}	
				
				
				// For each of the corrupted records, query the records "database" (the arraylist)
				in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Fixed Corrupted Data/Character Edits/Character_Edits_3.csv"));
				//in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Fixed Corrupted Data/Set Missing/Set_Missing_1.csv"));
				//in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/9999_1000_1_1_cor_syntheticrecords.csv"));

				in.nextLine(); // throws out column headers
				int i = -1;
				
				double tp = 0;
				double fp = 0;
				double fn = 0;
				
				while (in.hasNext()) {
					String line = in.nextLine();
					i++; // index of the correct record
					String[] tokens = line.split(",");
					String lastName = tokens[2];
					double percentage=Double.parseDouble(tokens[11]);
					
					// compute the encrypted Qgram
					String[] qGramsLast = QGram.convertToQgram(lastName, qNumber);
		        	String[] encryptedqGramLast = DataEncryptor.encryptStringArray(qGramsLast, "password");
		        	 
		        	
					// use that to compare to each record
					ArrayList<Integer> matchingRecordIndices = new ArrayList<>();
					int j = 0;
					//for (BetterRecord thisRecord: blockingTable.get(Arrays.toString(QGram.convertToQgram(lastName, qNumber)))) {
						//set counter here to check how may comparisons are made using blocking vs non blocking method
			
					for (BetterRecord thisRecord: records) {
						
						String cell =thisRecord.encryptedQGramsLast;
						String strArray[] = cell.split("  ");
						int intersection=0;
						
						int x=strArray.length;
						Integer y=0;
						if (encryptedqGramLast!=null){
							y=encryptedqGramLast.length;
						}
						else{
							y=0;
						}
						//if ((percentage>0.05)&&(percentage<=0.1)){
						//if ((percentage>0.02)&&(percentage<=0.05)){
						if ((percentage>=0)&&(percentage<=0.02)){
							for(int data = 0; data < strArray.length; data++)
							{
								for(int query = 0; query < encryptedqGramLast.length; query++)
								{
									if(strArray[data].equals(encryptedqGramLast[query]))
									{
										intersection=intersection+1;
									}
								}
							}
							double dice_sim=(2.0*intersection)/(x+y);
							if(dice_sim>0.5){
								matchingRecordIndices.add(j);
							}
						}
						else{
							if(thisRecord.encryptedQGramsLast.equals(thisRecord.encryptedQGramsLast)){
								matchingRecordIndices.add(j);

							}
						}
						j++;
					}
			

					// At this point, we have our guesses for matching records. Now, how right were we?
					for (int match: matchingRecordIndices) {
						if (i == match) {
							tp++;
						} else {
							fp++;
						}
					}
					
					if (!matchingRecordIndices.contains(i)) {
						fn++;
					}
				}
		
				in.close();
				
				double precision = tp / (tp + fp);
				double recall = tp / (tp + fn);
				System.out.println("precision: " + precision);
				System.out.println("recall: " + recall);
				System.out.println("tp:" + tp);
				System.out.println("fp:" + fp);
				System.out.println("fn:" + fn);

	}

}
