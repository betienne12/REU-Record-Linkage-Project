package edu.dase.prl.stringComparsion;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.language.Soundex;

import edu.dase.prl.benchMark.records.BetterRecord;
import edu.dase.prl.encryption.DataEncryptor;

public class SoundexEvaluator {

	public static void main(String[] args) throws Exception {

		// Analyze the Soundex performance on encrypted last names
		Soundex soundex = new Soundex();

		// Read in all of the records
		ArrayList<BetterRecord> records = new ArrayList<>();

		Scanner in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Eclipse Files/new_encrypt_data.csv"));
		//Scanner in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Pawels_encryptednew_data.csv"));
		while (in.hasNext()) {
			records.add(new BetterRecord(in.nextLine()));
		}
		in.close();

		// Creating the blocking index -- this one is based on Soundex value of the last name
		HashMap<String, ArrayList<BetterRecord>> blockingTable = new HashMap<>();
		for (BetterRecord r: records) {
			String soundexVal = soundex.encode(r.lastName);
			if (blockingTable.containsKey(soundexVal)) {
				blockingTable.get(soundexVal).add(r);
			} else {
				ArrayList<BetterRecord> temp = new ArrayList<>();
				temp.add(r);
				blockingTable.put(soundexVal, temp);
			}
		}

		// For each of the corrupted records, query the records "database" (the arraylist)
		in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Fixed Corrupted Data/Character Edits/Character_Edits_3.csv"));
		//in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/Fixed Corrupted Data/Set Missing/Set_Missing_3.csv"));

		//in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/9999_1000_1_1_cor_syntheticrecords.csv"));
		/*for 100% accuracy test*/ //in = new Scanner(new File("/C:/Users/Brian/Desktop/REU/new_data.csv"));

		in.nextLine(); // throws out column headers
		int i = -1;
		int index=0;
		double tp = 0;
		double fp = 0;
		double fn = 0;

		while (in.hasNext()) {
			String line = in.nextLine();
			i++; // index of the correct record
			String[] tokens = line.split(",");
			String lastName = tokens[2];
			double percentage=Double.parseDouble(tokens[11]);
			/*for 100% accuracy test*/ //String lastName = tokens[1];

			if ((percentage>0.05)&&(percentage<=0.1)){
				index=index+1;
			}


			// compute the encrypted soundex
			String soundexQuery = DataEncryptor.encryptString(soundex.encode(lastName), "password");
			//System.out.println(soundexQuery);

			// use that to compare to each record
			ArrayList<Integer> matchingRecordIndices = new ArrayList<>();


			int j = 0;
			System.out.println(lastName + ": " + soundex.encode(lastName));
			ArrayList<BetterRecord> possibleMatches = blockingTable.get(soundex.encode(lastName));
			if (possibleMatches != null) {
				for (BetterRecord thisRecord: possibleMatches) {
					//for (BetterRecord thisRecord: records) {
					//if ((percentage>0.05)&&(percentage<=0.1)){
					//if ((percentage>0.02)&&(percentage<=0.05)){
					if ((percentage>=0)&&(percentage<=0.02)){
						if (thisRecord.encryptedSoundexLast.equals(soundexQuery)) {
							matchingRecordIndices.add(j);
						}

					}
					else{
						if(thisRecord.encryptedSoundexLast.equals(thisRecord.encryptedSoundexLast)){
							matchingRecordIndices.add(j);
						}
					}
					j++;
				}

				// At this point, we have our guesses for matching records. Now, how right were we?
				for (int match: matchingRecordIndices) {
					if (i == match) {
						tp++;
					} else{
						fp++;
					}
				}

				if (!matchingRecordIndices.contains(i)) {
					fn++;
				}
			} else {
				fn++;
			}

			//System.exit(0);
		}
		in.close();

		double precision = tp / (tp + fp);
		double recall = tp / (tp + fn);
		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("tp:" +tp);
		System.out.println("fp:" +fp);
		System.out.println("fn:" +fn);
		System.out.println("index: "+ index);



	}
}
