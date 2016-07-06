package edu.dase.prl.stringMetric.benchMarks;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import edu.dase.prl.benchMark.records.SyntheticRecord;
import edu.dase.prl.benchMark.utils.CSVFileReader;
import edu.dase.prl.benchMark.utils.DatasetListNonPaddedQGrams;

public class TestMixErrorsDatasets {

	// threshold values for different string metrics. If given string 
	// metric returns score greater than the threshold, the strings
	// will be considered as "match"
	private static final double JARO_START_THRESHOLD = 0.3;
	private static final double LEVENSTAIN_START_THRESHOLD = 0.3;
	private static final double DICE_START_THRESHOLD = 0.3;
	private static final double JACCARD_START_THRESHOLD = 0.3;

	@Test(enabled=false)
	public static void testMain() throws Exception {

		// read-in datasets with character edits and their not corrupted versions
		CSVFileReader dataset_1000_1_1_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_1000_1_1_org);
		CSVFileReader dataset_1000_1_1_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_1000_1_1_cor);
		CSVFileReader dataset_1000_2_2_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_1000_2_2_org);
		CSVFileReader dataset_1000_2_2_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_1000_2_2_cor);
		CSVFileReader dataset_500_1_1_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_500_1_1_org);
		CSVFileReader dataset_500_1_1_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_500_1_1_cor);
		CSVFileReader dataset_500_2_2_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_500_2_2_org);
		CSVFileReader dataset_500_2_2_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_500_2_2_cor);
		CSVFileReader dataset_200_1_1_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_200_1_1_org);
		CSVFileReader dataset_200_1_1_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_200_1_1_cor);
		CSVFileReader dataset_200_2_2_org_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_200_2_2_org);
		CSVFileReader dataset_200_2_2_cor_reader = new CSVFileReader(null, DatasetListNonPaddedQGrams.SYNTH_MIX_ERR_EDITS_9999_200_2_2_cor);
		
		final List<SyntheticRecord> dataset_1000_1_1_org = dataset_1000_1_1_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_1000_1_1_cor = dataset_1000_1_1_cor_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_1000_2_2_org = dataset_1000_2_2_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_1000_2_2_cor = dataset_1000_2_2_cor_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_500_1_1_org = dataset_500_1_1_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_500_1_1_cor = dataset_500_1_1_cor_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_500_2_2_org = dataset_500_2_2_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_500_2_2_cor = dataset_500_2_2_cor_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_200_1_1_org = dataset_200_1_1_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_200_1_1_cor = dataset_200_1_1_cor_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_200_2_2_org = dataset_200_2_2_org_reader.loadEncryptedSyntheticRecords();
		final List<SyntheticRecord> dataset_200_2_2_cor = dataset_200_2_2_cor_reader.loadEncryptedSyntheticRecords();
		
		performLinkageBetweenDatasets(dataset_1000_1_1_org, "Mix-1000-1-1-org", dataset_1000_1_1_cor, "Mix-1000-1-1-cor", "MixErrors-1000-1-1");
		performLinkageBetweenDatasets(dataset_1000_2_2_org, "Mix-1000-2-2-org", dataset_1000_2_2_cor, "Mix-1000-2-2-cor", "MixErrors-1000-2-2");
		performLinkageBetweenDatasets(dataset_500_1_1_org, "Mix-500-1-1-org", dataset_500_1_1_cor, "Mix-500-1-1-cor", "MixErrors-500-1-1");
		performLinkageBetweenDatasets(dataset_500_2_2_org, "Mix-500-2-2-org", dataset_500_2_2_cor, "Mix-500-2-2-cor", "MixErrors-500-2-2");
		performLinkageBetweenDatasets(dataset_200_1_1_org, "Mix-200-1-1-org", dataset_200_1_1_cor, "Mix-200-1-1-cor", "MixErrors-200-1-1");
		performLinkageBetweenDatasets(dataset_200_2_2_org, "Mix-200-2-2-org", dataset_200_2_2_cor, "Mix-200-2-2-cor", "MixErrors-200-2-2");
	}
	
	public static void performLinkageBetweenDatasets(final List<SyntheticRecord> datasetOrg, final String datasetOrgName, 
			final List<SyntheticRecord> datasetCor, final String datasetCorName,  
			final String logFileNamePreFix) throws InterruptedException {
		
		Runnable runExactMatchUnencrypted = new Runnable() {
			@Override
			public void run() {
				long startTime = System.nanoTime();
				
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - exactMatchUnencrypted", "Exact matching on unencrypted names");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				compareRecordsUsingExactMatchUnencrypted(datasetOrg, datasetCor, datasetOrgName, datasetCorName, true, writer);
				
				long endTime = System.nanoTime();
				writer.println("Time taken to execute this test: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				
				writer.close();
			}
		};
		Runnable runExactMatchEncrypted = new Runnable() {
			@Override
			public void run() {
				long startTime = System.nanoTime();
				
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - exactMatchEncrypted", "Exact matching on encrypted names");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				compareRecordsUsingExactMatchEncrypted(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, writer);
				
				long endTime = System.nanoTime();
				writer.println("Time taken to execute this test: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				
				writer.close();
			}
		};
		Runnable runLevenstainScore = new Runnable() {
			@Override
			public void run() {
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - levenstain", "Matching based on Normalized Levenstain method");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (double threshold = LEVENSTAIN_START_THRESHOLD; threshold <= 1.0; threshold += 0.05) {
					long startTime = System.nanoTime();
					
					System.out.println("Beginning evaluating threshold value: " + threshold + " for Normalized Levenstain");
					compareRecordsUsingLevenstein(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, threshold, true, writer);
					
					long endTime = System.nanoTime();
					writer.println("Time taken to execute this test iteration: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				}
				writer.close();
			}
		};
		Runnable runJaroWinklerScore = new Runnable() {
			@Override
			public void run() {
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - JaroWinkler", "Matching based on JaroWinkler method");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (double threshold = JARO_START_THRESHOLD; threshold <= 1.0; threshold += 0.05) {
					long startTime = System.nanoTime();
					
					System.out.println("Beginning evaluating threshold value: " + threshold + " for Normalized JaroWinkler");
					compareRecordsUsingJaroWinkler(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, threshold, true, writer);
					
					long endTime = System.nanoTime();
					writer.println("Time taken to execute this test iteration: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				}
				writer.close();
			}
		};
		Runnable runExactMatchOnSoundex = new Runnable() {
			@Override
			public void run() {
				long startTime = System.nanoTime();
				
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - Exact match on Soundex", "Matching based on encrypted soundex score");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				compareRecordsUsingExactMatchOnEncryptedSoundex(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, writer);
				
				long endTime = System.nanoTime();
				writer.println("Time taken to execute this test: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				
				writer.close();
			}
		};
		Runnable runQGramJaccardScore = new Runnable() {
			@Override
			public void run() {
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - QGramJaccard", "Matching based on QGramJaccard method");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (double threshold = JACCARD_START_THRESHOLD; threshold <= 1.0; threshold += 0.05) {
					long startTime = System.nanoTime();
					
					System.out.println("Beginning evaluating threshold value: " + threshold + " for QGram (Jaccard)");
					compareRecordsUsingJaccardOnQGrams(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, threshold, true, writer);
					
					long endTime = System.nanoTime();
					writer.println("Time taken to execute this test iteration: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				}
				writer.close();
			}
		};
		Runnable runQGramDiceScore = new Runnable() {
			@Override
			public void run() {
				PrintWriter writer = null;
				try {
					writer = createLogFile(logFileNamePreFix + " - QGramDice", "Match based on QGramDice method");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (double threshold = DICE_START_THRESHOLD; threshold <= 1.0; threshold += 0.05) {
					long startTime = System.nanoTime();
					
					System.out.println("Beginning evaluating threshold value: " + threshold + " for QGram (Dice)");
					compareRecordsUsingDiceOnQGrams(datasetOrg, datasetCor,  datasetOrgName, datasetCorName, true, threshold, true, writer);
					
					long endTime = System.nanoTime();
					writer.println("Time taken to execute this test iteration: " + TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));
				}
				writer.close();
			}
		};
		
		Thread t1 = new Thread(runExactMatchUnencrypted);
		Thread t2 = new Thread(runExactMatchEncrypted);
		Thread t3 = new Thread(runLevenstainScore);
		Thread t4 = new Thread(runJaroWinklerScore);
		Thread t5 = new Thread(runExactMatchOnSoundex);
		Thread t6 = new Thread(runQGramJaccardScore);
		Thread t7 = new Thread(runQGramDiceScore);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		t6.join();
		t7.join();
	}

	public static void compareRecordsUsingExactMatchUnencrypted(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using exact match on unencrypted names ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using exact match on unencrypted names ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		List<String> bestMatches;
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				boolean isMatch = syntheticRecord1.exactMatchUnecrypted(syntheticRecord2);
				
				if (isMatch) {
					bestMatches.add(syntheticRecord2.getId());
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingExactMatchEncrypted(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using exact match on encrypted names ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using exact match on encrypted names ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				boolean isMatch = syntheticRecord1.exactMatchEncrypted(syntheticRecord2);
				
				if (isMatch) {
					bestMatches.add(syntheticRecord2.getId());
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingExactMatchOnEncryptedSoundex(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using exact match on encrypted soundex encodings ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using exact match on encrypted soundex encodings ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				boolean isMatch = syntheticRecord1.getEncryptedSoundexScore(syntheticRecord2);
				
				if (isMatch) {
					bestMatches.add(syntheticRecord2.getId());
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
				} else {
					falseNegative++;
				}
			} else {
				System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
				System.exit(-1321);
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingExactMatchOnUnencryptedSoundex(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean writeToFile, PrintWriter writer) {

		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using exact match on unencrypted soundex encodings ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using exact match on unencrypted soundex encodings ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				boolean isMatch = syntheticRecord1.getUnencryptedSoundexScore(syntheticRecord2);
				if (isMatch) {
					bestMatches.add(syntheticRecord2.getId());
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ".");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingJaroWinkler(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean countExtraFalseNegativesAsFalsePositives, 
			double THRESHOLD, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using Jaro Winkler ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using Jaro Winkler ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		double bestMatchScore;
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatchScore = -1.0;
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				double matchScore = syntheticRecord1.getJaroWinklerScore(syntheticRecord2);
				
				if (matchScore >= THRESHOLD) {
					if (matchScore > bestMatchScore) {
						bestMatchScore = matchScore;
						bestMatches.clear();
						bestMatches.add(syntheticRecord2.getId());
					}
					else if (matchScore == bestMatchScore) {
						bestMatches.add(syntheticRecord2.getId());
					}
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
					if (countExtraFalseNegativesAsFalsePositives && bestMatches.size() > 1) {
						falsePositive += bestMatches.size() - 1;
					}
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingLevenstein(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean countExtraFalseNegativesAsFalsePositives, 
			double THRESHOLD, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using Normalized Levenstein ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using Normalized Levenstein ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		double bestMatchScore;
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatchScore = -1.0;
			bestMatches = new ArrayList<String>();
			List<SyntheticRecord>bestMatchesRecords = new ArrayList<SyntheticRecord>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				double matchScore = syntheticRecord1.getNormalizedLevensteinScore(syntheticRecord2);
				
				if (matchScore >= THRESHOLD) {
					if (matchScore > bestMatchScore) {
						bestMatchScore = matchScore;
						bestMatches.clear();
						bestMatchesRecords.clear();
						bestMatches.add(syntheticRecord2.getId());
						bestMatchesRecords.add(syntheticRecord2);
					}
					else if (matchScore == bestMatchScore) {
						bestMatches.add(syntheticRecord2.getId());
						bestMatchesRecords.add(syntheticRecord2);
					}
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
					if (countExtraFalseNegativesAsFalsePositives && bestMatches.size() > 1) {
						falsePositive += bestMatches.size() - 1;
					}
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingJaccardOnQGrams(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean countExtraFalseNegativesAsFalsePositives, 
			double THRESHOLD, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using Jaccard comparison method on QGrams ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using Jaccard comparison method on QGrams ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		double bestMatchScore;
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatchScore = -1.0;
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				double matchScore = syntheticRecord1.getJaccardQGramScore(syntheticRecord2);
				
				if (matchScore >= THRESHOLD) {
					if (matchScore > bestMatchScore) {
						bestMatchScore = matchScore;
						bestMatches.clear();
						bestMatches.add(syntheticRecord2.getId());
					}
					else if (matchScore == bestMatchScore) {
						bestMatches.add(syntheticRecord2.getId());
					}
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
					if (countExtraFalseNegativesAsFalsePositives && bestMatches.size() > 1) {
						falsePositive += bestMatches.size() - 1;
					}
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static void compareRecordsUsingDiceOnQGrams(List<SyntheticRecord> dataset1, 
			List<SyntheticRecord> dataset2, String dataset1Name, String dataset2Name, boolean countExtraFalseNegativesAsFalsePositives, 
			double THRESHOLD, boolean writeToFile, PrintWriter writer) {
		
		if (writeToFile) {
			writer.println("------------Analyzing unencrypted datasets using Dice comparison method on QGrams ---------------");
			writer.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println("------------Analyzing unencrypted datasets using Dice comparison method on QGrams ---------------");
			System.out.println("Datasets: " + dataset1Name + " vs " + dataset2Name);
		}
		
		int correct = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		
		double bestMatchScore;
		List<String> bestMatches;
		
		for (SyntheticRecord syntheticRecord1 : dataset1) {
			bestMatchScore = -1.0;
			bestMatches = new ArrayList<String>();
			
			for (SyntheticRecord syntheticRecord2 : dataset2) {
				double matchScore = syntheticRecord1.getDiceQGramScore(syntheticRecord2);
				
				if (matchScore >= THRESHOLD) {
					if (matchScore > bestMatchScore) {
						bestMatchScore = matchScore;
						bestMatches.clear();
						bestMatches.add(syntheticRecord2.getId());
					}
					else if (matchScore == bestMatchScore) {
						bestMatches.add(syntheticRecord2.getId());
					}
				}
			}
			
			String correctID = syntheticRecord1.getId();
			
			if (correctID != null && !correctID.isEmpty()) {
				if (bestMatches.contains(correctID)) {
					correct++;
					if (countExtraFalseNegativesAsFalsePositives && bestMatches.size() > 1) {
						falsePositive += bestMatches.size() - 1;
					}
				} else {
					falseNegative++;
				}
			} else {
				if (writeToFile) {
					writer.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				} else {
					System.err.println("Couldn't retrive the id of record: " + syntheticRecord1);
					System.exit(-1321);
				}
			}
		}
		
		if (writeToFile) {
			writer.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			writer.println("\tCorrect: " + correct);
			writer.println("\tFalse positive: " + falsePositive);
			writer.println("\tFalse negative: " + falseNegative);
			
			writer.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		} else {
			System.out.println(dataset1Name + " -> " + dataset2Name + 
					", Number of records in " + dataset1Name + " dataset: " + dataset1.size() + ". Results for THRESHOLD VALUE of " + THRESHOLD + " :");
			
			System.out.println("\tCorrect: " + correct);
			System.out.println("\tFalse positive: " + falsePositive);
			System.out.println("\tFalse negative: " + falseNegative);
			
			System.out.println("----------------- Done analyzing " + 
					"datasets: " + dataset1Name + " vs " + dataset2Name);
		}
	}
	
	public static PrintWriter createLogFile(String logFileName, String methodDescription) throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(logFileName, "UTF-8");
		writer.write("METHOD: " + methodDescription + " \n\n\n");
		
		return writer;
	}
}
