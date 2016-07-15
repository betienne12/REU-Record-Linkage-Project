package edu.dase.prl.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Soundex;

import edu.dase.prl.encryption.DataEncryptor;


public class Driver {
	
	// Test parameters
	static String corruptionType = "keyboard"; // possibilities: character, keyboard, ocr, phonetic, all
	static String numofModifications="1"; //possibilities: 1, 2, or 3
	static String corruptionPercentage="2";  //possibilities: 2, 5, or 10
	static String compareOn = "first"; // possibilities are first or last (name)
	static String blockingMethod = "suffix"; // possibilities are soundex, metaphone, prefix, suffix, length, or none
	static int minBlockingParam = 1; // prefix/suffix size; ignored for soundex, length and none
	static int maxBlockingParam = 4;
	static int q = 2; // size of the q-grams
	static double threshold = 0.65;
	static Soundex soundex = new Soundex();
	static DoubleMetaphone doublemetaphone=new DoubleMetaphone();
	
	
	public static void main(String[] args) throws Exception {
		
		displayTestParameters();
		
		for (int blockingParam = minBlockingParam; blockingParam <= maxBlockingParam; blockingParam++) {
			
			double truePositives = 0;
			double falsePositives = 0;
			double falseNegatives = 0;
			int comparisons = 0;
	        ArrayList<Record> database = getRecords("data/original.csv", blockingParam);
			ArrayList<Record> queries = getRecords("data/" + corruptionType + ".csv", blockingParam);
			//ArrayList<Record> database = getRecords("data/test_data.csv", blockingParam);
			//ArrayList<Record> queries=getRecords("data/"+ corruptionType+ "/" + corruptionType + "_"+ numofModifications+ "_" + corruptionPercentage + ".csv", blockingParam);
			
			HashMap<String, ArrayList<Record>> blocks = createBlocks(database);
			for (Record query: queries) {
				
				ArrayList<String> qGramsQuery = getQGrams(query);
				ArrayList<Record> matches = new ArrayList<>();
								
				// Compare the query to all the records in the database from its block
				ArrayList<Record> block = blocks.get(getBlockingField(query));
				if (block == null) {
					falseNegatives++;
					continue;
				}
				
				for (Record b: block) {
					comparisons++;
					ArrayList<String> qGramsB = getQGrams(b);
					if (getDice(qGramsQuery, qGramsB) >= threshold) {
						matches.add(b);
					}
				}
				
				// Did we get the right answer?
				boolean found = false;
				for (Record match: matches) {
					if (match.id == (query.id)) {
						truePositives++;
						found = true;
					} else {
						falsePositives++;
					}
				}
				
				if (!found) {
					falseNegatives++;
				}
				
			}
			
			System.out.println(blockingParam + "," + comparisons + "," + truePositives + 
					"," + falsePositives + "," + falseNegatives);
			double precision = truePositives / (truePositives + falsePositives);
			double recall = truePositives / (truePositives + falseNegatives);
			System.out.println("precision: " + precision);
			System.out.println("recall: " + recall);
			System.out.println(" ");
		}
	}
	
	
	public static ArrayList<Record> getRecords(String filename, int blockingParam) throws Exception {
		
		ArrayList<Record> records = new ArrayList<>();
		
		int id = 0;
		Scanner in = new Scanner(new File(filename));
		while (in.hasNext()) {
			
			String line = in.nextLine();
			String[] tokens = line.split("[,]");
			
			Record r = new Record();
			r.id = ++id;
			r.firstName = tokens[0].trim();
			r.lastName = tokens[1].trim();
			r.encryptedFirstQGrams = stringToEncryptedQGrams(r.firstName);
			r.encryptedLastQGrams = stringToEncryptedQGrams(r.lastName);
			r.encryptedFirstSoundex = DataEncryptor.encryptString(soundex.encode(r.firstName), "password");
			r.encryptedLastSoundex = DataEncryptor.encryptString(soundex.encode(r.lastName), "password");
			r.encryptedFirstMetaphone=DataEncryptor.encryptString(doublemetaphone.encode(r.firstName), "password");
			r.encryptedLastMetaphone=DataEncryptor.encryptString(doublemetaphone.encode(r.lastName), "password");

			r.encryptedFirstPrefix = DataEncryptor.encryptString(
					r.firstName.substring(0, (int) Math.min(blockingParam, r.firstName.length())), "password");
			
			r.encryptedLastPrefix = DataEncryptor.encryptString(
					r.lastName.substring(0, (int) Math.min(blockingParam, r.lastName.length())), "password");
			
			r.encryptedFirstSuffix = DataEncryptor.encryptString(
					r.firstName.substring(Math.max(0, r.firstName.length()-blockingParam)), "password");
			
			r.encryptedLastSuffix = DataEncryptor.encryptString(
					r.lastName.substring(Math.max(0, r.lastName.length()-blockingParam)), "password");
			
			r.firstNumQgrams = r.encryptedFirstQGrams.size();
			r.lastNumQgrams = r.encryptedLastQGrams.size();

			records.add(r);
		}
		in.close();
		
		return records;
	}
	
	
	public static HashMap<String, ArrayList<Record>> createBlocks(ArrayList<Record> records) {
		HashMap<String, ArrayList<Record>> blocks = new HashMap<>();
		
		for (Record r: records) {
			String key = getBlockingField(r);
			if (!blocks.containsKey(key)) {
				blocks.put(key, new ArrayList<Record>());
			}
			blocks.get(key).add(r);
		}			
		return blocks;
	}
	
	
	public static String getBlockingField(Record r) {
		
		if (blockingMethod.equals("none")) {
			return "*";
			
		} else if (blockingMethod.equals("soundex")) {
			
			if (compareOn.equals("first")) {
				return r.encryptedFirstSoundex;
			} else {
				return r.encryptedLastSoundex;
			}
		} else if (blockingMethod.equals("metaphone")) {
			
			if (compareOn.equals("first")) {
				return r.encryptedFirstMetaphone;
			} else {
				return r.encryptedLastMetaphone;
			}
			
		} else if (blockingMethod.equals("prefix")) {
			
			if (compareOn.equals("first")) {
				return r.encryptedFirstPrefix;
			} else {
				return r.encryptedLastPrefix;
			}
		
		} else if (blockingMethod.equals("suffix")) {
			
			if (compareOn.equals("first")) {
				return r.encryptedFirstSuffix;
			} else {
				return r.encryptedLastSuffix;
			}
		
		} else {
			
			if (compareOn.equals("first")) {
				return "" + r.firstNumQgrams;
			} else {
				return "" + r.lastNumQgrams;
			}
		}
	}
	
	
	public static ArrayList<String> getQGrams(Record r) {
		
		ArrayList<String> qGrams = new ArrayList<>();
		
		if (compareOn.equals("first")) {
			qGrams.addAll(r.encryptedFirstQGrams);		
		} else {
			qGrams.addAll(r.encryptedLastQGrams);
		}
		
		return qGrams;
	}
	
	
	public static double getDice(ArrayList<String> set1, ArrayList<String> set2) {
		
		int intersection = 0;
		for (String s: set1) {
			if (set2.contains(s)) {
				intersection++;
			}
		}
		
		return (2.0 * intersection) / (set1.size() + set2.size());
	}
	
	
	public static void displayTestParameters() {
		
		corruptionType = corruptionType.toLowerCase();
		compareOn = compareOn.toLowerCase();
		blockingMethod = blockingMethod.toLowerCase();
		
		if (!(corruptionType.equals("character") || corruptionType.equals("keyboard")||corruptionType.equals("character_edits") || corruptionType.equals("ocr") 
				|| corruptionType.equals("phonetic")|| corruptionType.equals("keyboard_edits")|| corruptionType.equals("ocr_edits")
				|| corruptionType.equals("set_missing") || corruptionType.equals("phonetic_edits")  || corruptionType.equals("all"))) {
			System.err.println("Invalid corruption type, must be character, keyboard, ocr, phonetic, or all");
			System.exit(0);
		}
		
		if (!(compareOn.equals("first") || compareOn.equals("last"))) {
			System.err.println("Invalid compareOn type, must be first or last");
			System.exit(0);
		}

		if (!(blockingMethod.equals("soundex")|| blockingMethod.equals("metaphone") || blockingMethod.equals("prefix") || blockingMethod.equals("suffix") 
				|| blockingMethod.equals("length") || blockingMethod.equals("none"))) {
			System.err.println("Invalid blocking method, must be soundex, prefix, suffix, length, or none");
			System.exit(0);
		}
		
		// Soundex, length, and none have no parameters
		if (blockingMethod.equals("soundex") || blockingMethod.equals("length") || blockingMethod.equals("none")) {
			minBlockingParam = 1;
			maxBlockingParam = 1;
		}
		if (blockingMethod.equals("metaphone") || blockingMethod.equals("length") || blockingMethod.equals("none")) {
			minBlockingParam = 1;
			maxBlockingParam = 1;
		}

		System.out.println("corruptionType = " + corruptionType); 
		System.out.println("compareOn = " + compareOn); 
		System.out.println("blockingMethod = " + blockingMethod); 
		System.out.println("minBlockingParam = " + minBlockingParam);
		System.out.println("maxBlockingParam = " + maxBlockingParam);
		System.out.println("q = " + q); 
		System.out.println("threshold = " + threshold);
		System.out.println("\nblocking parameter, comparisons, true positives, false positives, false negatives");
	}

	
	public static ArrayList<String> stringToEncryptedQGrams(String s) throws Exception {
		ArrayList<String> encryptedQGrams = new ArrayList<>();
		
		// we're using padded q-grams, so pad the beginning and end of the string with 
		// q-1 special characters
		for (int i=0; i<q-1; i++) {
			s = "%" + s + "#";
		}
		
		ArrayList<String> qGrams = new ArrayList<>();
		for (int i=0; i+q<=s.length(); i++) {
			qGrams.add(s.substring(i, i+q));
		}
		
		for (String qGram: qGrams) {
			encryptedQGrams.add(DataEncryptor.encryptString(qGram, "password"));
		}
		
		return encryptedQGrams;
	}
}
