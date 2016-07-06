package edu.dase.prl.qGramFrequencyAnalysis.driver;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import edu.dase.prl.benchMark.records.SyntheticRecord;
import edu.dase.prl.benchMark.utils.CSVFileException;
import edu.dase.prl.benchMark.utils.CSVFileReader;
import edu.dase.prl.qGramFrequencyAnalysis.utils.QGramDictionary;
import edu.dase.prl.qGramFrequencyAnalysis.utils.QGramDictionary.SyntheticRecordQGrams;

public class RunGuessingOnLastName {

	public static void main(String[] args) throws Exception {
		
		for (int qGramNumber = 1; qGramNumber <= 5; qGramNumber++) {
			for (int offset = 10; offset < 160; offset += 10) {
				System.out.println("Running test for q = " + qGramNumber + " and offset = " + offset);
				runGuessing(qGramNumber, offset);
				System.out.println("---Done");
			}
		}
	}
	
	public static void runGuessing(int qGram, int offset) throws Exception {
		
		// load in two dictionaries (hijacked qGrams and attacker's dicts)
		QGramDictionary hijackedQGrams = new QGramDictionary(qGram, true);
		ArrayList<Map.Entry<List<String>, Double>> volnurabilityList = 
				hijackedQGrams.initFromSyntheticRecordListGenerateVulnerabilityList("src/main/resources/qGramFrequencyAnalysis/syntheticUncorruptedDataset/synthetic_records_org.csv", 
						SyntheticRecord.RECORD_HEADER_ENCRYPTED, SyntheticRecordQGrams.LAST_NAMES, false);
		
		QGramDictionary attackerQGrams = new QGramDictionary(qGram, false);
		attackerQGrams.initFromFile("src/main/resources/qGramFrequencyAnalysis/attackerRecords/lastNames.csv", new String[]{"lastName"});
		
		// load in the dictionary with correct answers
		QGramDictionary correctQGrams = new QGramDictionary(qGram, false);
		correctQGrams.initFromSyntheticRecordList("src/main/resources/qGramFrequencyAnalysis/syntheticUncorruptedDataset/synthetic_records_org.csv", 
				SyntheticRecord.RECORD_HEADER_ENCRYPTED, SyntheticRecordQGrams.LAST_NAMES, false);
				
		// display the overlap, just to make sure that the attacker dictionary has some value in it
		correctQGrams.checkOverlapBetweenDictionaries(attackerQGrams);
		
		// construct the "guessed" dictionary
		QGramDictionary guessedDictionary = new QGramDictionary(qGram, false);
		for (int i = 0; i < hijackedQGrams.getDictionrySize(); i++) {
			if (attackerQGrams.getNthMostCommmonQGram(i + 1) == null) {
				break;
			}
			guessedDictionary.addEntry(new AbstractMap.SimpleEntry<String, Integer>(attackerQGrams.getNthMostCommmonQGram(i + 1), hijackedQGrams.getNthMostCommonDictionaryEntry(i + 1).getValue()));
		}
		
		List<String> nameList = readInListOfNames("src/main/resources/qGramFrequencyAnalysis/attackerRecords/lastNames.csv");
		
		DictionaryAttack dictAttack = new DictionaryAttack(hijackedQGrams, guessedDictionary, nameList, volnurabilityList, offset);
		dictAttack.performGuessing();
	}
	
	private static List<String> readInListOfNames(String fileName) throws CSVFileException, IOException {
	
		CSVFileReader csvReader = new CSVFileReader(new String[]{"firstName"}, fileName);
		List<String> ret = csvReader.loadListOfNamesForFrequencyAttack();
		
		return ret;
	}
}
