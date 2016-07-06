package edu.dase.prl.qGramFrequencyAnalysis.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.dase.prl.encryption.DataEncryptor;
import edu.dase.prl.encryption.DataEncryptorException;

public class GuessedDictionary {

	private Map<String, List<BasicGuessedElement>> guessedDict;
	private Map<String, List<WeightedGuessedElement>> weigthedGuessedDict;
	
	private Map<String, CorrectlyDecryptedQGram> onlyHighestScoreMatchesDict;
	
	public GuessedDictionary() {
		
		this.guessedDict = new HashMap<String, List<BasicGuessedElement>>();
		this.weigthedGuessedDict = new HashMap<String, List<WeightedGuessedElement>>();
	}
	
	public void addToDict(String encryptedQGram, String guessedElement) {
		
		if (!guessedDict.containsKey(encryptedQGram)) {
			List<BasicGuessedElement> guessedElements = new ArrayList<BasicGuessedElement>();
			guessedElements.add(new BasicGuessedElement(guessedElement));
			guessedDict.put(encryptedQGram, guessedElements);
		} else {
			List<BasicGuessedElement> guessedElements = guessedDict.get(encryptedQGram);
			for (BasicGuessedElement element : guessedElements) {
				if (element.element.equals(guessedElement)) {
					element.increaseOccurenceByOne();
					return;
				}
			}
			guessedElements.add(new BasicGuessedElement(guessedElement));
		}
	}
	
	public void addToWeightedDict(String encryptedQGram, String guessedElement, int weight) {
	
		if (!weigthedGuessedDict.containsKey(encryptedQGram)) {
			List<WeightedGuessedElement> guessedElements = new ArrayList<WeightedGuessedElement>();
			guessedElements.add(new WeightedGuessedElement(guessedElement, weight));
			weigthedGuessedDict.put(encryptedQGram, guessedElements);
		} else {
			List<WeightedGuessedElement> guessedElements = weigthedGuessedDict.get(encryptedQGram);
			for (WeightedGuessedElement element : guessedElements) {
				if (element.element.equals(guessedElement)) {
					element.modifyWeight(weight);
					return;
				}
			}
			guessedElements.add(new WeightedGuessedElement(guessedElement, weight));
		}
	}
	
	public Map<String, CorrectlyDecryptedQGram> createOnlyHighestScoreMatchesDict() throws Exception {
		onlyHighestScoreMatchesDict = new HashMap<String, CorrectlyDecryptedQGram>();
		
		for (Map.Entry<String, List<BasicGuessedElement>> entry : guessedDict.entrySet()) {
			String decryption = DataEncryptor.decryptString(entry.getKey(), "password");
			if (entry.getValue().contains(new BasicGuessedElement(decryption))) {
				if (entry.getValue().size() == 1) {
					onlyHighestScoreMatchesDict.put(entry.getKey(), new CorrectlyDecryptedQGram(entry.getKey(), decryption));
					continue;
				}
				
				int highestOcc = entry.getValue().get(0).occurence;
				List<String> correctDecryptions = new ArrayList<String>();
				for (BasicGuessedElement element : entry.getValue()) {
					if (element.occurence == highestOcc) {
						correctDecryptions.add(element.element);
					} else {
						if (correctDecryptions.contains(decryption)) {
							onlyHighestScoreMatchesDict.put(entry.getKey(), new CorrectlyDecryptedQGram(entry.getKey(), correctDecryptions));
						}
						break;
					}
				}
				if (!correctDecryptions.isEmpty() && correctDecryptions.contains(decryption)) {
					onlyHighestScoreMatchesDict.put(entry.getKey(), new CorrectlyDecryptedQGram(entry.getKey(), correctDecryptions));
				}
			}
		}
		
		
//		for (Map.Entry<String, CorrectlyDecryptedQGram> entry : onlyHighestScoreMatchesDict.entrySet()) {
//			System.out.println(entry.getKey() + " : " + entry.getValue().getDecryptedQGram().toString());
//		}
		
		return onlyHighestScoreMatchesDict;
	}
	
	public String printNormalDictinary() {
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, List<BasicGuessedElement>> entry : guessedDict.entrySet()) {
			builder.append(entry.getKey() + ", ");
			String decryption = null;
			try {
				decryption = DataEncryptor.decryptString(entry.getKey(), "password");
				if (entry.getValue().contains(new BasicGuessedElement(decryption))) {
					
					int highestOcc = entry.getValue().get(0).occurence;
					for (BasicGuessedElement element : entry.getValue()) {
						if (highestOcc == element.occurence) {
							if (element.element.equals(decryption)) {
								if (entry.getValue().size() == 1) {
									builder.append(decryption + " (HIGHEST SINGLE SCORE MATCH), ");
								} else {
									builder.append(decryption + " (HIGHEST SCORE MATCH), ");
								}
								break;
							}
						} else {
							builder.append(decryption + " (MATCH), ");
							break;
						}
					}
					
				} else {
					builder.append(decryption + ", ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (BasicGuessedElement element : entry.getValue()) {
				builder.append(element.element + " : " + element.occurence + ", ");
			}
			builder.delete(builder.length() - 2, builder.length());
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public String printWeigthedDictinary() {
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, List<WeightedGuessedElement>> entry : weigthedGuessedDict.entrySet()) {
			builder.append(entry.getKey() + ", ");
			String decryption = null;
			try {			
				decryption = DataEncryptor.decryptString(entry.getKey(), "password");
				if (entry.getValue().contains(new WeightedGuessedElement(decryption, 0))) {
					
					double lowestScore = entry.getValue().get(0).score;
					for (WeightedGuessedElement element : entry.getValue()) {
						if (lowestScore == element.score) {
							if (element.element.equals(decryption)) {
								if (entry.getValue().size() == 1) {
									builder.append(decryption + " (HIGHEST SINGLE SCORE MATCH), ");
								} else {
									builder.append(decryption + " (HIGHEST SCORE MATCH), ");
								}
								break;
							}
						} else {
							builder.append(decryption + " (MATCH), ");
							break;
						}
					}
					
				} else {
					builder.append(decryption + ", ");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (WeightedGuessedElement element : entry.getValue()) {
				builder.append(element.element + " : " + element.score + ", ");
			}
			builder.delete(builder.length() - 2, builder.length());
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public void sort() {
		
		if (guessedDict != null && guessedDict.size() > 1) {
			for (Map.Entry<String, List<BasicGuessedElement>> entry : guessedDict.entrySet()) {
				Collections.sort(entry.getValue(), new Comparator<BasicGuessedElement>() {

					@Override
					public int compare(BasicGuessedElement o1,
							BasicGuessedElement o2) {
						
						if (o1.occurence == o2.occurence) {
							return 0;
						} else if (o1.occurence < o2.occurence) {
							return 1;
						} else {
							return -1;
						}
					}
				});
			}
		}
		
		if (weigthedGuessedDict != null && weigthedGuessedDict.size() > 1) {
			for (Map.Entry<String, List<WeightedGuessedElement>> entry : weigthedGuessedDict.entrySet()) {
				Collections.sort(entry.getValue(), new Comparator<WeightedGuessedElement>() {

					@Override
					public int compare(WeightedGuessedElement o1,
							WeightedGuessedElement o2) {
						
						if (o1.score == o2.score) {
							return 0;
						} else if (o1.score > o2.score) {
							return 1;
						} else {
							return -1;
						}
					}
				});
			}
		}
	}
	
	public static class BasicGuessedElement {
		
		private String element;
		private int occurence;
		
		public BasicGuessedElement(String element, int occurence) {
			
			this.element = element;
			this.occurence = occurence;
		}
		
		public BasicGuessedElement(String element) {
			
			this(element, 1);
		}
		
		public void increaseOccurenceByOne() {
			
			this.occurence++;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (obj == null) {
				return false;
			}
			if (!BasicGuessedElement.class.isAssignableFrom(obj.getClass())) {
				return false;
			}
			final BasicGuessedElement other = (BasicGuessedElement) obj;
			if (!this.element.equals(other.element)) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public String toString() {
			
			return element + " : " + occurence;
		}
	}
	
	public static class CorrectlyDecryptedQGram {
		
		private String encryptedQGram;
		private List<String> possibleDecryptions;
		private String correctDecryption;
		
		public CorrectlyDecryptedQGram(String encryptedQGram, List<String> possibleDecryptions) {
			this.encryptedQGram = encryptedQGram;
			this.possibleDecryptions = new ArrayList<String>(possibleDecryptions);
			
			try {
				this.correctDecryption = DataEncryptor.decryptString(encryptedQGram, "password");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public CorrectlyDecryptedQGram(String encryptedQGram, String decryption) {
			this.encryptedQGram = encryptedQGram;
			this.possibleDecryptions = new ArrayList<String>();
			this.possibleDecryptions.add(decryption);
			
			try {
				this.correctDecryption = DataEncryptor.decryptString(encryptedQGram, "password");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public String getEncryptedQGram() {
			
			return encryptedQGram;
		}
		
		public List<String> getDecryptedQGram() {
			
			return this.possibleDecryptions;
		}
		
		public String getCorrectDecryption() {
			
			return this.correctDecryption;
		}
	}
	
	public static class WeightedGuessedElement {
		
		private String element;
		private int occurence;
		private int distance;
		
		private double score;
		
		public WeightedGuessedElement(String element, int occurence, int distance) {
			
			this.element = element;
			this.occurence = occurence;
			this.distance = distance;
			this.score = (double)distance/occurence;
		}
		
		public WeightedGuessedElement(String element, int distance) {
			
			this(element, 1, distance);
		}
		
		public void modifyWeight(int distance) {
			
			this.occurence++;
			this.distance += distance;
			this.score = (double)distance/occurence;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (obj == null) {
				return false;
			}
			if (!WeightedGuessedElement.class.isAssignableFrom(obj.getClass())) {
				return false;
			}
			final WeightedGuessedElement other = (WeightedGuessedElement) obj;
			if (!this.element.equals(other.element)) {
				return false;
			}
			
			return true;
		}
		
		@Override
		public String toString() {
			
			return element + " : " + score;
		}
	}
}
