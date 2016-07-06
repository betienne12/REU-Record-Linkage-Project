package edu.dase.prl.qGramFrequencyAnalysis.driver;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.dase.prl.encryption.DataEncryptorException;
import edu.dase.prl.qGramFrequencyAnalysis.utils.GuessedDictionary;
import edu.dase.prl.qGramFrequencyAnalysis.utils.GuessedDictionary.CorrectlyDecryptedQGram;
import edu.dase.prl.qGramFrequencyAnalysis.utils.QGramDictionary;
import edu.dase.prl.stringComparsion.StringComparsionException;

public class DictionaryAttack {

	private QGramDictionary hijackedDict;
	private QGramDictionary guessedDict;
	
	private List<String> nameList;
	
	private ArrayList<Map.Entry<List<String>,Double>> volnurabilityList;
	
	private int permListRange;
	
	private GuessedDictionary adjustedGuessedDictionaryOneMatchOnly;
	
	public DictionaryAttack(QGramDictionary hijackedDict, QGramDictionary guessedDict, 
			List<String> nameList, ArrayList<Map.Entry<List<String>, Double>> volnurabilityList,
			int permListRange) {
		
		this.hijackedDict = hijackedDict;
		this.guessedDict = guessedDict;
		
		List<String> myNameList = new ArrayList<>(nameList.size());
		for (String name : nameList) {
			myNameList.add(name.toLowerCase());
		}
		this.nameList = myNameList;
		
		this.volnurabilityList = volnurabilityList;
		
		this.permListRange = permListRange;
		
		this.adjustedGuessedDictionaryOneMatchOnly = new GuessedDictionary();
	}
	
	public void performGuessing() throws Exception {
		
		// for each word in the vulnerability list
		int tmpCounter = 0;
		for (Map.Entry<List<String>, Double> volnWordEntry : volnurabilityList) {
			
			// create a tmp copy of list of all possible names:
			// trim the list so that only the names with adequate length are left
			List<String> nameListCpy = getSubDictOfLengthN(volnWordEntry.getKey().size() + (hijackedDict.getQNumber() - 1), this.nameList);
			
			// keep track of number of iterations of the for-loop below
			int iteration = 1;
			// trim the list of possible names based on the possible permutations
			List<List<StringElement>> listOfPerms = createListOfPossiblePermutations(volnWordEntry.getKey());
			
			if (listOfPerms == null) {
				//System.out.println("Skipping guessing for the name due to the fact it uses qGrams with frequency index so low that it doesn't have it's corresponding entry in the guessed dictionary");
				continue;
			}
			
			int charsToCheck = hijackedDict.getQNumber();
			for (List<StringElement> perms : listOfPerms) {
				List<String> nameListCpy2 = new ArrayList<String>(nameListCpy);
				for (String name : nameListCpy) {
					StringElement mustMatch;
					// two chars in the name at appropriate position must much to at least one of the elements of possible permutations for this positions
					mustMatch = new StringElement(name.substring(iteration - 1, (iteration - 1) + charsToCheck), 0);
					if (!mustMatch.isPartOf(perms)) {
						nameListCpy2.remove(name);
					}
				}
				iteration++;
				nameListCpy = nameListCpy2;
			}
			
			// assign a score to each "guessed" name
			List<StringElement> guessedNamesWithScore = new ArrayList<StringElement>();
			for (String name : nameListCpy) {
				int score = 0;
				for (int i = 0; i < listOfPerms.size(); i++) { //name.length() - 1
					List<StringElement> perms = listOfPerms.get(i);
					String perm = name.substring(i, i + hijackedDict.getQNumber());
					score += StringElement.getPenaltyElementOutOfCollection(perms, perm);
				}
				guessedNamesWithScore.add(new StringElement(name, score));
			}
			// sort the names according to the score
			Collections.sort(guessedNamesWithScore, new Comparator<StringElement>() {

				@Override
				public int compare(StringElement o1, StringElement o2) {
					
					if (o1.getPenaltyElement() == o2.getPenaltyElement()) {
						return 0;
					} else if (o1.getPenaltyElement() > o2.getPenaltyElement()) {
						return 1;
					} else {
						return -1;
					}
				}
			});
			
			// display the results
//			System.out.println("--------------------------------------");
//			System.out.println("Display possible matches for " 
//					+ Arrays.toString(DataEncryptor.decryptedStringArray((String[])volnWordEntry.getKey().toArray(), "password")) 
//					+ ", with encrypt version of: " + Arrays.toString((String[])volnWordEntry.getKey().toArray()));
//			String actualName = QGram.convertFromQGramArray(DataEncryptor.decryptedStringArray((String[])volnWordEntry.getKey().toArray(), "password"));
//			System.out.print("\t");
//			for (StringElement element : guessedNamesWithScore) {
//				System.out.print(element + ", ");
//			}
//			System.out.println();
//			if (nameListCpy.contains(actualName) || nameListCpy.size() >= 1) {
//				System.out.print("Found a match!");
//				if (nameListCpy.size() == 1) {
//					System.out.print(" <---- And this is the only match!!");
//					if (nameListCpy.get(0).equals(actualName)) {
//						System.out.print(" <---- CORRECT ONE !!");
//					} else {
//						System.out.println();
//					}
//				} else {
//					System.out.println();
//				}
//			}
//			System.out.println("--------------------------------------\n");
//			System.out.println("Processed " + (++tmpCounter) + " out of " + volnurabilityList.size());
			
			// build/updated the guessed dictionary (1 match only)
			if (guessedNamesWithScore.size() >= 1) {
				List<String> encryptedQGrams = volnWordEntry.getKey();
				for (int i = 0; i < encryptedQGrams.size(); i++) {
					List<StringElement> possibleQGramDecryptions = listOfPerms.get(i);
					String perm = guessedNamesWithScore.get(0).getStringElement().substring(i, i + hijackedDict.getQNumber());
					int penatly = StringElement.getPenaltyElementOutOfCollection(possibleQGramDecryptions, perm);
					adjustedGuessedDictionaryOneMatchOnly.addToDict(encryptedQGrams.get(i),perm);
					adjustedGuessedDictionaryOneMatchOnly.addToWeightedDict(encryptedQGrams.get(i), perm, penatly);
				}
			}
			
		}
		
		// print the guessed dictionaries
		adjustedGuessedDictionaryOneMatchOnly.sort();
//		System.out.println("\nPrinting guessed dictionary (occurence-based):");
//		System.out.println(adjustedGuessedDictionaryOneMatchOnly.printNormalDictinary());
//		System.out.println("----------------Done\n");
//		
//		System.out.println("\nPrinting guessed dictionary (weight-based):");
//		System.out.println(adjustedGuessedDictionaryOneMatchOnly.printWeigthedDictinary());
//		System.out.println("----------------Done\n");
		
		Map<String, CorrectlyDecryptedQGram> decryptedQGramDict = adjustedGuessedDictionaryOneMatchOnly.createOnlyHighestScoreMatchesDict();
		
		hijackedDict.analyzeQualityOfGuessedDictionary(decryptedQGramDict);
	}
	
	private List<List<StringElement>> createListOfPossiblePermutations(List<String> volunrableName) {
		
		List<List<StringElement>> permutations = null;
		
		for (String encryptedQGram : volunrableName) {
			// represents how frequent the qGram is in the hijackedDict (1 - the most frequent)
			int qGramIndex = hijackedDict.getIndexOfQGram(encryptedQGram);
			
			if (qGramIndex > guessedDict.getDictionrySize()) {
				return null;
			}
			
			List<StringElement> possibleDecryptions = getPossibleDecryptions(qGramIndex);
			
			// if null, then it is the first iteration add all possible permutations
			if (permutations == null) {
				permutations = new ArrayList<List<StringElement>>();
				permutations.add(possibleDecryptions);
			} 
			// if not, then keep only those permutations that begin with the same character 
			else {
				List<StringElement> tmp = new ArrayList<StringElement>();
				for (StringElement currentGuessElem : possibleDecryptions) {
					int charsToCheck = hijackedDict.getQNumber() - 1;
					String currentElementPrefix = currentGuessElem.getStringElement().substring(0, charsToCheck);
					
					for (StringElement prevGuessElem : permutations.get(permutations.size() - 1)) {
						if (prevGuessElem.getStringElement().endsWith(currentElementPrefix)) {
							tmp.add(currentGuessElem);
							break;
						}
					}
				}
				permutations.add(tmp);
			}
		}
		
		return permutations;
	}
	
	private List<StringElement> getPossibleDecryptions(int qGramIndex) {
		
		List<StringElement> ret = new ArrayList<StringElement>();
		ret.add(new StringElement(guessedDict.getNthMostCommmonQGram(qGramIndex),0));
		int elementsAdded = 1;
		
		for (int i = qGramIndex - 1; i >= 1 && elementsAdded < permListRange/2; i--) {
			ret.add(new StringElement(guessedDict.getNthMostCommmonQGram(i), calculatePenalty(qGramIndex, i)));
			elementsAdded++;
		}
		
		int index = qGramIndex;
		while (elementsAdded < permListRange && guessedDict.getNthMostCommmonQGram(index + 1) != null) {
			index = index + 1;
			ret.add(new StringElement(guessedDict.getNthMostCommmonQGram(index), calculatePenalty(qGramIndex, index)));
			elementsAdded++;
		}
		
		return ret;
	}
	
	private int calculatePenalty(int qGramIndex, int otherIndex) {
		
		return Math.abs(qGramIndex - otherIndex);
	}
	
	private Map<Integer, List<String>> listOfSubDict = null;
	private List<String> getSubDictOfLengthN(int N, List<String> dict) {
		
		if (listOfSubDict == null) {
			listOfSubDict = new HashMap<Integer, List<String>>();
			// find the longest string
			int maxLength = 0;
			for (String word : dict) {
				maxLength = Math.max(maxLength, word.length());
			}
			// for each number 1...maxLength prepare a subdict
			for (int i = 1; i <= maxLength; i++) {
				List<String> listOfWordOfLengthI = new ArrayList<String>();
				for (String word : dict) {
					if (word.length() == i) {
						listOfWordOfLengthI.add(word);
					}
				}
				listOfSubDict.put(new Integer(i), listOfWordOfLengthI);
			}
		}
		
		if (listOfSubDict.get(new Integer(N)) == null) {
			return new ArrayList<String>();
		}
		
		return listOfSubDict.get(new Integer(N));
	}
	
	private static class StringElement {
		
		private String perm;
		private int penalty;
		
		public StringElement(String perm, int penalty) {
			
			this.perm = perm.toLowerCase();
			this.penalty = penalty;
		}
		
		public String getStringElement() {
			
			return this.perm;
		}
		
		public int getPenaltyElement() {
			
			return this.penalty;
		}
		
		public boolean isPartOf(Collection<StringElement> elements) {
			
			boolean ret = false;
			
			for (StringElement element : elements) {
				if (element.getStringElement().equals(this.perm)) {
					ret = true;
				}
			}
			
			return ret;
		}
		
		public static int getPenaltyElementOutOfCollection(Collection<StringElement> elements, String perm) {
			
			for (StringElement element : elements) {
				if (element.getStringElement().equals(perm)) {
					return element.getPenaltyElement();
				}
			}
			
			return -1;
		}
		
		@Override
		public String toString() {
			
			return this.perm + " : " + this.penalty;
		}
	}
}
