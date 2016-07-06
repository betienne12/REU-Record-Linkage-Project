package edu.dase.prl.qGramFrequencyAnalysis.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.dase.prl.benchMark.records.SyntheticRecord;
import edu.dase.prl.benchMark.utils.CSVFileException;
import edu.dase.prl.benchMark.utils.CSVFileReader;
import edu.dase.prl.benchMark.utils.CSVFileWriter;
import edu.dase.prl.encryption.DataEncryptor;
import edu.dase.prl.encryption.DataEncryptorException;
import edu.dase.prl.qGramFrequencyAnalysis.utils.GuessedDictionary.CorrectlyDecryptedQGram;
import edu.dase.prl.stringComparsion.QGram;
import edu.dase.prl.stringComparsion.StringComparsionException;

public class QGramDictionary {

	private int qNumber;
	private boolean encryptNames;
	
	private ArrayList<Map.Entry<String, Integer>> sortedQGramFrequencies;
	private Map<String, Integer> qGramsFrequencies;
	private Set<String> qGrams;
	
	private List<List<String>> listOfNames;
	
	public enum SyntheticRecordQGrams {
		FIRST_NAMES,
		LAST_NAMES,
		FIRST_AND_LAST_NAMES
	}
	
	public QGramDictionary(int qNumber, boolean encryptDictValues) {
		
		this.qNumber = qNumber;
		this.encryptNames = encryptDictValues;
	}
	
	public int getIndexOfQGram(String qGram) {
		
		for (int i = 0; i < sortedQGramFrequencies.size(); i++) {
			if (sortedQGramFrequencies.get(i).getKey().equals(qGram)) {
				return i + 1;
			}
		}
		
		return -1;
	}
	
	public int getQNumber() {
		
		return this.qNumber;
	}
	
	public int getDictionrySize() {
		
		if  (sortedQGramFrequencies == null || sortedQGramFrequencies.isEmpty()) {
			return 0;
		}
		
		return sortedQGramFrequencies.size();
	}
	
	public void addEntry(Map.Entry<String, Integer> entry) {
		
		if (sortedQGramFrequencies == null || sortedQGramFrequencies.isEmpty()) {
			sortedQGramFrequencies = new ArrayList<Map.Entry<String, Integer>>();
			qGrams = new HashSet<String>();
		}
		
		sortedQGramFrequencies.add(entry);
		qGrams.add(entry.getKey());
		sortedQGramFrequencies.sort(new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
	}
	
	public void toCSVFile(String fileName, String[] header, boolean includeTranslations)
			throws Exception {
	
		if (sortedQGramFrequencies == null || sortedQGramFrequencies.isEmpty()) {
			return;
		}
		
		CSVFileWriter csvWriter = new CSVFileWriter(header, fileName);
		csvWriter.saveQGramFrequencies(sortedQGramFrequencies, includeTranslations);
	}
	
	public void initFromWordCollection(Collection<String> wordList)
			throws Exception {

		// get q-grams and their count in the wordList
		Map<String, Integer> qGramCount = generateQGramCountFromWordsCollection(wordList);
		
		// sort the map according to the q-gram occurrence
		sortedQGramFrequencies = sortQGramCount(qGramCount);
		qGramsFrequencies = qGramCount;
		
		// save the list of qgrams
		qGrams = new HashSet<>(qGramCount.keySet());
	}
	
	public void initFromQGramCollection(Collection<String> qGramList) throws Exception {
		
		// get q-grams frequencies
		Map<String, Integer> qGramCount = generateQGramCountFromQGramCollection(qGramList);
		
		// sort the map according to the q-gram occurrence
		sortedQGramFrequencies = sortQGramCount(qGramCount);
		qGramsFrequencies = qGramCount;
		
		// save the list of qgrams
		qGrams = new HashSet<>(qGramCount.keySet());
	}
	
	public void initFromFile(String fileName, String[] header) throws Exception {
		
		CSVFileReader csvReader = new CSVFileReader(header, fileName);
		
		initFromWordCollection(csvReader.loadListOfNamesForFrequencyAttack());
	}
	
	public ArrayList<Map.Entry<List<String>, Double>> initFromSyntheticRecordListGenerateVulnerabilityList(
			String fileName, String[] header, SyntheticRecordQGrams qGramsType, boolean recordsEncrypted)
					throws Exception {
	
		// load records
		CSVFileReader csvReder = new CSVFileReader(header, fileName);
		List<SyntheticRecord> records;
		if (recordsEncrypted) {
			records = csvReder.loadEncryptedSyntheticRecords();
		} else {
			records = csvReder.loadUnencryptedSyntheticRecords();
		}
		
		// prepare the list of qGrams
		List<String> qGramList = new ArrayList<String>();
		List<String> namesList = new ArrayList<String>();
		// and the list of words made by those qGrams
		List<List<String>> listOfWords = new ArrayList<List<String>>();
		
		// populate the qGrams list and the list of words
		switch(qGramsType) {
		case FIRST_AND_LAST_NAMES:
			System.err.println("initFromSyntheticRecordListGenerateVulnerabilityList() not implemented yet for generating the vulnerability list");
			System.exit(-1234);
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsFirstName());
					qGramList.addAll(record.getQGramsLastName());
				} else {
					namesList.add(record.getFirstName());
					namesList.add(record.getLastName());
				}
			}
			break;
		case FIRST_NAMES:
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsFirstName());
					listOfWords.add(record.getQGramsFirstName());
				} else {
					namesList.add(record.getFirstName());
					listOfWords.add(Arrays.asList(DataEncryptor.encryptStringArray(QGram.convertToQgram(record.getFirstName(), qNumber), "password")));
				}
			}
			break;
		case LAST_NAMES:
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsLastName());
					listOfWords.add(record.getQGramsLastName());
				} else {
					namesList.add(record.getLastName());
					listOfWords.add(Arrays.asList(DataEncryptor.encryptStringArray(QGram.convertToQgram(record.getLastName(), qNumber), "password")));
				}
			}
			break;
		default:
			break;
		}
		
		if (recordsEncrypted) {
			initFromQGramCollection(qGramList);
		} else {
			initFromWordCollection(namesList);
		}
		
		listOfNames = listOfWords;
		
		// we have the frequency list by now, let's go through the list of words and assign a score to each of the words
		ArrayList<Map.Entry<List<String>, Double>> volnList = new ArrayList<Map.Entry<List<String>, Double>>();
		int totalWeightOfAllQGrams = 0;
		for (Map.Entry<String, Integer> entry : sortedQGramFrequencies) {
			totalWeightOfAllQGrams += entry.getValue();
		}
		populatingTheList:
		for (List<String> word : listOfWords) {
			// make sure not to insert duplicate entries
			for (Map.Entry<List<String>, Double> entry : volnList) {
				if (word.size() == entry.getKey().size()) {
					if (word.equals(entry.getKey())) {//(word.containsAll(entry.getKey())) {
						continue populatingTheList;
					}
				}
			}
			int wordScore = 0;
			for (String qGram : word) {
				wordScore += qGramsFrequencies.get(qGram);
			}
			volnList.add(new AbstractMap.SimpleEntry<List<String>, Double>(word, (double)wordScore/totalWeightOfAllQGrams));
		}
		// sort it in a descending order
		Collections.sort(volnList, new Comparator<Map.Entry<List<String>, Double>>() {

			@Override
			public int compare(Map.Entry<List<String>, Double> o1, Map.Entry<List<String>, Double> o2) {
				
				return o2.getValue().compareTo(o1.getValue());
			}
		});	
		
		return volnList;
	}
	
	public void initFromSyntheticRecordList(String fileName, String[] header, SyntheticRecordQGrams qGramsType,
			boolean recordsEncrypted)
					throws Exception {

		CSVFileReader csvReder = new CSVFileReader(header, fileName);
		List<SyntheticRecord> records;
		if (recordsEncrypted) {
			records = csvReder.loadEncryptedSyntheticRecords();
		} else {
			records = csvReder.loadUnencryptedSyntheticRecords();
		}
		
		List<String> qGramList = new ArrayList<String>();
		List<String> namesList = new ArrayList<String>();
		
		switch(qGramsType) {
		case FIRST_AND_LAST_NAMES:
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsFirstName());
					qGramList.addAll(record.getQGramsLastName());
				} else {
					namesList.add(record.getFirstName());
					namesList.add(record.getLastName());
				}
			}
			break;
		case FIRST_NAMES:
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsFirstName());
				} else {
					namesList.add(record.getFirstName());
				}
			}
			break;
		case LAST_NAMES:
			for (SyntheticRecord record : records) {
				if (recordsEncrypted) {
					qGramList.addAll(record.getQGramsLastName());
				} else {
					namesList.add(record.getLastName());
				}
			}
			break;
		default:
			break;
		}
		
		if (recordsEncrypted) {
			initFromQGramCollection(qGramList);
		} else {
			initFromWordCollection(namesList);
		}
	}
	
	public String getNthMostCommmonQGram(int index) {
		
		if (sortedQGramFrequencies == null || index > sortedQGramFrequencies.size()) {
			return null;
		}
		return sortedQGramFrequencies.get(index - 1).getKey();
	}
	
	public Map.Entry<String, Integer> getNthMostCommonDictionaryEntry(int index) {
		
		if (sortedQGramFrequencies == null || index > sortedQGramFrequencies.size()) {
			return null;
		}
		return sortedQGramFrequencies.get(index - 1);
	}
	
	public void checkOverlapBetweenDictionaries(QGramDictionary otherDictionary) {
		
		if (this.qGrams == null || otherDictionary.qGrams == null) {
			System.out.println("One of the dictionaries has not been initialized yet.");
		}
		
		Set<String> tmp = new HashSet<String>(this.qGrams);
		tmp.retainAll(otherDictionary.qGrams);
		
		System.out.println("Out of " + this.qGrams.size() + " qGrams, the other "
				+ "dictionary contains " + tmp.size() + " qGrams.");
	}
	
	public void analyzeQualityOfGuessedDictionary(Map<String, CorrectlyDecryptedQGram> decryptedQGramsDict)
			throws Exception {
	
		// for stats
		int numberOfNamesAttemptedToDecrypt = 0;
		
		int numberOFUniqueNames = 0;
		Set<String> setOfUnqiueNames = new HashSet<String>();
		
		int numberOfFullyDecryptedNames = 0;
		Set<String> setOfUniqueDecryptedNames = new HashSet<String>();
		
		int numberOfUniqueNamesNotDecryptedAtAll = 0;
		Set<String> setOfUniqueNotDecryptedAttAllNames = new HashSet<String>();
		
		int numberOfPartiallyDecryptedNames = 0;
		Set<String> setOfUniquePartiallyDecryptedNames = new HashSet<String>();
		Map<Double, Integer> partiallyDecryptedNamesDistribution = new HashMap<Double, Integer>();
		partiallyDecryptedNamesDistribution.put(0.25, 0);
		partiallyDecryptedNamesDistribution.put(0.50, 0);
		partiallyDecryptedNamesDistribution.put(0.75, 0);
		partiallyDecryptedNamesDistribution.put(1.0, 0);
		
		double avgPrecentageOfDecryption = 0.0;
		
		for (List<String> encryptedName : listOfNames) {
			int qGramCount = encryptedName.size();
			
			numberOfNamesAttemptedToDecrypt += 1;
			if (!setOfUnqiueNames.contains(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")))) {
				numberOFUniqueNames++;
				setOfUnqiueNames.add(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")));
			}
			
			int decryptedQGramCount = 0;
			List<String> decryptedName = new ArrayList<String>();
			for (String encryptedQGram : encryptedName) {
				if (decryptedQGramsDict.containsKey(encryptedQGram)) {
					decryptedName.add(decryptedQGramsDict.get(encryptedQGram).getCorrectDecryption());
					decryptedQGramCount++;
				} else {
					decryptedName.add(encryptedQGram);
				}
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("Name \"");
			sb.append(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")));
			sb.append("\" was ");
			if (qGramCount == decryptedQGramCount) {
				// name was decrypted successfully
				sb.append("decrypted successfully to ");
				sb.append(QGram.convertFromQGramArray(decryptedName.toArray(new String[decryptedName.size()])));
				sb.append("\n");
				
				if (!setOfUniqueDecryptedNames.contains(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")))) {
					setOfUniqueDecryptedNames.add(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")));
					numberOfFullyDecryptedNames += 1;
					avgPrecentageOfDecryption += 100;
				}
			} else {
				// name wasn't decrypted or it was partially decrypted
				sb.append("not fully decrypted. Decrypted names looks as follows: \n");
				sb.append("\t" + decryptedName.toString() + "\n");
				sb.append("and it was decrypted in " + (int)Math.ceil((decryptedQGramCount/(double)qGramCount) * 100) + "%");
				
				if (decryptedQGramCount == 0 && !setOfUniqueNotDecryptedAttAllNames.contains(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")))) {
					numberOfUniqueNamesNotDecryptedAtAll += 1;
					avgPrecentageOfDecryption += (int)Math.ceil((decryptedQGramCount/(double)qGramCount) * 100);
					setOfUniqueNotDecryptedAttAllNames.add(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")));
				}
				if (decryptedQGramCount != 0 && !setOfUniquePartiallyDecryptedNames.contains(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")))) {
					numberOfPartiallyDecryptedNames += 1;
					avgPrecentageOfDecryption += (int)Math.ceil((decryptedQGramCount/(double)qGramCount) * 100);
					setOfUniquePartiallyDecryptedNames.add(QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[]) encryptedName.toArray(), "password")));
					
					int precentageOfDecryption = (int)Math.ceil((decryptedQGramCount/(double)qGramCount) * 100);
					if (precentageOfDecryption <= 100 && precentageOfDecryption >= 75) {
						partiallyDecryptedNamesDistribution.put(1.0, new Integer(partiallyDecryptedNamesDistribution.get(1.0) + 1));
					} else if (precentageOfDecryption < 75 && precentageOfDecryption >= 50) {
						partiallyDecryptedNamesDistribution.put(0.75, new Integer(partiallyDecryptedNamesDistribution.get(0.75) + 1));
					} else if (precentageOfDecryption < 50 && precentageOfDecryption >= 25) {
						partiallyDecryptedNamesDistribution.put(0.5, new Integer(partiallyDecryptedNamesDistribution.get(0.5) + 1));
					} else if (precentageOfDecryption < 25 && precentageOfDecryption >= 0) {
						partiallyDecryptedNamesDistribution.put(0.25, new Integer(partiallyDecryptedNamesDistribution.get(0.25) + 1));
					}
				}
			}
			
//			System.out.println(sb.toString());
		}
		
		System.out.println("Some stats:");
		System.out.println("\tNumber of names to decrypt: " + numberOfNamesAttemptedToDecrypt + " (number of unique names: " + numberOFUniqueNames + ")");
		System.out.println("\tNumber of fully decrypted names: " + numberOfFullyDecryptedNames + " (that is " + (int)Math.ceil((numberOfFullyDecryptedNames/(double)numberOFUniqueNames) * 100) + "% of all names)");
		System.out.println("\tNumber of partially decrypted names: " + numberOfPartiallyDecryptedNames + " (that is " + (int)Math.ceil((numberOfPartiallyDecryptedNames/(double)numberOFUniqueNames) * 100) + "% of all names)");
		System.out.println("\t\tDistribution of partially decrypted names:");
		System.out.println("\t\t\tPartial decryption between 100% and 75%: " + (int)Math.ceil((partiallyDecryptedNamesDistribution.get(1.0)/(double)numberOFUniqueNames) * 100) + "%");
		System.out.println("\t\t\tPartial decryption between 75% and 50%: " + (int)Math.ceil((partiallyDecryptedNamesDistribution.get(0.75)/(double)numberOFUniqueNames) * 100) + "%");
		System.out.println("\t\t\tPartial decryption between 50% and 25%: " + (int)Math.ceil((partiallyDecryptedNamesDistribution.get(0.5)/(double)numberOFUniqueNames) * 100) + "%");
		System.out.println("\t\t\tPartial decryption between 25% and 0%: " + (int)Math.ceil((partiallyDecryptedNamesDistribution.get(0.25)/(double)numberOFUniqueNames) * 100) + "%");
		System.out.println("\tNumber of names not decrypted at all: " + numberOfUniqueNamesNotDecryptedAtAll + " (that is " + (int)Math.ceil((numberOfUniqueNamesNotDecryptedAtAll/(double)numberOFUniqueNames) * 100) + "% of all names)") ;
		System.out.println("\tAverage % of decryption: " + (int)Math.ceil((avgPrecentageOfDecryption/numberOFUniqueNames)) + "%");
		System.out.println("Some more stats about q-grams:");
		System.out.println("\tThe total number of all encrypted q-grams was: " + this.qGrams.size());
		System.out.println("\tThe total number of all decrypted q-grams was: " + decryptedQGramsDict.size() + " (that is " + (int)Math.ceil(decryptedQGramsDict.size()/(double)this.qGrams.size() * 100) + " % all q-grams)");
		
	}
	
	/**
	 * returns sorted array list (most to least occurring q-gram)
	 */
	private static <K, V extends Comparable<? super V>> ArrayList<Map.Entry<K, V>>	sortQGramCount(Map<K, V> mapToSort) {
		
		ArrayList<Map.Entry<K, V>> sortedList = new ArrayList<Map.Entry<K,V>>(mapToSort.entrySet());
		
		Collections.sort(sortedList, new Comparator<Map.Entry<K, V>>() {

			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		return sortedList;
	}
	
	
	
	private Map<String, Integer> generateQGramCountFromQGramCollection(Collection<String> qGramList) throws Exception {
		Map<String, Integer> qGramCount = new HashMap<String, Integer>();
		
		for (String qGram: qGramList) {
			if (encryptNames) {
				qGram = DataEncryptor.encryptString(qGram, "password"); 
			} else {
				qGram = qGram.toLowerCase();
			}
			
			Integer oldCount = qGramCount.get(qGram);
			qGramCount.put(qGram, oldCount == null ? 1 : oldCount + 1);
		}
		
		return qGramCount;
	}
	
	private Map<String, Integer> generateQGramCountFromWordsCollection(Collection<String> wordList) throws Exception {
		Map<String, Integer> qGramCount = new HashMap<String, Integer>();
		
		for (String word : wordList) {
			String[] qGrams = null;
			try {
				qGrams = QGram.convertToQgram(word, qNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (String qGram : qGrams) {
				if (encryptNames) {
					qGram = DataEncryptor.encryptString(qGram, "password");
				}  else {
					qGram = qGram.toLowerCase();
				}
				
				Integer oldCount = qGramCount.get(qGram);
				qGramCount.put(qGram, oldCount == null ? 1 : oldCount + 1);
			}
		}
		
		return qGramCount;
	}
	
	@Override
	public String toString() {
		
		if (sortedQGramFrequencies == null) {
			return "null";
		}
		
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Integer> entry : sortedQGramFrequencies) {
			builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
		}
		
		return builder.toString();
	}
}
